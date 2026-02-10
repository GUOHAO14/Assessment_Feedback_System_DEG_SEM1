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

/**
 *
 * @author US
 */
public class LeaderEditModule extends FrameFormat {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderEditModule.class.getName());
    private Leader sessionUser;
    private DefaultListModel<String> lecturerListModel;

    public LeaderEditModule(Leader sessionUser, String moduleId, String moduleName) {
        initComponents();
        this.sessionUser = sessionUser;

        super.formatWindow("Edit Module");

        // Initialize lecturer list model
        lecturerListModel = new DefaultListModel<>();
        jList1.setModel(lecturerListModel);

        moduleIdField.setText(moduleId);
        moduleIdField.setEditable(false);
        moduleNameField.setText(moduleName);

      
        loadLecturerList();
        preselectAssignedLecturers(moduleId);

    }

    private void loadLecturerList() {
        lecturerListModel.clear();

        if (sessionUser.leaderTeam.isEmpty()) {
            lecturerListModel.addElement("No lecturers available");
            return;
        }

        for (Lecturer lecturer : sessionUser.leaderTeam) {
            lecturerListModel.addElement(lecturer.getId() + " - " + lecturer.getName());
        }
    }

//    Pre-select lecturers already assigned to this module
    private void preselectAssignedLecturers(String moduleId) {
        Module module = InteractTxt.checkModID(moduleId);
        if (module == null || module.Mod_Lecturers.isEmpty()) {
            return;
        }

        int[] selectedIndices = new int[module.Mod_Lecturers.size()];
        int count = 0;

        for (Lecturer lecturer : module.Mod_Lecturers) {
            String lecturerItem = lecturer.getId() + " - " + lecturer.getName();
            for (int i = 0; i < lecturerListModel.getSize(); i++) {
                if (lecturerListModel.get(i).equals(lecturerItem)) {
                    selectedIndices[count++] = i;
                    break;
                }
            }
        }

        // Set selected indices
        if (count > 0) {
            int[] finalIndices = new int[count];
            System.arraycopy(selectedIndices, 0, finalIndices, 0, count);
            jList1.setSelectedIndices(finalIndices);
        }
    }

    private void updateModuleData() {
        String moduleId = moduleIdField.getText().trim();
        String newModuleName = moduleNameField.getText().trim();

        if (moduleId.isEmpty()) {
            showError("Module ID is missing!");
            return;
        }

        if (newModuleName.isEmpty()) {
            showError("Module name cannot be empty!");
            return;
        }

        // Find module object
        Module module = InteractTxt.checkModID(moduleId);
        if (module == null) {
            showError("Module not found!");
            return;
        }

        // Check for duplicate module names (excluding the current module being edited)
        for (Module existingModule : InteractTxt.allModule) {
            // Skip the current module being edited
            if (existingModule.getModuleId().equals(moduleId)) {
                continue;
            }

            if (existingModule.getModuleName().equalsIgnoreCase(newModuleName)) {
                JOptionPane.showMessageDialog(this,
                        "A module with this name already exists: " + existingModule.getModuleId(),
                        "Module Exists",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Update module name
        module.setModuleName(newModuleName);

        // Update lecturer assignments
        updateLecturerAssignments(module);

        InteractTxt.saveDatabase();

        showSuccess("Module updated successfully!");

        new LeaderModule(sessionUser).setVisible(true);
        this.dispose();
    }

    private void updateLecturerAssignments(Module module) {
   
        List<String> selectedLecturers = jList1.getSelectedValuesList();

        // Clear existing assignments
        for (Lecturer lecturer : module.Mod_Lecturers) {
            lecturer.Lec_Modules.remove(module);
        }
        module.Mod_Lecturers.clear();

        // Add new assignments
        if (!selectedLecturers.isEmpty()) {
            for (String lecturerSelection : selectedLecturers) {
                if (!lecturerSelection.equals("No lecturers available")) {
                    String lecturerId = lecturerSelection.split(" - ")[0];
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
            }
        }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        moduleIdField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        moduleNameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

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

        moduleIdField.setText("jTextField1");

        jLabel2.setText("Assigned Lecturer :");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel3.setText("Hold Ctrl + Left Click to Select more Lecturer");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Edit Module");

        moduleNameField.setText("jTextField1");
        moduleNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduleNameFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Module Name :");

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

        jLabel4.setText("Module ID :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(47, 47, 47)
                                .addComponent(moduleIdField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(26, 26, 26)
                                .addComponent(moduleNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(295, 295, 295))
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jLabel3)
                        .addGap(52, 52, 52)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jButton2)
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(moduleIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(moduleNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moduleNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moduleNameFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        updateModuleData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new LeaderModule(sessionUser).setVisible(true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField moduleIdField;
    private javax.swing.JTextField moduleNameField;
    // End of variables declaration//GEN-END:variables
}
