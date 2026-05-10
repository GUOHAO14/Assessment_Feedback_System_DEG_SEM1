/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.lecturer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import project.roles.*;
import project.utils.*;
import project.utils.exceptions.*;
/**
 *
 * @author Khoo Guo Hao
 */
public class EnterAssessmentMarks0 extends FrameFormat {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EnterAssessmentMarks0.class.getName());
    private final Lecturer sessionUser;
    private final DefaultTableModel model = new DefaultTableModel();
    private final String [] columnName = {"Student ID", "Student Name", "Class", "GPA"};
    private int row = -1, totalStuInTable = 0;
    
    private ArrayList<String> ownMods = new ArrayList<>();
    private ArrayList<String> ownClasses = new ArrayList<>();
    private Intake chosenIntake = null;
    private IntakeModule chosenIM = null;
    private project.roles.Class chosenClass = null;
    private Student chosenStudent = null;
    private String intakeFullName;
    private String moduleFullName;
    
    // load all students in a class
    private void loadTable(ArrayList<Student> studentArray, project.roles.Class c) {
        for (Student s : studentArray) {
            String grade = s.getSpecificGrade(c);
            String [] record = {s.getId(), s.getName(), c.getClassId(), grade};
            model.addRow(record);
            totalStuInTable++;
        }
        
        studentCountLabel.setText("Row Count: "+String.valueOf(totalStuInTable));
        totalStuInTable = 0;
    }
    
    // load specific student in table
    private void loadTable(project.roles.Class c, Student s) {
        String grade = s.getSpecificGrade(c);
        String [] record = {s.getId(), s.getName(), c.getClassId(), grade};
        model.addRow(record);
        totalStuInTable++;
        
        studentCountLabel.setText("Row Count: "+String.valueOf(totalStuInTable));
        totalStuInTable = 0;
    }
    
    //utility methods
    private void disableModuleDropdown() {
        moduleDropdown.setEnabled(false);
        moduleDropdown.setEditable(true);
        AutoCompleteDecorator.decorate(moduleDropdown);
        chosenIM = null;
    }
    
    private void disableClassDropdown() {
        classDropdown.setEnabled(false);
        classDropdown.setEditable(true);
        AutoCompleteDecorator.decorate(classDropdown);
        chosenClass = null;
    }
    
    private void disableStudentDropdown() {
        studentDropdown.setEnabled(false);
        studentDropdown.setEditable(true);
        AutoCompleteDecorator.decorate(studentDropdown);
        chosenStudent = null;
    }
    
    private String finalScoreCalc(String oriScore, String fullMarks, String assPercent) {
        String finalScore;
        try {
            if (oriScore.isEmpty()) {
                return "0";
            }
            float score = Float.parseFloat(oriScore);
            float fullScore = Float.parseFloat(fullMarks);
            
            if (score > fullScore) {
                return "Error - Range";
            }
            float percent = Float.parseFloat(assPercent);
            
            float floatFinalScore = score / fullScore * percent;
            
            finalScore = String.format("%.1f", floatFinalScore);
            
        } catch (NumberFormatException e) {
            finalScore = "Error - NFE";
        } catch (Exception e) {
            finalScore = "Error";
        } 
        return finalScore;
    }
    
    //nested class used for input tracking
    private class scoreInputGroup {
        private String assId;
        private JTextField score; 
        private JTextArea feedback; 

        private scoreInputGroup(String assId, JTextField score, JTextArea feedback) {
            this.assId = assId;
            this.score = score;
            this.feedback = feedback;
        }

        public String getAssId() {
            return assId;
        }

        public void setAssId(String assId) {
            this.assId = assId;
        }

        public JTextField getScore() {
            return score;
        }

        public void setScore(JTextField score) {
            this.score = score;
        }

        public JTextArea getFeedback() {
            return feedback;
        }

        public void setFeedback(JTextArea feedback) {
            this.feedback = feedback;
        }
        
    }
    
    // constructor
    public EnterAssessmentMarks0(Lecturer sessionUser) {
        model.setColumnIdentifiers(columnName);
        initComponents();
        super.formatWindow("Enter Assessment Details - Select Student");
        this.sessionUser = sessionUser;
        
        mainTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        mainTable.setAutoCreateRowSorter(true);
        mainTable.setDefaultEditor(Object.class, null);
        
        sessionUser.Lec_Modules.forEach(m -> {
            ownMods.add(m.getModuleId());
        });
        
        sessionUser.Lec_Classes.forEach(c->{
            ownClasses.add(c.getClassId());
        });
        
        intakeDropdown.setEditable(true);
        
        InteractTxt.allIntake.forEach(i -> {
            String item = i.getIntakeId() + " (" + i.getIntakeName() + ")";
            intakeDropdown.addItem(item);
        });
        
        
        intakeDropdown.addItem("None");
        intakeDropdown.setSelectedItem("None");
        AutoCompleteDecorator.decorate(intakeDropdown);
        intakeDropdown.setMaximumRowCount(6);
        
        moduleDropdown.setMaximumRowCount(6);
        disableModuleDropdown();
        
        classDropdown.setMaximumRowCount(6);
        disableClassDropdown();
        
        studentDropdown.setMaximumRowCount(6);
        disableStudentDropdown();
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
        jTable1 = new javax.swing.JTable();
        intakeDropdown = new javax.swing.JComboBox<>();
        moduleDropdown = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        searchStudentsButt = new javax.swing.JButton();
        textForUser = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        classDropdown = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        studentDropdown = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        mainTable = new javax.swing.JTable();
        studentCountLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        intakeLabel = new javax.swing.JLabel();
        moduleLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        intakeDropdown.addActionListener(this::intakeDropdownActionPerformed);

        moduleDropdown.addActionListener(this::moduleDropdownActionPerformed);

        jLabel1.setText("Intake*");

        jLabel2.setText("Module*");
        jLabel2.setMaximumSize(new java.awt.Dimension(38, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(38, 16));

        searchStudentsButt.setText("Search");
        searchStudentsButt.addActionListener(this::searchStudentsButtActionPerformed);

        textForUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        textForUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        textForUser.setText("Filter Search");

        jLabel3.setText("Class");

        classDropdown.addActionListener(this::classDropdownActionPerformed);

        jLabel4.setText("Student");

        studentDropdown.addActionListener(this::studentDropdownActionPerformed);

        mainTable.setModel(model);
        mainTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mainTableMouseReleased(evt);
            }
        });
        mainTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mainTableKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(mainTable);

        studentCountLabel.setText("Total Count: ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Click Student Row to Insert/Edit Assessment Marks");

        intakeLabel.setText("Intake: ");

        moduleLabel.setText("Module:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Enter Student Assessment Marks");

        backButton.setText("Back");
        backButton.addActionListener(this::backButtonActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(studentCountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(intakeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(moduleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textForUser, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(58, 58, 58))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(intakeDropdown, 0, 231, Short.MAX_VALUE)
                            .addComponent(classDropdown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(moduleDropdown, 0, 231, Short.MAX_VALUE)
                            .addComponent(studentDropdown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(58, 58, 58))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(searchStudentsButt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textForUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intakeDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(moduleDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(studentDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchStudentsButt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intakeLabel)
                    .addComponent(moduleLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentCountLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void intakeDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intakeDropdownActionPerformed
        // TODO add your handling code here:
        disableModuleDropdown();
        moduleDropdown.removeAllItems();
        String choice = intakeDropdown.getSelectedItem().toString();
        System.out.println(choice);

        if (!InteractTxt.allIntake.contains(InteractTxt.checkIntID(choice.split(" ")[0])) || choice == null || choice.isEmpty() || choice.equals("None")) chosenIntake = null;
        else {
            // valid choice
            System.out.println("------");
            String chosenIntakeId = choice.split(" \\(")[0];
            System.out.println(chosenIntakeId);
            chosenIntake = null;
            InteractTxt.allIntake.forEach(i -> {
                String intakeId = i.getIntakeId();

                if (intakeId.equals(chosenIntakeId)) {
                    intakeFullName = chosenIntakeId+" ("+i.getIntakeName()+")";
                    chosenIntake = i;
                }
            });

            System.out.println("Selected i: " + chosenIntake.getIntakeName());
            System.out.println("Selected id: " + chosenIntake.getIntakeId());
            System.out.println("Modules: " + InteractTxt.checkInt_Modules(chosenIntake.getIntakeId()));

            //find overlap of modules from chosen intake
            //and the lecturer's (sessionUser) own modules

            moduleDropdown.addItem("None");
            moduleDropdown.setSelectedItem("None");
            
            InteractTxt.checkInt_Modules(chosenIntake.getIntakeId()).forEach(m -> {
                String modId = m.getModuleId();
                if (ownMods.contains(modId)) moduleDropdown.addItem(modId+" ("+m.getModuleName()+")");
            });

            moduleDropdown.setEnabled(true);
        }
    }//GEN-LAST:event_intakeDropdownActionPerformed

    private void moduleDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleDropdownActionPerformed
        // TODO add your handling code here:
        disableClassDropdown();
        classDropdown.removeAllItems();
        try {
            String choice = moduleDropdown.getSelectedItem().toString();
            System.out.println("hello: "+choice);
            System.out.println(choice);

            if (!InteractTxt.allModule.contains(InteractTxt.checkModID(choice.split(" ")[0])) || choice == null || choice.isEmpty() || choice.equals("None")) chosenIM = null;
            else {
                // valid choice
                System.out.println("------");

                String chosenModuleId = choice.split(" \\(")[0];

                System.out.println(chosenModuleId);
                
                InteractTxt.allIntakeModule.forEach(im -> {
                    if (chosenIntake.getIntakeId().equals(im.getIntakeId()) && chosenModuleId.equals(im.getModuleId())) {
                        moduleFullName = chosenModuleId+" ("+InteractTxt.checkModID(chosenModuleId).getModuleName()+")";
                        chosenIM = im;
                    }
                });

                if (chosenIM == null) {
                    JOptionPane.showMessageDialog(this, "Filter selection failed.\nReport this error.", "Error - Unknown Error", 0);
                } else {
                    
                    classDropdown.addItem("None");
                    classDropdown.setSelectedItem("None");
                    
                    chosenIM.IM_Classes.forEach(c -> {
                        String classId = c.getClassId();
                        if (ownClasses.contains(classId)) classDropdown.addItem(classId+" ("+c.getClassName()+")");
                    });
                    
                    classDropdown.setEnabled(true);
                    
                }
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("Error recorded: dropdown referenced without user action. Safe to ignore.");
        }
    }//GEN-LAST:event_moduleDropdownActionPerformed

    private void searchStudentsButtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStudentsButtActionPerformed
        // TODO add your handling code here:
        model.setRowCount(0);
        if (chosenIntake == null || chosenIM == null) {
            JOptionPane.showMessageDialog(this, "Intake and Module must be chosen to proceed.\nIf you did, kindly report this error.", "Error - Incomplete Action", 0);
        } else {
            //chosenIM must be present
            int total = 0;
                    
            if (chosenClass == null && chosenStudent != null) JOptionPane.showMessageDialog(this, "Student selection failed.\nReport this error.", "Error - Unknown Error", 0);
            else if (chosenClass == null && chosenStudent == null) {
                // only choose intake module - show everyone in the intake module who is under the session user
                
                for (project.roles.Class c : chosenIM.IM_Classes) {
                    String tableClassId = c.getClassId();
                    if (ownClasses.contains(tableClassId)) {
                        loadTable(c.Class_Students, c);
                    }
                }
            } else if (chosenClass != null && chosenStudent == null) {
                // choose intake & class - show everyone in that class (already guaranteed under session user)
                loadTable(chosenClass.Class_Students, chosenClass);
            } else if (chosenClass != null && chosenStudent != null) {
                //class & student chosen - show only the student (already guaranteed under session user)
                loadTable(chosenClass, chosenStudent);
            }
            
            intakeLabel.setText("Intake: "+intakeFullName);
            moduleLabel.setText("Module: "+moduleFullName);
        }
    }//GEN-LAST:event_searchStudentsButtActionPerformed

    private void classDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classDropdownActionPerformed
        // TODO add your handling code here:
        disableStudentDropdown();
        studentDropdown.removeAllItems();
        try {
            String choice = classDropdown.getSelectedItem().toString();
            System.out.println(choice);

            if (!InteractTxt.allClass.contains(InteractTxt.checkClassID(choice.split(" ")[0])) || choice == null || choice.isEmpty() || choice.equals("None")) chosenClass = null;
            else {
                // valid choice
                System.out.println("------");

                String chosenClassId = choice.split(" \\(")[0];

                System.out.println(chosenClassId);
                
                InteractTxt.allClass.forEach(c -> {
                    if (chosenClassId.equals(c.getClassId())) {
                        chosenClass = c;
                    }
                });
                
                if (chosenClass == null) {
                    JOptionPane.showMessageDialog(this, "Filter selection failed.\nReport this error.", "Error - Unknown Error", 0);
                } else {
                    
                    studentDropdown.addItem("None");
                    studentDropdown.setSelectedItem("None");
                    
                    chosenClass.Class_Students.forEach(s -> {
                        studentDropdown.addItem(s.getId()+" ("+s.getName()+")");
                    });
                    
                    studentDropdown.setEnabled(true);
                    
                }
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("Error recorded: dropdown referenced without user action. Safe to ignore.");
        }
    }//GEN-LAST:event_classDropdownActionPerformed

    private void studentDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentDropdownActionPerformed
        // TODO add your handling code here:
        try {
            String choice = studentDropdown.getSelectedItem().toString();
            System.out.println(choice);

            if (!InteractTxt.allStudent.contains(InteractTxt.checkStuID(choice.split(" ")[0])) || choice == null || choice.isEmpty() || choice.equals("None")) chosenStudent = null;
            else {
                // valid choice
                System.out.println("------");

                String chosenStuId = choice.split(" \\(")[0];

                System.out.println(chosenStuId);
                
                InteractTxt.allStudent.forEach(s -> {
                    if (chosenStuId.equals(s.getId())) {
                        chosenStudent = s;
                    }
                });
                
                if (chosenStudent == null) {
                    JOptionPane.showMessageDialog(this, "Filter selection failed.\nReport this error.", "Error - Unknown Error", 0);
                } 
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("Error recorded: dropdown referenced without user action. Safe to ignore.");
        }
    }//GEN-LAST:event_studentDropdownActionPerformed

    private void mainTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mainTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_mainTableKeyReleased

    private void mainTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainTableMouseReleased
        // TODO add your handling code here:
        row = mainTable.getSelectedRow();
        System.out.println(row);
        String stuId = String.valueOf(model.getValueAt(row, 0));
        String stuName = String.valueOf(model.getValueAt(row, 1));
        String classId = String.valueOf(model.getValueAt(row, 2));
        String gpa = String.valueOf(model.getValueAt(row, 3));

        System.out.println(stuId+" "+stuName+" "+classId+" "+gpa);
        
        Student clickedStudent = InteractTxt.checkStuID(stuId);
        //+chosenIM
        
        // find student's scores related to this module
        ArrayList<StudentScore> relevantScores = new ArrayList<>();
        ArrayList<String> assIds = new ArrayList<>();
        
        // find amount of assessments for this module
        chosenIM.IM_Assessments.forEach(a ->{
            assIds.add(a.getAssId());
        });
        
        
        for (StudentScore ss : clickedStudent.Stu_Scores) {
            String assId = ss.getAssessment().getAssId();
            if (assIds.contains(assId)) {
                relevantScores.add(ss);
            }
        }
        
        
        ArrayList<StudentScore> newStuScores = new ArrayList<>();
        
        if (chosenIM.IM_Assessments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Assessment format unset for this module and class.\nGo to Design Assessment page.", "Error - Void Action", 0);
        } else if (chosenIM.calcTotalAssPercent() != 100) {
            JOptionPane.showMessageDialog(this, "Cannot add/update score and feedback because assessment format is invalid.\nCombined assessment percentage must be 100%.\nGo to Design Assessment page.", "Error - Invalid Value", 0);
        } 
        
        else {
            //LIKELY correct (not concrete...), if got time use more concrete solution
            
            ArrayList<scoreInputGroup> userInputs = new ArrayList<>();

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(new java.awt.Color(230, 240, 255));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel lblType = new JLabel("Assessment Type");
            lblType.setFont(lblType.getFont().deriveFont(Font.BOLD));
            panel.add(lblType, gbc);

            gbc.gridx = 1;
            JLabel lblName = new JLabel("Assessment Name");
            lblName.setFont(lblName.getFont().deriveFont(Font.BOLD));
            panel.add(lblName, gbc);

            gbc.gridx = 2;
            JLabel lblScore = new JLabel("Score");
            lblScore.setFont(lblScore.getFont().deriveFont(Font.BOLD));
            panel.add(lblScore, gbc);

            gbc.gridx = 3;
            JLabel lblFullScore = new JLabel("Full Score");
            lblFullScore.setFont(lblFullScore.getFont().deriveFont(Font.BOLD));
            panel.add(lblFullScore, gbc);

            gbc.gridx = 4;
            JLabel lblFeedback = new JLabel("Feedback");
            lblFeedback.setFont(lblFeedback.getFont().deriveFont(Font.BOLD));
            panel.add(lblFeedback, gbc);
            
            for (Assessment a : chosenIM.IM_Assessments) {
                String assId = a.getAssId();
                gbc.gridx = 0;
                gbc.weightx = 0.0;
                panel.add(new JLabel(a.getAssType()), gbc);
                
                gbc.gridx = 1;
                gbc.weightx = 0.0;
                JLabel nameLabel = new JLabel("<html><body style='width:125px'>" + a.getAssName() + "</body></html>");
                panel.add(nameLabel, gbc);
                
                JTextField scoreField = new JTextField(10);
                JTextArea feedbackField = new JTextArea(10, 30);
                feedbackField.setLineWrap(true); // Enable wrapping
                feedbackField.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(feedbackField);
                
                scoreField.setText("");
                feedbackField.setText("");
                for (StudentScore ss : relevantScores) {
                    if (ss.getAssessment().getAssId().equals(assId)) {
                        scoreField.setText(ss.getOrginalScore());
                        feedbackField.setText(ss.getFeedback());
                    }
                }
                gbc.gridx = 2;
                gbc.weightx = 0.0;
                panel.add(scoreField, gbc);
                
                String fullMarks = a.getAssFullMarks();
                gbc.gridx = 3;
                gbc.weightx = 0.0;
                panel.add(new JLabel(fullMarks), gbc);
                
                gbc.gridx = 4;
                gbc.weightx = 1.0;
                panel.add(scrollPane, gbc);
                
                userInputs.add(new scoreInputGroup(assId, scoreField, feedbackField));
                System.out.println(scoreField.getText());
                System.out.println(fullMarks);
                System.out.println(a.getAssPercentage());
                // StudentScore(Assessment assessment, String finalScore, String originalScore, String originalFullMarks, String feedback)
                String finalScore = finalScoreCalc(scoreField.getText(), fullMarks, a.getAssPercentage());
                
                System.out.println("HELLO: "+finalScore);
                
                if (!finalScore.contains("Error")) {
                    newStuScores.add(new StudentScore(a, finalScore, scoreField.getText(), fullMarks, feedbackField.getText()));
                }
            }
            
            JScrollPane scrollableContainer = new JScrollPane(panel);
            
            int maxWidth = 1000;
            int maxHeight = 500;
            Dimension maxScrollPaneSize = new Dimension(maxWidth, maxHeight);
            scrollableContainer.setPreferredSize(maxScrollPaneSize);
            
            JTextArea sideTextArea = new JTextArea(20, 25);
            sideTextArea.setLineWrap(true);
            sideTextArea.setWrapStyleWord(true);
            sideTextArea.setEditable(false); // optional
            
            // display comment
            JScrollPane sideScrollPane = new JScrollPane(sideTextArea);
            
            JPanel rightPanel = new JPanel(new BorderLayout());

            JLabel header = new JLabel("Comment");
            header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
            header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // spacing

            rightPanel.add(header, BorderLayout.NORTH);
            rightPanel.add(sideScrollPane, BorderLayout.CENTER);
            
            // create overall display
            JPanel container = new JPanel(new BorderLayout());
            container.add(scrollableContainer, BorderLayout.CENTER);
            container.add(rightPanel, BorderLayout.EAST);
            
            for (StudentGradeAndComment gc : clickedStudent.GradesAndComments) {
                if (gc.getStuClass().getClassId().equals(classId)) {
                    sideTextArea.append(gc.getComment());
                }
            }
            
            //loop until no error (finish filling score)
            while (true) {
                int userInput = JOptionPane.showConfirmDialog(this, container, "Input Student Scores", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
                if (userInput == JOptionPane.OK_OPTION) {
                    try {
                        int userConfirm = JOptionPane.showConfirmDialog(
                                this, 
                                "Save the changes?", 
                                "Confirm Creation", 
                                JOptionPane.YES_NO_OPTION);

                        if (userConfirm == JOptionPane.YES_OPTION) {
                            // input validation time
                            for (scoreInputGroup sig : userInputs) {
                                String scoreInput = sig.getScore().getText();
                                String feedbackInput = sig.getFeedback().getText();
                                for (StudentScore ss : newStuScores) {
                                    if (sig.getAssId().equals(ss.getAssessment().getAssId())) {
                                        String finalScoreInput = finalScoreCalc(scoreInput, ss.getAssessment().getAssFullMarks(), ss.getAssessment().getAssPercentage());
                                        if (finalScoreInput.contains("Range")) { 
                                            throw new IntegerRangeException(ss.getAssessment().getAssName()+" score", "0", ss.getAssessment().getAssFullMarks());
                                        } 
                                        if (finalScoreInput.contains("NFE")) { 
                                            throw new NumberFormatException();
                                        } 
                                        if (finalScoreInput.equals("Error")) {
                                            throw new Exception();
                                        }
                                        if (feedbackInput.length() < Constants.FEEDBACK_MIN_LENGTH || feedbackInput.length() > Constants.FEEDBACK_MAX_LENGTH) {
                                            throw new IntegerRangeException("Feedback length", Constants.FEEDBACK_MIN_LENGTH, Constants.FEEDBACK_MAX_LENGTH);
                                        } 
                                        ss.setOrginalScore(scoreInput);
                                        ss.setFinalScore(finalScoreInput);
                                        ss.setFeedback(feedbackInput);
                                        break;
                                    } 
                                }
                            }

                            // modify InteractTxt arrays to be saved

                            Iterator<StudentScore> scoreIterator = clickedStudent.Stu_Scores.iterator();

                            // loop using the iterator's methods
                            while (scoreIterator.hasNext()) {
                                StudentScore ss = scoreIterator.next();
                                // make sure same assessment id
                                if (assIds.contains(ss.getAssessment().getAssId())) {

                                    // remove the studentscore object
                                    scoreIterator.remove(); 
                                }
                            }

                            // add the new input for the student
                            for (StudentScore ss : newStuScores) {
                                System.out.println(ss.getFinalScore());
                                clickedStudent.Stu_Scores.add(ss);
                            }

                            for (StudentGradeAndComment gc : clickedStudent.GradesAndComments) {
                                if (gc.getStuClass().getClassId().equals(classId)) {
                                    String finalScore = clickedStudent.calcStuScore(chosenIM);
                                    gc.setGrade(clickedStudent.calcStuGrade(finalScore));
                                }
                            }

                            System.out.println("Now: "+classId);
                            System.out.println(clickedStudent.getSpecificGrade(InteractTxt.checkClassID(classId)));
                            
                            searchStudentsButt.doClick();
                            InteractTxt.saveDatabase();
                            break;
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Score value must be numeric.\nDecimal are allowed.", "Error - Invalid Value", 0);
                    } catch (IntegerRangeException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error - Invalid Value", 0);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Assessment edit failed.\nReport this error.", "Error - Unknown Error", 0);
                    }
                } else {
                    break;
                }
            }
                        
            
        }
    }//GEN-LAST:event_mainTableMouseReleased

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new LecturerDashboard(sessionUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backButtonActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        InteractTxt.initDatabase();
        String x = "lc076206";
        InteractTxt.allLecturer.forEach(l -> {
            System.out.println(l.getId());
            if (l.getId().equals(x)) {
                System.out.println(l.getId());
                java.awt.EventQueue.invokeLater(() -> new EnterAssessmentMarks0(l).setVisible(true));
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JComboBox<String> classDropdown;
    private javax.swing.JComboBox<String> intakeDropdown;
    private javax.swing.JLabel intakeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable mainTable;
    private javax.swing.JComboBox<String> moduleDropdown;
    private javax.swing.JLabel moduleLabel;
    private javax.swing.JButton searchStudentsButt;
    private javax.swing.JLabel studentCountLabel;
    private javax.swing.JComboBox<String> studentDropdown;
    private javax.swing.JLabel textForUser;
    // End of variables declaration//GEN-END:variables
}
