/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.leader;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import project.utils.*;
import project.roles.*;
import project.roles.Module;
import project.roles.Class;

/**
 *
 * @author US
 */
public class LeaderAssign extends FrameFormat {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderAssign.class.getName());
    private Leader sessionUser;
    private DefaultTableModel model = new DefaultTableModel();
    private int row = -1;
    private String[] columnName = {"Class ID", "Class Name", "Intake", "Module Name", "Lecturer Name"};
    private JPopupMenu popupMenu;

    public LeaderAssign(Leader sessionUser) {
        initComponents();
        super.formatWindow("Leader Assign Lecturers");
        this.sessionUser = sessionUser;

        model.setColumnIdentifiers(columnName);

        createPopupMenu();

        loadFilterDropdowns();

        loadModuleData();
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit Assignment");
        editItem.addActionListener(e -> {
            if (row != -1) {
                String classId = model.getValueAt(row, 0).toString();
                String className = model.getValueAt(row, 1).toString();
                String moduleName = model.getValueAt(row, 3).toString();
                String lecturerName = model.getValueAt(row, 4).toString();

                // We need to find the moduleId and lecturerId from the data
                // by looking them up based on the displayed information
                String moduleId = findModuleIdByName(moduleName);
                String lecturerId = findLecturerIdByName(lecturerName);

                new LeaderEditAssign(sessionUser, classId, className, moduleId, moduleName, lecturerId, lecturerName).setVisible(true);
                this.dispose();
            }
        });

        JMenuItem deleteItem = new JMenuItem("Remove Lecturer from Class");
        deleteItem.addActionListener(e -> {
            if (row != -1) {
                String classId = model.getValueAt(row, 0).toString();
                String className = model.getValueAt(row, 1).toString();
                String lecturerName = model.getValueAt(row, 4).toString();

                // Find the lecturerId from the lecturer name
                String lecturerId = findLecturerIdByName(lecturerName);

                int confirm = JOptionPane.showConfirmDialog(
                        this, "Remove " + lecturerName + " from " + className + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Class classObj = InteractTxt.checkClassID(classId);
                        Lecturer lecturer = InteractTxt.checkLecID(lecturerId);

                        if (classObj != null && lecturer != null) {
                            // Remove lecturer from class
                            classObj.setLecId("NA");

                            // Remove class from lecturer
                            lecturer.Lec_Classes.remove(classObj);

                            InteractTxt.saveDatabase();

                            JOptionPane.showMessageDialog(
                                    this, "Lecturer removed from class successfully!", "Success", JOptionPane.INFORMATION_MESSAGE
                            );

                            loadModuleData();

                        } else {
                            JOptionPane.showMessageDialog(
                                    this, "Class or Lecturer not found!", "Error", JOptionPane.ERROR_MESSAGE
                            );
                        }

                    } catch (Exception ex) {
                        logger.log(java.util.logging.Level.SEVERE, "Error removing lecturer", ex);
                        JOptionPane.showMessageDialog(
                                this, "Error removing lecturer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
    }

    // Helper method to find module ID by module name
    private String findModuleIdByName(String moduleName) {
        for (Module module : sessionUser.Lea_Modules) {
            if (module.getModuleName().equals(moduleName)) {
                return module.getModuleId();
            }
        }
        return null;
    }

    // Helper method to find lecturer ID by lecturer name
    private String findLecturerIdByName(String lecturerName) {
        if (lecturerName.equals("Not Assigned")) {
            return "NA";
        }
        for (Lecturer lecturer : sessionUser.leaderTeam) {
            if (lecturer.getName().equals(lecturerName)) {
                return lecturer.getId();
            }
        }
        return "NA";
    }

    private void loadFilterDropdowns() {

        moduleFilter.removeAllItems();
        moduleFilter.addItem("-- All Modules --");
        for (Module module : sessionUser.Lea_Modules) {
            moduleFilter.addItem(module.getModuleId() + " - " + module.getModuleName());
        }

        lecturerFilter.removeAllItems();
        lecturerFilter.addItem("-- All Lecturers --");
        for (Lecturer lecturer : sessionUser.leaderTeam) {
            lecturerFilter.addItem(lecturer.getId() + " - " + lecturer.getName());
        }

        intakeFilter.removeAllItems();
        intakeFilter.addItem("-- All Intakes --");
        for (Intake intake : InteractTxt.allIntake) {
            intakeFilter.addItem(intake.getIntakeId() + " - " + intake.getIntakeName());
        }
    }

    public void loadModuleData() {
        model.setRowCount(0);

        // Get selected filters
        String selectedModule = (String) moduleFilter.getSelectedItem();
        String selectedLecturer = (String) lecturerFilter.getSelectedItem();
        String selectedIntake = (String) intakeFilter.getSelectedItem();

        // Extract IDs from selections (if not "All")
        String filterModuleId = null;
        String filterLecturerId = null;
        String filterIntakeId = null;

        if (selectedModule != null && !selectedModule.startsWith("--")) {
            filterModuleId = selectedModule.split(" - ")[0];
        }

        if (selectedLecturer != null && !selectedLecturer.startsWith("--")) {
            filterLecturerId = selectedLecturer.split(" - ")[0];
        }

        if (selectedIntake != null && !selectedIntake.startsWith("--")) {
            filterIntakeId = selectedIntake.split(" - ")[0];
        }

        // Iterate through all IntakeModules that belong to this leader's modules
        for (IntakeModule im : InteractTxt.allIntakeModule) {
            Module module = InteractTxt.checkModID(im.getModuleId());

            // Only show modules created by this leader
            if (module == null || module.getLeader() == null
                    || !module.getLeader().getId().equals(sessionUser.getId())) {
                continue;
            }

            // Apply module filter
            if (filterModuleId != null && !module.getModuleId().equals(filterModuleId)) {
                continue;
            }

            // Apply intake filter
            if (filterIntakeId != null && !im.getIntakeId().equals(filterIntakeId)) {
                continue;
            }

            Intake intake = InteractTxt.checkIntID(im.getIntakeId());
            String intakeName = intake != null ? intake.getIntakeName() : im.getIntakeId();

            // Iterate through all classes in this IntakeModule
            for (Class classObj : im.IM_Classes) {
                String lecturerId = classObj.getLecId();
                String lecturerName = "Not Assigned";

                if (lecturerId != null && !lecturerId.equals("NA")) {
                    Lecturer lecturer = InteractTxt.checkLecID(lecturerId);

                    if (lecturer != null) {

                        if (!sessionUser.leaderTeam.contains(lecturer)) {
                            continue;
                        }

                        if (filterLecturerId != null && !lecturer.getId().equals(filterLecturerId)) {
                            continue;
                        }

                        lecturerName = lecturer.getName();
                    } else {
                        lecturerId = "NA";
                    }
                } else {
                    lecturerId = "NA";

                    if (filterLecturerId != null) {
                        continue;
                    }
                }

                Object[] rowData = {
                    classObj.getClassId(),
                    classObj.getClassName(),
                    intakeName,
                    module.getModuleName(),
                    lecturerName
                };
                model.addRow(rowData);
            }
        }

        System.out.println("Total assignments loaded: " + model.getRowCount());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        moduleFilter = new javax.swing.JComboBox<>();
        intakeFilter = new javax.swing.JComboBox<>();
        lecturerFilter = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(model);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton4.setText("Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        moduleFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        moduleFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduleFilterActionPerformed(evt);
            }
        });

        intakeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        intakeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intakeFilterActionPerformed(evt);
            }
        });

        lecturerFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lecturerFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lecturerFilterActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Manage Class Assignment to Lecturer");

        jLabel3.setText("Click a Row to Edit Or Delete Class Assignment");

        jLabel4.setText("Apply Filters");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(intakeFilter, javax.swing.GroupLayout.Alignment.LEADING, 0, 408, Short.MAX_VALUE)
                                        .addComponent(moduleFilter, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lecturerFilter, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1057, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 18, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
            .addGroup(layout.createSequentialGroup()
                .addGap(412, 412, 412)
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lecturerFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(moduleFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(intakeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        row = jTable1.rowAtPoint(evt.getPoint());

        if (row != -1) {
            jTable1.setRowSelectionInterval(row, row);

            // Show popup menu on right-click or regular click
            if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3 || evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                popupMenu.show(jTable1, evt.getX(), evt.getY());
            }
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new LeaderDashboard(sessionUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void moduleFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleFilterActionPerformed
        // TODO add your handling code here:
        loadModuleData();
    }//GEN-LAST:event_moduleFilterActionPerformed

    private void intakeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intakeFilterActionPerformed
        // TODO add your handling code here:
        loadModuleData();
    }//GEN-LAST:event_intakeFilterActionPerformed

    private void lecturerFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lecturerFilterActionPerformed
        // TODO add your handling code here:
        loadModuleData();
    }//GEN-LAST:event_lecturerFilterActionPerformed

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
            java.util.logging.Logger.getLogger(LeaderAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaderAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaderAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaderAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                    new LeaderAssign().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> intakeFilter;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> lecturerFilter;
    private javax.swing.JComboBox<String> moduleFilter;
    // End of variables declaration//GEN-END:variables
}
