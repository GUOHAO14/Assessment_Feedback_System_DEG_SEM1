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

/**
 *
 * @author US
 */
public class LeaderAddModule extends FrameFormat {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderAddModule.class.getName());
    private Leader sessionUser;
    /**
     * Creates new form LeaderAddModule
     */
    public LeaderAddModule(Leader sessionUser) {
        initComponents();
        super.formatWindow("Leader Add Module");
        this.sessionUser = sessionUser;
        this.sessionUser.printFullLeaderData();
        // Load dropdown data
        loadIntakeComboBox();
        loadLecturerComboBox();
        
        // Clear default text
        modField.setText("");
    }
    
//    private void loadIntakeComboBox() {
//        intakeComboBox.removeAllItems();
//        intakeComboBox.addItem("-- Select Intake (Optional) --");
//        
//        for (Intake intake : InteractTxt.allIntake) {
//            intakeComboBox.addItem(intake.getIntakeId() + " - " + intake.getIntakeName());
//        }
    
//    private void saveModuleData(){
//        String moduleName = modField.getText();
//        if (moduleName.isEmpty()) {
//            JOptionPane.showMessageDialog(this,"Please fill in module name!","Validation Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//    }
//    }
    
    /**
     * Load all intakes into combo box
     */
    private void loadIntakeComboBox() {
        intakeCombo.removeAllItems();
        intakeCombo.addItem("-- Select Intake --");
        
        for (Intake intake : InteractTxt.allIntake) {
            intakeCombo.addItem(intake.getIntakeId() + " - " + intake.getIntakeName());
        }
    }
    
    /**
     * Load all lecturers into combo box
     */
    private void loadLecturerComboBox() {
        lecturerCombo.removeAllItems();
        lecturerCombo.addItem("-- Select Lecturer (Optional) --");
        
        for (Lecturer lecturer : InteractTxt.allLecturer) {
            lecturerCombo.addItem(lecturer.getId() + " - " + lecturer.getName());
        }
    }
    
    /**
     * Save/Add new module with intake and lecturer assignment
     */
    private void saveModuleData() {
        // Get module name
        String moduleName = modField.getText().trim();
        
        // Validation: Module name
        if (moduleName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in module name!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected intake
        String intakeSelection = (String) intakeCombo.getSelectedItem();
        if (intakeSelection == null || intakeSelection.startsWith("--")) {
            JOptionPane.showMessageDialog(this, 
                "Please select an Intake!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        String intakeId = intakeSelection.split(" - ")[0];
        
        // Get selected lecturer (optional)
        String lecturerSelection = (String) lecturerCombo.getSelectedItem();
        String lecturerId = null;
        if (lecturerSelection != null && !lecturerSelection.startsWith("--")) {
            lecturerId = lecturerSelection.split(" - ")[0];
        }
        
        // Generate Module ID (auto-generate based on existing modules)
        String moduleId = generateModuleId();
        
        // Check if module with same name already exists
        for (Module existingModule : InteractTxt.allModule) {
            if (existingModule.getModuleName().equalsIgnoreCase(moduleName)) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "A module with this name already exists: " + existingModule.getModuleId() + "\n" +
                    "Do you want to assign it to the selected intake instead?",
                    "Module Exists",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (choice == JOptionPane.YES_OPTION) {
                    // Use existing module
                    assignExistingModule(existingModule, intakeId, lecturerId);
                    return;
                } else {
                    return; // User cancelled
                }
            }
        }
        
        // Create new module
        Module newModule = new Module(moduleId, moduleName);
        InteractTxt.allModule.add(newModule);
        
        // Find intake and module objects
        Intake intake = InteractTxt.checkIntID(intakeId);
        
        if (intake == null) {
            JOptionPane.showMessageDialog(this, 
                "Intake not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create IntakeModule assignment
        String imId = generateIMID();
        IntakeModule newIM = new IntakeModule(imId, intakeId, moduleId);
        InteractTxt.allIntakeModule.add(newIM);
        
        // Add module to intake's module list
        if (!intake.Int_Modules.contains(newModule)) {
            intake.Int_Modules.add(newModule);
        }
        
        // Assign lecturer if selected
        if (lecturerId != null) {
            Lecturer lecturer = InteractTxt.checkLecID(lecturerId);
            if (lecturer != null) {
                // Add module to lecturer's module list
                if (!lecturer.Lec_Modules.contains(newModule)) {
                    lecturer.Lec_Modules.add(newModule);
                }
                
                // Add lecturer to module's lecturer list
                if (!newModule.Mod_Lecturers.contains(lecturer)) {
                    newModule.Mod_Lecturers.add(lecturer);
                }
            }
        }
        
        // Save to database
//        InteractTxt.saveDatabase();
        
        // Success message
        String successMsg = "Module added successfully!\n" +
                          "Module ID: " + moduleId + "\n" +
                          "Module Name: " + moduleName + "\n" +
                          "Assigned to: " + intake.getIntakeName();
        
        if (lecturerId != null) {
            Lecturer lecturer = InteractTxt.checkLecID(lecturerId);
            successMsg += "\nLecturer: " + lecturer.getName();
        }
        
        JOptionPane.showMessageDialog(this, 
            successMsg, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Clear form
        clearForm();
    }
    
    /**
     * Assign existing module to intake and lecturer
     */
    private void assignExistingModule(Module module, String intakeId, String lecturerId) {
        Intake intake = InteractTxt.checkIntID(intakeId);
        
        if (intake == null) {
            JOptionPane.showMessageDialog(this, "Intake not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if already assigned to this intake
        if (InteractTxt.checkIMID(intakeId, module.getModuleId()) != null) {
            JOptionPane.showMessageDialog(this, 
                "This module is already assigned to this intake!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
        } else {
            // Create new IntakeModule assignment
            String imId = generateIMID();
            IntakeModule newIM = new IntakeModule(imId, intakeId, module.getModuleId());
            InteractTxt.allIntakeModule.add(newIM);
            
            // Add to intake's module list
            if (!intake.Int_Modules.contains(module)) {
                intake.Int_Modules.add(module);
            }
        }
        
        // Assign lecturer if selected
        if (lecturerId != null) {
            Lecturer lecturer = InteractTxt.checkLecID(lecturerId);
            if (lecturer != null) {
                if (!lecturer.Lec_Modules.contains(module)) {
                    lecturer.Lec_Modules.add(module);
                }
                if (!module.Mod_Lecturers.contains(lecturer)) {
                    module.Mod_Lecturers.add(lecturer);
                }
            }
        }
        
        // Save
//        InteractTxt.saveDatabase();
        
        JOptionPane.showMessageDialog(this, 
            "Existing module assigned successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        clearForm();
    }
    
    /**
     * Generate new Module ID
     */
    private String generateModuleId() {
        int maxId = 0;
        
        for (Module module : InteractTxt.allModule) {
            String moduleId = module.getModuleId();
            // Assuming format: MOD001, MOD002, etc.
            if (moduleId.startsWith("MOD")) {
                try {
                    int num = Integer.parseInt(moduleId.substring(3));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid IDs
                }
            }
        }
        
        return String.format("MOD%03d", maxId + 1);
    }
    
    /**
     * Generate new IM ID
     */
    private String generateIMID() {
        int maxId = 0;
        
        for (IntakeModule im : InteractTxt.allIntakeModule) {
            String imId = im.getIMID();
            if (imId.startsWith("IM")) {
                try {
                    int num = Integer.parseInt(imId.substring(2));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip
                }
            }
        }
        
        return String.format("IM%03d", maxId + 1);
    }
    
    /**
     * Clear form fields
     */
    private void clearForm() {
        modField.setText("");
        intakeCombo.setSelectedIndex(0);
        lecturerCombo.setSelectedIndex(0);
    }
    
    /**
     * Back to dashboard
     */
    private void backToDashboard() {
        new LeaderDashboard(sessionUser).setVisible(true);
        this.dispose();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        lecturerCombo = new javax.swing.JComboBox<>();
        modField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        intakeCombo = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("Lecturer");

        lecturerCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lecturerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lecturerComboActionPerformed(evt);
            }
        });

        modField.setText("jTextField1");
        modField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Module Name");

        jLabel2.setText("Intake Name");

        intakeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        intakeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intakeComboActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(18, 18, 18))
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(modField)
                    .addComponent(intakeCombo, 0, 95, Short.MAX_VALUE)
                    .addComponent(lecturerCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(modField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intakeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lecturerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modFieldActionPerformed

    private void intakeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intakeComboActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_intakeComboActionPerformed

    private void lecturerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lecturerComboActionPerformed
        // TODO add your handling code here:
    
    }//GEN-LAST:event_lecturerComboActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        saveModuleData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        backToDashboard();
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
            java.util.logging.Logger.getLogger(LeaderAddModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaderAddModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaderAddModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaderAddModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new LeaderAddModule().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> intakeCombo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox<String> lecturerCombo;
    private javax.swing.JTextField modField;
    // End of variables declaration//GEN-END:variables
}
