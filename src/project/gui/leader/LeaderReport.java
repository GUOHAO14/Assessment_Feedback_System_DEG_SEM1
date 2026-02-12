/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.leader;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.utils.*;
import project.roles.*;
import project.roles.Module;
import project.roles.Class;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author US
 */
public class LeaderReport extends FrameFormat {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderReport.class.getName());
    private Leader sessionUser;
    private DefaultTableModel reportTableModel;

    public LeaderReport(Leader sessionUser) {
        initComponents();
        super.formatWindow("Generate Analyzed Reports");
        this.sessionUser = sessionUser;

        reportTableModel = new DefaultTableModel();
        reportTable.setModel(reportTableModel);

        loadReportTypes();
    }

    private void loadReportTypes() {
        reportTypeCombo.removeAllItems();
        reportTypeCombo.addItem("-- Select Report Type --");
        reportTypeCombo.addItem("Module Performance Summary");
        reportTypeCombo.addItem("Lecturer Assignment Report");
        reportTypeCombo.addItem("Class Performance Report");
        reportTypeCombo.addItem("Student Performance by Module");
        reportTypeCombo.addItem("Assessment Performance Report");
        reportTypeCombo.addItem("Overall Academic Summary");
    }

    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();

        if (reportType == null || reportType.startsWith("--")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a report type!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        reportTableModel.setRowCount(0);
        reportTableModel.setColumnCount(0);

        switch (reportType) {
            case "Module Performance Summary":
                generateModulePerformanceReport();
                break;
            case "Lecturer Assignment Report":
                generateLecturerAssignmentReport();
                break;
            case "Class Performance Report":
                generateClassPerformanceReport();
                break;
            case "Student Performance by Module":
                generateStudentPerformanceReport();
                break;
            case "Assessment Performance Report":
                generateAssessmentPerformanceReport();
                break;
            case "Overall Academic Summary":
                generateOverallSummaryReport();
                break;
        }
    }

    private void generateModulePerformanceReport() {
        reportTableModel.setColumnIdentifiers(new String[]{
            "Module ID", "Module Name", "Total Classes", "Assigned Lecturers", "Total Students", "Total Assessments", "Avg Score"
        });

        for (Module module : sessionUser.Lea_Modules) {
            int totalClasses = 0;
            int totalStudents = 0;
            int totalAssessments = 0;
            double totalScore = 0;
            int scoreCount = 0;

            for (IntakeModule im : InteractTxt.allIntakeModule) {
                if (im.getModuleId().equals(module.getModuleId())) {
                    totalClasses += im.IM_Classes.size();
                    totalAssessments += im.IM_Assessments.size();

                    for (Class classObj : im.IM_Classes) {
                        totalStudents += classObj.Class_Students.size();

                        for (Student student : classObj.Class_Students) {
                            for (StudentScore score : student.Stu_Scores) {
                                if (score.getAssessment().getAssIM().getIMID().equals(im.getIMID())) {
                                    try {

                                        double originalScore = Double.parseDouble(score.getOrginalScore());
                                        double fullMarks = Double.parseDouble(score.getOriginalFullMarks());

                                        double percentageScore = (originalScore / fullMarks) * 100;

                                        totalScore += percentageScore;
                                        scoreCount++;
                                    } catch (NumberFormatException | ArithmeticException e) {
                                        // Skip invalid scores or division by zero
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String avgScore = scoreCount > 0
                    ? String.format("%.2f%%", totalScore / scoreCount)
                    : "N/A";

            Object[] rowData = {
                module.getModuleId(),
                module.getModuleName(),
                totalClasses,
                module.Mod_Lecturers.size(),
                totalStudents,
                totalAssessments,
                avgScore
            };
            reportTableModel.addRow(rowData);
        }
    }

    private void generateLecturerAssignmentReport() {
        reportTableModel.setColumnIdentifiers(new String[]{
            "Lecturer ID", "Lecturer Name", "Modules Assigned", "Classes Teaching", "Total Students"
        });

        for (Lecturer lecturer : sessionUser.leaderTeam) {
            int totalStudents = 0;

            for (Class classObj : lecturer.Lec_Classes) {
                totalStudents += classObj.Class_Students.size();
            }

            Object[] rowData = {
                lecturer.getId(),
                lecturer.getName(),
                lecturer.Lec_Modules.size(),
                lecturer.Lec_Classes.size(),
                totalStudents
            };
            reportTableModel.addRow(rowData);
        }
    }

    private void generateClassPerformanceReport() {
        reportTableModel.setColumnIdentifiers(new String[]{
            "Class ID", "Class Name", "Module", "Lecturer", "Students", "Avg Score"
        });

        for (Module module : sessionUser.Lea_Modules) {
            for (IntakeModule im : InteractTxt.allIntakeModule) {
                if (im.getModuleId().equals(module.getModuleId())) {
                    for (Class classObj : im.IM_Classes) {
                        Lecturer lecturer = InteractTxt.checkLecID(classObj.getLecId());
                        String lecturerName = lecturer != null ? lecturer.getName() : "Not Assigned";

                        double totalScore = 0;
                        int scoreCount = 0;

                        for (Student student : classObj.Class_Students) {
                            for (StudentScore score : student.Stu_Scores) {

                                if (score.getAssessment().getAssIM().getIMID().equals(im.getIMID())) {
                                    try {
                                        double originalScore = Double.parseDouble(score.getOrginalScore());
                                        double fullMarks = Double.parseDouble(score.getOriginalFullMarks());

                                        double percentageScore = (originalScore / fullMarks) * 100;

                                        totalScore += percentageScore;
                                        scoreCount++;
                                    } catch (NumberFormatException | ArithmeticException e) {
                                        // Skip invalid scores or division by zero
                                    }
                                }
                            }
                        }

                        String avgScore = scoreCount > 0
                                ? String.format("%.2f%%", totalScore / scoreCount)
                                : "N/A";

                        Object[] rowData = {
                            classObj.getClassId(),
                            classObj.getClassName(),
                            module.getModuleName(),
                            lecturerName,
                            classObj.Class_Students.size(),
                            avgScore
                        };
                        reportTableModel.addRow(rowData);
                    }
                }
            }
        }
    }

    private void generateStudentPerformanceReport() {
        reportTableModel.setColumnIdentifiers(new String[]{
            "Student ID", "Student Name", "Module", "Class", "Grade", "Comment"
        });

        for (Module module : sessionUser.Lea_Modules) {
            for (IntakeModule im : InteractTxt.allIntakeModule) {
                if (im.getModuleId().equals(module.getModuleId())) {
                    for (Class classObj : im.IM_Classes) {
                        for (Student student : classObj.Class_Students) {
                            String grade = student.getSpecificGrade(classObj);
                            String comment = student.getSpecificComment(classObj);

                            Object[] rowData = {
                                student.getId(),
                                student.getName(),
                                module.getModuleName(),
                                classObj.getClassName(),
                                grade != null ? grade : "N/A",
                                comment != null ? comment : "No comment"
                            };
                            reportTableModel.addRow(rowData);
                        }
                    }
                }
            }
        }
    }

    private void generateAssessmentPerformanceReport() {
        reportTableModel.setColumnIdentifiers(new String[]{
            "Assessment ID", "Assessment Name", "Type", "Module", "Intake", "Total Students", "Avg Score", "Pass Rate"
        });

        for (Module module : sessionUser.Lea_Modules) {
            for (IntakeModule im : InteractTxt.allIntakeModule) {
                if (im.getModuleId().equals(module.getModuleId())) {
                    Intake intake = InteractTxt.checkIntID(im.getIntakeId());
                    String intakeName = intake != null ? intake.getIntakeName() : im.getIntakeId();

                    for (Assessment assessment : im.IM_Assessments) {
                        int studentCount = 0;
                        double totalScore = 0;
                        int passCount = 0;

                        // Calculate scores for this assessment
                        for (Class classObj : im.IM_Classes) {
                            for (Student student : classObj.Class_Students) {
                                for (StudentScore score : student.Stu_Scores) {
                                    if (score.getAssessment().getAssId().equals(assessment.getAssId())) {
                                        try {

                                            double originalScore = Double.parseDouble(score.getOrginalScore());
                                            double fullMarks = Double.parseDouble(score.getOriginalFullMarks());

                                            double percentageScore = (originalScore / fullMarks) * 100;

                                            totalScore += percentageScore;
                                            studentCount++;

                                            if (percentageScore >= 40) {
                                                passCount++;
                                            }
                                        } catch (NumberFormatException | ArithmeticException e) {
                                            // Skip invalid scores or division by zero
                                        }
                                    }
                                }
                            }
                        }

                        String avgScore = studentCount > 0
                                ? String.format("%.2f%%", totalScore / studentCount)
                                : "N/A";

                        String passRate = studentCount > 0
                                ? String.format("%.1f%%", (passCount * 100.0) / studentCount)
                                : "N/A";

                        Object[] rowData = {
                            assessment.getAssId(),
                            assessment.getAssName(),
                            assessment.getAssType(),
                            module.getModuleName(),
                            intakeName,
                            studentCount,
                            avgScore,
                            passRate
                        };
                        reportTableModel.addRow(rowData);
                    }
                }
            }
        }
    }

    private void generateOverallSummaryReport() {
        reportTableModel.setColumnIdentifiers(new String[]{
            "Category", "Count", "Details"
        });

        reportTableModel.addRow(new Object[]{
            "Total Modules Managed",
            sessionUser.Lea_Modules.size(),
            "Modules under your leadership"
        });

        reportTableModel.addRow(new Object[]{
            "Total Lecturers in Team",
            sessionUser.leaderTeam.size(),
            "Lecturers reporting to you"
        });

        int totalClasses = 0;
        int totalStudents = 0;

        for (Module module : sessionUser.Lea_Modules) {
            for (IntakeModule im : InteractTxt.allIntakeModule) {
                if (im.getModuleId().equals(module.getModuleId())) {
                    totalClasses += im.IM_Classes.size();
                    for (Class classObj : im.IM_Classes) {
                        totalStudents += classObj.Class_Students.size();
                    }
                }
            }
        }

        reportTableModel.addRow(new Object[]{
            "Total Classes",
            totalClasses,
            "Active classes across all modules"
        });

        reportTableModel.addRow(new Object[]{
            "Total Students",
            totalStudents,
            "Students enrolled in your modules"
        });

        // Unassigned Classes
        int unassignedClasses = 0;
        for (Module module : sessionUser.Lea_Modules) {
            for (IntakeModule im : InteractTxt.allIntakeModule) {
                if (im.getModuleId().equals(module.getModuleId())) {
                    for (Class classObj : im.IM_Classes) {
                        if (classObj.getLecId() == null || classObj.getLecId().equals("NA")) {
                            unassignedClasses++;
                        }
                    }
                }
            }
        }

        reportTableModel.addRow(new Object[]{
            "Unassigned Classes",
            unassignedClasses,
            "Classes without assigned lecturers"
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        reportTypeCombo = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(reportTable);

        jButton1.setText("Generate Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        reportTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Analyzed Report");

        jLabel2.setText("Please select a type of report to generate.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(reportTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 846, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reportTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        generateReport();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new LeaderDashboard(sessionUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LeaderReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaderReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaderReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaderReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new LeaderReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable reportTable;
    private javax.swing.JComboBox<String> reportTypeCombo;
    // End of variables declaration//GEN-END:variables
}
