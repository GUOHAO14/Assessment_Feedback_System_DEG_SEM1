/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.leader;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.utils.*;
import project.roles.*;
import project.roles.Module;
import project.roles.Class;

/**
 *
 * @author US
 */
public class LeaderEditAssign extends FrameFormat {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderEditAssign.class.getName());
    private Leader sessionUser;
    private String originalClassId;
    private String className;
    private String currentModuleId;
    private String currentLecturerId;

    public LeaderEditAssign(Leader sessionUser, String classId, String className, String moduleId, String moduleName, String lecturerId, String lecturerName) {
        initComponents();
        this.sessionUser = sessionUser;
        this.originalClassId = classId;
        this.className = className;
        this.currentModuleId = moduleId;
        this.currentLecturerId = lecturerId;

        super.formatWindow("Edit Lecturer Assignment");

        moduleCombo.setEnabled(false);
        intakeModuleCombo.setEnabled(false);
        classCombo.setEnabled(false);
        lecturerCombo.setEnabled(true);

        moduleCombo.removeAllItems();
        moduleCombo.addItem(moduleId + " - " + moduleName);

        // Display current intake-module
        Class classObj = InteractTxt.checkClassID(classId);
        if (classObj != null) {
            String imid = classObj.getIMID();
            IntakeModule im = InteractTxt.checkIMID(imid);
            if (im != null) {
                Intake intake = InteractTxt.checkIntID(im.getIntakeId());
                String imDisplay = im.getIMID() + " - "
                        + (intake != null ? intake.getIntakeName() : im.getIntakeId());
                intakeModuleCombo.removeAllItems();
                intakeModuleCombo.addItem(imDisplay);
            }
        }

        // Display current class
        classCombo.removeAllItems();
        classCombo.addItem(classId + " - " + className);

        // Load lecturers for this module (editable)
        loadLecturerComboBox(moduleId);

        // Set current lecturer
        if (lecturerId != null && !lecturerId.equals("NA")) {
            lecturerCombo.setSelectedItem(lecturerId + " - " + lecturerName);
        }
    }

    private void loadLecturerComboBox(String moduleId) {
        lecturerCombo.removeAllItems();
        lecturerCombo.addItem("-- Select Lecturer --");

        Module module = InteractTxt.checkModID(moduleId);

        if (module == null) {
            lecturerCombo.addItem("Module not found");
            return;
        }

        if (module.Mod_Lecturers.isEmpty()) {
            lecturerCombo.addItem("No lecturers assigned to this module");
            return;
        }

        // Load only lecturers assigned to this module
        for (Lecturer lecturer : module.Mod_Lecturers) {
            lecturerCombo.addItem(lecturer.getId() + " - " + lecturer.getName());
        }
    }

    private void updateClassAssignment() {
        // Validate lecturer selection - MUST select a lecturer
        String lecturerSelection = (String) lecturerCombo.getSelectedItem();
        if (lecturerSelection == null || lecturerSelection.startsWith("--")
                || lecturerSelection.equals("No lecturers assigned to this module")
                || lecturerSelection.equals("Module not found")) {
            showError("Please select a Lecturer!\nTo remove a lecturer, use the 'Remove' option in the main table.");
            return;
        }

        String lecturerId = lecturerSelection.split(" - ")[0];

        // Get module
        Module module = InteractTxt.checkModID(currentModuleId);
        if (module == null) {
            showError("Module not found!");
            return;
        }

        // Get current class object
        Class classObj = InteractTxt.checkClassID(this.originalClassId);
        if (classObj == null) {
            showError("Class not found!");
            return;
        }

        // Get new lecturer
        Lecturer newLecturer = InteractTxt.checkLecID(lecturerId);
        if (newLecturer == null) {
            showError("Lecturer not found!");
            return;
        }

        if (!module.Mod_Lecturers.contains(newLecturer)) {
            showError("This lecturer is not assigned to the selected module!\n"
                    + "Please assign the lecturer to the module first.");
            return;
        }

        // Remove old lecturer from class (if exists)
        if (currentLecturerId != null && !currentLecturerId.equals("NA")) {
            Lecturer oldLecturer = InteractTxt.checkLecID(currentLecturerId);
            if (oldLecturer != null) {
                oldLecturer.Lec_Classes.remove(classObj);
            }
        }

        // Check if class already has a different lecturer
        if (classObj.getLecId() != null && !classObj.getLecId().equals("NA")
                && !classObj.getLecId().equals(lecturerId)) {
            Lecturer existingLecturer = InteractTxt.checkLecID(classObj.getLecId());
            String existingName = existingLecturer != null ? existingLecturer.getName() : classObj.getLecId();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "This class is already assigned to " + existingName + ".\n"
                    + "Do you want to reassign it to " + newLecturer.getName() + "?",
                    "Confirm Reassignment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Remove class from existing lecturer
            if (existingLecturer != null) {
                existingLecturer.Lec_Classes.remove(classObj);
            }
        }

        // Assign lecturer to module (if not already assigned)
        if (!newLecturer.Lec_Modules.contains(module)) {
            newLecturer.Lec_Modules.add(module);
        }
        if (!module.Mod_Lecturers.contains(newLecturer)) {
            module.Mod_Lecturers.add(newLecturer);
        }

        // Assign lecturer to class
        classObj.setLecId(newLecturer.getId());
        if (!newLecturer.Lec_Classes.contains(classObj)) {
            newLecturer.Lec_Classes.add(classObj);
        }

        InteractTxt.saveDatabase();

        showSuccess("Assignment updated successfully!\n"
                + "Module: " + module.getModuleName() + "\n"
                + "Class: " + classObj.getClassName() + "\n"
                + "Lecturer: " + newLecturer.getName());

        new LeaderAssign(sessionUser).setVisible(true);
        this.dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lecturerCombo = new javax.swing.JComboBox<>();
        moduleCombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        intakeModuleCombo = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        classCombo = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lecturerCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lecturerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lecturerComboActionPerformed(evt);
            }
        });

        moduleCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        moduleCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduleComboActionPerformed(evt);
            }
        });

        jLabel4.setText("Intake :  ");

        jLabel3.setText("Lecturer :");

        intakeModuleCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        intakeModuleCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intakeModuleComboActionPerformed(evt);
            }
        });

        jLabel1.setText("Module :");

        jLabel2.setText("Class : ");

        classCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        classCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classComboActionPerformed(evt);
            }
        });

        jButton1.setText("Save");
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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Edit Class Assignment Details");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(classCombo, 0, 385, Short.MAX_VALUE)
                                    .addComponent(intakeModuleCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(moduleCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(16, 16, 16)
                                    .addComponent(lecturerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jButton2)
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(moduleCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(intakeModuleCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(classCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lecturerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(45, 45, 45)
                .addComponent(jButton1)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lecturerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lecturerComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lecturerComboActionPerformed

    private void moduleComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleComboActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_moduleComboActionPerformed

    private void intakeModuleComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intakeModuleComboActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_intakeModuleComboActionPerformed

    private void classComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_classComboActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        updateClassAssignment();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new LeaderAssign(sessionUser).setVisible(true);
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
            java.util.logging.Logger.getLogger(LeaderEditAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaderEditAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaderEditAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaderEditAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new LeaderEditAssign().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> classCombo;
    private javax.swing.JComboBox<String> intakeModuleCombo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox<String> lecturerCombo;
    private javax.swing.JComboBox<String> moduleCombo;
    // End of variables declaration//GEN-END:variables
}
