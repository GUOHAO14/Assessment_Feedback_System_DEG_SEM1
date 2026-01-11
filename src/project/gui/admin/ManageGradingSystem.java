package project.gui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import project.roles.*;
import project.utils.*;

public class ManageGradingSystem extends javax.swing.JFrame {

    private DefaultTableModel model = new DefaultTableModel(
        new String[]{"Grade", "Marks From", "Marks To"}, 0
    );
    
    private boolean ProgramUpdate = false;
    private boolean abc = true;
    
    public ManageGradingSystem() {
        initComponents();
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
                System.out.println("Changed at row " + row + ", col " + col + ": " + NewV);
                
                if(col == 0){
                    
                } else if (col == 1){
                    if (row == (InteractTxt.allGrading.size()-1)) {
                        JOptionPane.showMessageDialog(this, "Cannot edit", "Error", JOptionPane.ERROR_MESSAGE);
                        ProgramUpdate = true;
                        model.setValueAt(InteractTxt.allGrading.get(row).getMarksFrom(), row, col);
                        ProgramUpdate = false;
                        return;
                    }
                    
                    try {
                        int NewValue = Integer.parseInt(String.valueOf(NewV));
                        
                        if(NewValue < 0+((InteractTxt.allGrading.size()-1)-row)*2){
                            JOptionPane.showMessageDialog(this, "Too Small", "Error", JOptionPane.ERROR_MESSAGE);
                            ProgramUpdate = true;
                            model.setValueAt(InteractTxt.allGrading.get(row).getMarksFrom(), row, col);
                            ProgramUpdate = false;
                            return;
                        } else if (NewValue > 100-((row*2)+1)){
                            JOptionPane.showMessageDialog(this, "Too Big", "Error", JOptionPane.ERROR_MESSAGE);
                            ProgramUpdate = true;
                            model.setValueAt(InteractTxt.allGrading.get(row).getMarksFrom(), row, col);
                            ProgramUpdate = false;
                            return;
                        }
                        
                        if(abc){
                            InteractTxt.allGrading.get(row).setMarksFrom(String.valueOf(NewValue));
                            InteractTxt.allGrading.get(row+1).setMarksTo(String.valueOf(NewValue - 1));
                            abc = false;
                            model.setValueAt(InteractTxt.allGrading.get(row+1).getMarksTo(), (row+1), 2);
                        }
                        abc = true;
                        
                        if (NewValue >= Integer.parseInt(InteractTxt.allGrading.get(row).getMarksTo())){
                            InteractTxt.allGrading.get(row).setMarksTo(String.valueOf(NewValue + 1));
                            model.setValueAt(InteractTxt.allGrading.get(row).getMarksTo(), row, 2);
                        }
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(this, "Must be Number", "Error", JOptionPane.ERROR_MESSAGE);
                        ProgramUpdate = true;
                        model.setValueAt(InteractTxt.allGrading.get(row).getMarksFrom(), row, col);
                        ProgramUpdate = false;
                    }
                    
                } else if (col == 2){
                    if (row == 0) {
                        JOptionPane.showMessageDialog(this, "Cannot edit", "Error", JOptionPane.ERROR_MESSAGE);
                        ProgramUpdate = true;
                        model.setValueAt(InteractTxt.allGrading.get(row).getMarksTo(), row, col);
                        ProgramUpdate = false;
                        return;
                    }
                    
                    try {
                        int NewValue = Integer.parseInt(String.valueOf(NewV));
                        
                        if(NewValue < 0+(((InteractTxt.allGrading.size()-1)-row)*2)+1){
                            JOptionPane.showMessageDialog(this, "Too Small", "Error", JOptionPane.ERROR_MESSAGE);
                            ProgramUpdate = true;
                            model.setValueAt(InteractTxt.allGrading.get(row).getMarksTo(), row, col);
                            ProgramUpdate = false;
                            return;
                        } else if (NewValue > 100-(row*2)){
                            JOptionPane.showMessageDialog(this, "Too Big", "Error", JOptionPane.ERROR_MESSAGE);
                            ProgramUpdate = true;
                            model.setValueAt(InteractTxt.allGrading.get(row).getMarksTo(), row, col);
                            ProgramUpdate = false;
                            return;
                        }
                        
                        if(abc){
                            InteractTxt.allGrading.get(row).setMarksTo(String.valueOf(NewValue));
                            InteractTxt.allGrading.get(row-1).setMarksFrom(String.valueOf(NewValue + 1));
                            abc = false;
                            model.setValueAt(InteractTxt.allGrading.get(row-1).getMarksFrom(), (row-1), 1);
                        }
                        abc = true;
                        
                        if (NewValue <= Integer.parseInt(InteractTxt.allGrading.get(row).getMarksFrom())){
                            InteractTxt.allGrading.get(row).setMarksFrom(String.valueOf(NewValue - 1));
                            model.setValueAt(InteractTxt.allGrading.get(row).getMarksFrom(), row, 1);
                        }
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(this, "Must be Number", "Error", JOptionPane.ERROR_MESSAGE);
                        ProgramUpdate = true;
                        model.setValueAt(InteractTxt.allGrading.get(row).getMarksTo(), row, col);
                        ProgramUpdate = false;
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Manage Grading System");

        GradeTable.setModel(model);
        jScrollPane1.setViewportView(GradeTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageGradingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageGradingSystem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable GradeTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
