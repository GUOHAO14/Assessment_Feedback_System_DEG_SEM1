package project.gui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import project.roles.*;
import project.utils.*;

public class ManageGradingSystem extends FrameFormat {

    private Admin sessionUser;
    
    private DefaultTableModel model = new DefaultTableModel(
        new String[]{"Grade", "Marks From", "Marks To"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false;
            }

            if (column == 1 && row == (InteractTxt.allGrading.size()-1)) {
                return false;
            }

            if (column == 2 && row == 0) {
                return false;
            }
            return true;
        }
    };
    
    private boolean ProgramUpdate = false;
    private boolean Loop = true;
    
    private void rejectEdit(int row, int col, String oldValue, String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        ProgramUpdate = true;
        model.setValueAt(oldValue, row, col);
        ProgramUpdate = false;
    }
    
    public ManageGradingSystem(Admin sessionUser) {
        initComponents();
        super.formatWindow("Grading System");
        this.sessionUser = sessionUser;
        InteractTxt.allGrading.clear();
        InteractTxt.readGrade();
        for(Grading x : InteractTxt.allGrading){
            model.addRow(new String[]{x.getGrade(), x.getMarksFrom(), x.getMarksTo()});
        }
        
        model.addTableModelListener(e -> {
            if (ProgramUpdate) return;
            
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                Object NewV = model.getValueAt(row, col);
                Grading OldV = InteractTxt.allGrading.get(row);
                System.out.println("Changed at row " + row + ", col " + col + ": " + NewV);
                
                if (col == 1){
                    if (row == (InteractTxt.allGrading.size()-1)) {
                        rejectEdit(row, col, OldV.getMarksFrom(), "Cannot edit");
                        return;
                    }
                    
                    try {
                        int NewValue = Integer.parseInt(String.valueOf(NewV));
                        
                        if(NewValue < 0+((InteractTxt.allGrading.size()-1)-row)*2){
                            rejectEdit(row, col, OldV.getMarksFrom(), "Too Small");
                            return;
                        } else if (NewValue > 100-((row*2)+1)){
                            rejectEdit(row, col, OldV.getMarksFrom(), "Too Big");
                            return;
                        }
                        
                        if(Loop){
                            OldV.setMarksFrom(String.valueOf(NewValue));
                            InteractTxt.allGrading.get(row+1).setMarksTo(String.valueOf(NewValue - 1));
                            Loop = false;
                            model.setValueAt(InteractTxt.allGrading.get(row+1).getMarksTo(), (row+1), 2);
                        }
                        Loop = true;
                        
                        if (NewValue >= Integer.parseInt(OldV.getMarksTo())){
                            OldV.setMarksTo(String.valueOf(NewValue + 1));
                            model.setValueAt(OldV.getMarksTo(), row, 2);
                        }
                    } catch (Exception f) {
                        rejectEdit(row, col, OldV.getMarksFrom(), "Must be Number");
                    }
                    
                } else if (col == 2){
                    if (row == 0) {
                        rejectEdit(row, col, OldV.getMarksTo(), "Cannot edit");
                        return;
                    }
                    
                    try {
                        int NewValue = Integer.parseInt(String.valueOf(NewV));
                        
                        if(NewValue < 0+(((InteractTxt.allGrading.size()-1)-row)*2)+1){
                            rejectEdit(row, col, OldV.getMarksTo(), "Too Small");
                            return;
                        } else if (NewValue > 100-(row*2)){
                            rejectEdit(row, col, OldV.getMarksTo(), "Too Big");
                            return;
                        }
                        
                        if(Loop){
                            OldV.setMarksTo(String.valueOf(NewValue));
                            InteractTxt.allGrading.get(row-1).setMarksFrom(String.valueOf(NewValue + 1));
                            Loop = false;
                            model.setValueAt(InteractTxt.allGrading.get(row-1).getMarksFrom(), (row-1), 1);
                        }
                        Loop = true;
                        
                        if (NewValue <= Integer.parseInt(OldV.getMarksFrom())){
                            OldV.setMarksFrom(String.valueOf(NewValue - 1));
                            model.setValueAt(OldV.getMarksFrom(), row, 1);
                        }
                    } catch (Exception f) {
                        rejectEdit(row, col, OldV.getMarksTo(), "Must be Number");
                    }
                }
            }
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        GradeTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Manage Grading System");

        GradeTable.setModel(model);
        jScrollPane1.setViewportView(GradeTable);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(23, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            InteractTxt.writeGrade();
            JOptionPane.showMessageDialog(this, "Saved Successfully", "Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "Do you sure you want to exit?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            InteractTxt.allGrading.clear();
            InteractTxt.readGrade();
            new Dashboard(sessionUser).setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ManageGradingSystem().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable GradeTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
