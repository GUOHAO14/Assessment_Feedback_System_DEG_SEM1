/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.leader;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import project.utils.*;
import project.roles.*;
import project.roles.Module;

/**
 *
 * @author US
 */
public class LeaderModule extends FrameFormat {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeaderModule.class.getName());
    private Leader sessionUser;
    private DefaultTableModel model = new DefaultTableModel();
    private int row = -1;
    private String[] columnName = {"Module ID", "Module Name", "Assigned Lecturers"};
    private JPopupMenu popupMenu;

    public LeaderModule(Leader sessionUser) {
        initComponents();
        super.formatWindow("Leader Module");
        this.sessionUser = sessionUser;
        model.setColumnIdentifiers(columnName);

        createPopupMenu();

        loadModuleData();
    }


    private void createPopupMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit Module");
        editItem.addActionListener(e -> {
            if (row != -1) {
                String moduleId = model.getValueAt(row, 0).toString();
                String moduleName = model.getValueAt(row, 1).toString();

                new LeaderEditModule(sessionUser, moduleId, moduleName).setVisible(true);
                this.dispose();
            }
        });

        JMenuItem deleteItem = new JMenuItem("Delete Module");
        deleteItem.addActionListener(e -> {
            if (row != -1) {
                String moduleId = model.getValueAt(row, 0).toString();
                String moduleName = model.getValueAt(row, 1).toString();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete module: " + moduleName + "?\n"
                        + "This will also remove all lecturer assignments and related data.",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Module moduleToDelete = InteractTxt.checkModID(moduleId);
                        if (moduleToDelete != null) {
                            // Remove module from all lecturers who teach it
                            for (Lecturer lecturer : moduleToDelete.Mod_Lecturers) {
                                lecturer.Lec_Modules.remove(moduleToDelete);
                            }

                            // Remove from leader's module list
                            sessionUser.Lea_Modules.remove(moduleToDelete);

                            // Remove all IntakeModules associated with this module
                            java.util.Iterator<IntakeModule> imIterator = InteractTxt.allIntakeModule.iterator();
                            while (imIterator.hasNext()) {
                                IntakeModule im = imIterator.next();
                                if (im.getModuleId().equals(moduleId)) {
                                    imIterator.remove();
                                }
                            }

                          
                            InteractTxt.allModule.remove(moduleToDelete);
                        }

                        
                        InteractTxt.saveDatabase();

                        JOptionPane.showMessageDialog(
                                this,
                                "Module deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        loadModuleData();

                    } catch (Exception ex) {
                        logger.log(java.util.logging.Level.SEVERE, "Error deleting module", ex);
                        JOptionPane.showMessageDialog(
                                this,
                                "Error deleting module: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        popupMenu.add(editItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);
    }

    public void loadModuleData() {
        model.setRowCount(0);

        // Use a Set to track already added modules (to avoid duplicates)
        java.util.Set<String> addedModules = new java.util.HashSet<>();

        for (Module module : InteractTxt.allModule) {
            if (module != null && module.getLeader() != null) {
                // Show only modules created by this leader
                if (!module.getLeader().getId().equals(sessionUser.getId())) {
                    continue;
                }

                // Skip if already added (avoid duplicates)
                if (addedModules.contains(module.getModuleId())) {
                    continue;
                }

                // Build lecturer names string
                StringBuilder lecturerNames = new StringBuilder();
                if (module.Mod_Lecturers.isEmpty()) {
                    lecturerNames.append("No lecturers assigned");
                } else {
                    for (int i = 0; i < module.Mod_Lecturers.size(); i++) {
                        Lecturer lecturer = module.Mod_Lecturers.get(i);
                        lecturerNames.append(lecturer.getName());
                        if (i < module.Mod_Lecturers.size() - 1) {
                            lecturerNames.append(", ");
                        }
                    }
                }

                Object[] rowData = {
                    module.getModuleId(),
                    module.getModuleName(),
                    lecturerNames.toString()
                };
                model.addRow(rowData);
                addedModules.add(module.getModuleId());
            }
        }

        System.out.println("Total modules loaded: " + model.getRowCount());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton4.setText("Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Manage Module");

        jTable1.setModel(model);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Click A Row to Edit or Delete Module");

        jButton1.setText("Add Module");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(473, 473, 473)
                                .addComponent(jButton1))
                            .addComponent(jLabel1)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4)))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(205, 205, 205))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new LeaderDashboard(sessionUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        System.out.println("Clicked");
        row = jTable1.rowAtPoint(evt.getPoint());

        if (row != -1) {
            jTable1.setRowSelectionInterval(row, row);

            // Show popup menu on right-click or regular click
            if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3 || evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                popupMenu.show(jTable1, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new LeaderAddModule(sessionUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(LeaderModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaderModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaderModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaderModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new LeaderModule().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
