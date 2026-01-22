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
public class LeaderAddModule extends FrameFormat {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderAddModule.class.getName());
    private Leader sessionUser;
    private DefaultListModel<String> lecturerListModel;

    public LeaderAddModule(Leader sessionUser) {
        initComponents();
        super.formatWindow("Leader Add Module");
        this.sessionUser = sessionUser;

        // Initialize lecturer list model
        lecturerListModel = new DefaultListModel<>();
        jList1.setModel(lecturerListModel);

        modField.setText("");
        loadLecturerList();
    }

    /**
     * Load lecturers into JList for multiple selection
     */
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

    private void saveModuleData() {
        String moduleName = modField.getText().trim();

        if (moduleName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in module name!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for existing module with same name
        for (Module existingModule : InteractTxt.allModule) {
            if (existingModule.getModuleName().equalsIgnoreCase(moduleName)) {
                JOptionPane.showMessageDialog(this,
                        "A module with this name already exists: " + existingModule.getModuleId(),
                        "Module Exists",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Generate Module ID
        String moduleId = generateModuleId();

        // Create new module with Leader object
        Module newModule = new Module(moduleId, moduleName, sessionUser);
        InteractTxt.allModule.add(newModule);

        // Add module to leader's module list
        sessionUser.Lea_Modules.add(newModule);

        // Get selected lecturers and assign them to the module
        List<String> selectedLecturers = jList1.getSelectedValuesList();
        int assignedCount = 0;

        if (!selectedLecturers.isEmpty()) {
            for (String lecturerSelection : selectedLecturers) {
                if (!lecturerSelection.equals("No lecturers available")) {
                    String lecturerId = lecturerSelection.split(" - ")[0];
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
                        assignedCount++;
                    }
                }
            }
        }

        InteractTxt.saveDatabase();

        String lecturerInfo = "";
        if (assignedCount > 0) {
            lecturerInfo = "\nLecturers assigned: " + assignedCount;
        }

        JOptionPane.showMessageDialog(this,
                "Module added successfully!\nID: " + moduleId
                + "\nName: " + moduleName + lecturerInfo,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        clearForm();
    }

    private String generateModuleId() {
        int maxId = 0;

        for (Module module : InteractTxt.allModule) {
            String moduleId = module.getModuleId();
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

    private void clearForm() {
        modField.setText("");
        jList1.clearSelection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        modField.setText("jTextField1");
        modField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modFieldActionPerformed(evt);
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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Add Module");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel5.setText("Hold Ctrl + Left Click to Select more Lecturer");

        jLabel2.setText("Assigned Lecturer :");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 466, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton2)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(21, 21, 21)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(modField, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(jLabel5)
                                                    .addGap(40, 40, 40))
                                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)))))
                                .addComponent(jLabel4))))
                    .addContainerGap(21, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButton2)
                    .addGap(24, 24, 24)
                    .addComponent(jLabel4)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(modField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(28, 28, 28)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel5)
                    .addGap(18, 18, 18)
                    .addComponent(jButton1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        saveModuleData();
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField modField;
    // End of variables declaration//GEN-END:variables
}
