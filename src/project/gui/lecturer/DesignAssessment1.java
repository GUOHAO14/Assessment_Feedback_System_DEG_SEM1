/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.lecturer;

import project.utils.exceptions.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import project.roles.*;
import project.utils.*;
import javax.swing.*;
/**
 *
 * @author Khoo Guo Hao
 */
public class DesignAssessment1 extends FrameFormat {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DesignAssessment1.class.getName());
    private Lecturer sessionUser;
    private IntakeModule designIM;
    String [] assessmentTypes = {"Assignment", "Class Test", "Examination", "Presentation"};
    private ArrayList<Assessment> originalAssessments;
    
    // utility methods
    
    private void generateAssessmentList() {
        assessmentContainer.removeAll();
        
        designIM.IM_Assessments.forEach(a ->{
            
            String type = a.getAssType();
            String name = a.getAssName();
            String id = a.getAssId();
            String percent = a.getAssPercentage();
            
            JButton part = new JButton("<html>"+type+" ("+percent+"%) - "+name+"</html>");
            
            //button and panel design
            part.setHorizontalAlignment(SwingConstants.LEFT);  // <--- key

            // Optional: add some padding so text isn't right at the edge
            part.setMargin(new Insets(10, 10, 10, 10));

            part.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            part.setPreferredSize(new Dimension(assessmentContainer.getWidth() - 100, 50)); 

            part.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    createEditDialog(a);
                }
            });
            
            // FORCE size so Swing cannot collapse it
            assessmentContainer.add(part);
            assessmentContainer.revalidate();
            assessmentContainer.repaint();
        });
    }
    
    private void createEditDialog(Assessment a) {
        
        boolean createSignal = true;
        JComboBox typeDropdown = new JComboBox(assessmentTypes);
        JTextField assessmentName = new JTextField(10);
        JTextField assessmentPercent = new JTextField(10);
        JTextField assessmentFullMarks = new JTextField(10);
        
        String type;
        String name;
        String percent;
        String fullMarks;
        
        JPanel panel = new JPanel();
        
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Assessment Type:"));
        panel.add(typeDropdown);
        panel.add(new JLabel("Assessment Name:"));
        panel.add(assessmentName);  
        panel.add(new JLabel("Full Marks % (integer):"));
        panel.add(assessmentFullMarks);
        panel.add(new JLabel("Total Score Percentage % (integer):"));
        panel.add(assessmentPercent);
        
        panel.setPreferredSize(new Dimension(400, 120));
        
        if (a != null) {
            assessmentName.setText(a.getAssName());
            assessmentFullMarks.setText(a.getAssFullMarks());
            assessmentPercent.setText(a.getAssPercentage());
            typeDropdown.setSelectedItem(a.getAssType());
            createSignal = false;
        }
        
        boolean loop = true;
        
        while (loop) {
            int userInput = JOptionPane.showConfirmDialog(this, panel, "Select Assessment Type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
            if (userInput == JOptionPane.OK_OPTION) {
                
                type = typeDropdown.getSelectedItem().toString();
                name = assessmentName.getText();
                percent = assessmentPercent.getText();
                fullMarks = assessmentFullMarks.getText();
                
                int userConfirm = JOptionPane.showConfirmDialog(
                        this, 
                        "Type: " + type+"\nName: " + name+"\nFull Marks: "+fullMarks+"\nScore Percent: " + percent, 
                        "Confirm Creation", 
                        JOptionPane.YES_NO_OPTION);
                if (userConfirm == JOptionPane.YES_OPTION) {
                    //make sure IM not > 100
                    //ErrorChecking class in /utils
                    //check total percent to make sure always be 100 when saving
                    //dont allow to exit program if not 100
                    
                    //try-catch block to ensure integer input for percent
                    
                    try {
                        int intPercent = Integer.parseInt(percent);
                        int intFullMarks = Integer.parseInt(fullMarks);
                        
                        if (name.length() > Constants.ITEM_NAME_MAX_LENGTH) throw new ItemNameMaxLengthException("Assessment");
                        if (intPercent < 1 || intPercent > 100) throw new IntegerRangeException("Score Percentage", 1, 100);
                        if (intFullMarks < 1 || intFullMarks > 100) throw new IntegerRangeException("Full Marks", 10, 100);
                        
                        int total = intPercent;
                        for (Assessment ass : designIM.IM_Assessments) {
                            if (!createSignal) if (a.getAssId().equals(ass.getAssId())) continue;

                            total += Integer.parseInt(ass.getAssPercentage());
                        }
                        System.out.println(total);
                        if (total <= 100) {
                            if (createSignal) {
                                String newId = "Ass"+String.valueOf(InteractTxt.allAssessment.size() + 1);
                                Assessment newAssessment = new Assessment(newId, name, type, percent, fullMarks, designIM);

                                designIM.IM_Assessments.add(newAssessment);
                                InteractTxt.allAssessment.add(newAssessment);
                            } else {
                                a.setAssName(name);
                                a.setAssType(type);
                                a.setAssPercentage(percent);
                            }

                            System.out.println("Type: " + type);
                            System.out.println("Name: " + name);
                            System.out.println("Percent: " + percent);

                            generateAssessmentList();

                            loop = false;
                        } else {
                            JOptionPane.showMessageDialog(this, "Assessment edit failed.\nTotal percentage has exceeded 100.", "Error - Invalid Value", 0);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Assessment edit failed.\nScore Percentage input must be an integer.", "Error - Invalid Value", 0);
                    } catch (ItemNameMaxLengthException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error - Invalid Value", 0);
                    } catch (IntegerRangeException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error - Invalid Value", 0);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Assessment edit failed.\nReport this error.", "Error - Unknown Error", 0);
                    }
                } 

            } else {
                JOptionPane.showMessageDialog(this, "Assessment edit failed!", "Error - Incomplete Action", 0);
                break;
            }
        }
    }
    
    // constructor
    public DesignAssessment1(Lecturer sessionUser, IntakeModule designIM) {
        initComponents();
        super.formatWindow("Design Module Assessment - Page 2");
        this.sessionUser = sessionUser;
        this.designIM = designIM;
        this.originalAssessments = designIM.IM_Assessments;
        
        System.out.println(this.designIM.getIntakeId());
        System.out.println(this.designIM.getModuleId());
        assessmentContainer.setLayout(
            new BoxLayout(assessmentContainer, BoxLayout.Y_AXIS)
        );
        generateAssessmentList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        assessmentScroll = new javax.swing.JScrollPane();
        assessmentContainer = new javax.swing.JPanel();
        saveChanges = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        intakeText = new javax.swing.JLabel();
        moduleText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("+ Add New Assessment");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        assessmentContainer.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout assessmentContainerLayout = new javax.swing.GroupLayout(assessmentContainer);
        assessmentContainer.setLayout(assessmentContainerLayout);
        assessmentContainerLayout.setHorizontalGroup(
            assessmentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 404, Short.MAX_VALUE)
        );
        assessmentContainerLayout.setVerticalGroup(
            assessmentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 242, Short.MAX_VALUE)
        );

        assessmentScroll.setViewportView(assessmentContainer);

        saveChanges.setText("Save");
        saveChanges.addActionListener(this::saveChangesActionPerformed);

        backButton.setText("Back");
        backButton.addActionListener(this::backButtonActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Update Module Assessment Design");

        intakeText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        intakeText.setText("Intake: ");

        moduleText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        moduleText.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(143, 143, 143)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(saveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(assessmentScroll)
                            .addComponent(moduleText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(intakeText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(intakeText)
                .addGap(28, 28, 28)
                .addComponent(moduleText)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(assessmentScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(saveChanges)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        String hello = JOptionPane.showInputDialog(this, "Hello", "lol", JOptionPane.PLAIN_MESSAGE);
//        System.out.println(hello);
        createEditDialog(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void saveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesActionPerformed
        // TODO add your handling code here:
        ErrorChecking.checkIM_Assessments();
    }//GEN-LAST:event_saveChangesActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new DesignAssessment0(sessionUser).setVisible(true);
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
//        java.awt.EventQueue.invokeLater(() -> new DesignAssessment1().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel assessmentContainer;
    private javax.swing.JScrollPane assessmentScroll;
    private javax.swing.JButton backButton;
    private javax.swing.JLabel intakeText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel moduleText;
    private javax.swing.JButton saveChanges;
    // End of variables declaration//GEN-END:variables
}
