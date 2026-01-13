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
public class LeaderEditModule extends FrameFormat {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderEditModule.class.getName());
    private Leader sessionUser;
    private DefaultTableModel model = new DefaultTableModel();
    private String[] columnName = {"Module ID", "Module Name", "Intake ID", "Intake Name", "IMID"};
    private int selectedRow = -1;
    
    /**
     * Creates new form LeaderEditModule
     */
    public LeaderEditModule(Leader sessionUser) {
        initComponents();
        this.sessionUser = sessionUser;
        super.formatWindow("Edit Module");
        
        model.setColumnIdentifiers(columnName);
        
        
        loadComboBoxes();
        loadModuleData();
    }
    
    private void loadComboBoxes() {
        // Load Intakes
        intakeComboBox.removeAllItems();
        intakeComboBox.addItem("-- Select Intake --");
        for (Intake intake : InteractTxt.allIntake) {
            intakeComboBox.addItem(intake.getIntakeId() + " - " + intake.getIntakeName());
        }
        
        // Load Lecturers
        lecturerComboBox.removeAllItems();
        lecturerComboBox.addItem("-- No Change --");
        for (Lecturer lecturer : InteractTxt.allLecturer) {
            lecturerComboBox.addItem(lecturer.getId() + " - " + lecturer.getName());
        }
    }
    
    private void loadModuleData() {
        model.setRowCount(0);
        
        for (IntakeModule im : InteractTxt.allIntakeModule) {
            Module module = InteractTxt.checkModID(im.getModuleId());
            Intake intake = InteractTxt.checkIntID(im.getIntakeId());
            
            if (module != null) {
                String moduleName = module.getModuleName();
                String intakeId = im.getIntakeId();
                String intakeName = (intake != null) ? intake.getIntakeName() : "Unknown";
                
                Object[] row = {
                    module.getModuleId(),
                    moduleName,
                    intakeId,
                    intakeName,
                    im.getIMID()
                };
                model.addRow(row);
            }
        }
    }
    
    private void populateFields() {
        if (selectedRow != -1) {
            String moduleId = model.getValueAt(selectedRow, 0).toString();
            String moduleName = model.getValueAt(selectedRow, 1).toString();
            String intakeId = model.getValueAt(selectedRow, 2).toString();
            String intakeName = model.getValueAt(selectedRow, 3).toString();
            
            moduleIdField.setText(moduleId);
            moduleNameField.setText(moduleName);
            intakeComboBox.setSelectedItem(intakeId + " - " + intakeName);
            
            statusLabel.setText("Editing: " + moduleId);
//            statusLabel.setForeground(Color.BLUE);
        }
    }
    
    private void updateModuleData() {
        if (selectedRow == -1) {
            showError("Please select a module to edit!");
            return;
        }
        
        String moduleId = moduleIdField.getText().trim();
        String newModuleName = moduleNameField.getText().trim();
        String oldIntakeId = model.getValueAt(selectedRow, 2).toString();
        String imId = model.getValueAt(selectedRow, 4).toString();
        
        // Validation
        if (newModuleName.isEmpty()) {
            showError("Module name cannot be empty!");
            return;
        }
        
        // Get new intake
        String intakeSelection = (String) intakeComboBox.getSelectedItem();
        if (intakeSelection == null || intakeSelection.startsWith("--")) {
            showError("Please select an intake!");
            return;
        }
        String newIntakeId = intakeSelection.split(" - ")[0];
        
        // Get lecturer selection (optional)
        String lecturerSelection = (String) lecturerComboBox.getSelectedItem();
        String lecturerId = null;
        if (lecturerSelection != null && !lecturerSelection.startsWith("--")) {
            lecturerId = lecturerSelection.split(" - ")[0];
        }
        
        // Find objects
        Module module = InteractTxt.checkModID(moduleId);
        if (module == null) {
            showError("Module not found!");
            return;
        }
        
        // Update module name
        module.setModuleName(newModuleName);
        
        // Update intake assignment if changed
        if (!newIntakeId.equals(oldIntakeId)) {
            IntakeModule im = InteractTxt.checkIMID(imId);
            if (im != null) {
                // Remove from old intake
                Intake oldIntake = InteractTxt.checkIntID(oldIntakeId);
                if (oldIntake != null) {
                    oldIntake.Int_Modules.remove(module);
                }
                
                // Update to new intake
                im.setIntakeId(newIntakeId);
                
                // Add to new intake
                Intake newIntake = InteractTxt.checkIntID(newIntakeId);
                if (newIntake != null && !newIntake.Int_Modules.contains(module)) {
                    newIntake.Int_Modules.add(module);
                }
            }
        }
        
        // Update lecturer if selected
        if (lecturerId != null) {
            Lecturer lecturer = InteractTxt.checkLecID(lecturerId);
            if (lecturer != null) {
                // Add module to lecturer
                if (!lecturer.Lec_Modules.contains(module)) {
                    lecturer.Lec_Modules.add(module);
                }
                // Add lecturer to module
                if (!module.Mod_Lecturers.contains(lecturer)) {
                    module.Mod_Lecturers.add(lecturer);
                }
            }
        }
        
        // Save to database
//        InteractTxt.saveDatabase();
        
        // Refresh
        loadModuleData();
        clearFields();
        
        showSuccess("Module updated successfully!");
    }
    
    private void clearFields() {
        moduleIdField.setText("");
        moduleNameField.setText("");
        intakeComboBox.setSelectedIndex(0);
        lecturerComboBox.setSelectedIndex(0);
        jTable3.clearSelection(); //attention jtable3
        selectedRow = -1;
        statusLabel.setText("Select a module from the table to edit");
//        statusLabel.setForeground(Color.BLACK);
    }
    
    private void backToDashboard() {
        new LeaderDashboard(sessionUser).setVisible(true);
        this.dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Error: " + message);
//        statusLabel.setForeground(Color.RED);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(message);
//        statusLabel.setForeground(new Color(0, 128, 0));
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lecturerComboBox = new javax.swing.JComboBox<>();
        moduleNameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        intakeComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        moduleIdField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();

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

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("Lecturer");

        lecturerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lecturerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lecturerComboBoxActionPerformed(evt);
            }
        });

        moduleNameField.setText("jTextField1");
        moduleNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduleNameFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Module Name");

        jLabel2.setText("Intake Name");

        intakeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        intakeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intakeComboBoxActionPerformed(evt);
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

        jTable3.setModel(model);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel4.setText("Module ID");

        moduleIdField.setText("jTextField1");

        statusLabel.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(moduleNameField)
                            .addComponent(intakeComboBox, 0, 95, Short.MAX_VALUE)
                            .addComponent(lecturerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(moduleIdField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(moduleIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(moduleNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(intakeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lecturerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1))))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lecturerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lecturerComboBoxActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_lecturerComboBoxActionPerformed

    private void moduleNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moduleNameFieldActionPerformed

    private void intakeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intakeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_intakeComboBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        updateModuleData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        backToDashboard();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
            selectedRow = jTable3.getSelectedRow(); //attention jtable3 not model
                if (selectedRow != -1) {
                    populateFields();
                }
    }//GEN-LAST:event_jTable3MouseClicked

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
            java.util.logging.Logger.getLogger(LeaderEditModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaderEditModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaderEditModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaderEditModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new LeaderEditModule().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> intakeComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JComboBox<String> lecturerComboBox;
    private javax.swing.JTextField moduleIdField;
    private javax.swing.JTextField moduleNameField;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
