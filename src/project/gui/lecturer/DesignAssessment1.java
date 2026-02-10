/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.gui.lecturer;

import project.utils.exceptions.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import project.roles.*;
import project.utils.*;
import javax.swing.*;
/**
 *
 * @author Khoo Guo Hao
 */
public class DesignAssessment1 extends FrameFormat {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DesignAssessment1.class.getName());
    private final Lecturer sessionUser;
    private final IntakeModule designIM;
    String [] assessmentTypes = {"Assignment", "Class Test", "Examination", "Presentation"};
    private ArrayList<Assessment> workingAssessmentList = new ArrayList<>();
    private ArrayList<String> usedAssIds = new ArrayList<>();
    private boolean saveStatus = true;
    
    // constructor
    public DesignAssessment1(Lecturer sessionUser, IntakeModule designIM) {
        initComponents();
        super.formatWindow("Design Module Assessment - Page 2");
        this.sessionUser = sessionUser;
        this.designIM = designIM;
        
        System.out.println(this.designIM.getIntakeId());
        System.out.println(this.designIM.getModuleId());
        assessmentContainer.setLayout(
            new BoxLayout(assessmentContainer, BoxLayout.Y_AXIS)
        );
        saveChanges.setEnabled(false);
        workingAssessmentList = new ArrayList<>();
        
        for (Assessment a : designIM.IM_Assessments) {
            workingAssessmentList.add(new Assessment(a));
        }
        
        identifyInUseAssessment();
        generateAssessmentList(workingAssessmentList);
        
        intakeText.setText("Intake: "+designIM.getIntakeId()+" ("+InteractTxt.checkIntID(designIM.getIntakeId()).getIntakeName()+")");
        moduleText.setText("Module: "+designIM.getModuleId()+" ("+InteractTxt.checkModID(designIM.getModuleId()).getModuleName()+")");
    }
    
    
    // utility methods
    
    private void generateAssessmentList(ArrayList<Assessment> assessmentList) {
        assessmentContainer.removeAll();
        
        assessmentList.forEach(a ->{
            
            String type = a.getAssType();
            String name = a.getAssName();
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
        
        // if assessment already exist
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
                    if (!createSignal) {
                        String oldType = a.getAssType();
                        String oldName = a.getAssName();
                        String oldPercent = a.getAssPercentage();
                        String oldFullMarks = a.getAssFullMarks();

                        if (oldType.equals(type) && oldName.equals(name) && oldPercent.equals(percent) && oldFullMarks.equals(fullMarks)) {
                            JOptionPane.showMessageDialog(
                                    this, 
                                    "No changes are made.",
                                    "Assessment Creation", 
                                    1);
                            break;
                        } 
                    }
                    
                    try {
                        int intPercent = Integer.parseInt(percent);
                        int intFullMarks = Integer.parseInt(fullMarks);
                        
                        // value checking
                        if (name.length() > Constants.ITEM_NAME_MAX_LENGTH) throw new ItemNameMaxLengthException("Assessment");
                        if (intPercent < 1 || intPercent > 100) throw new IntegerRangeException("Score Percentage", 1, 100);
                        if (intFullMarks < 1 || intFullMarks > 100) throw new IntegerRangeException("Full Marks", 10, 100);
                        
                        // create new assessment
                        if (createSignal) {
                            String newId = Assessment.getNewAssID();
                            Assessment newAssessment = new Assessment(newId, name, type, percent, fullMarks, designIM);

                            workingAssessmentList.add(newAssessment);
                            
                        } else {
                            a.setAssFullMarks(fullMarks);
                            a.setAssPercentage(percent);
                            a.setAssName(name);
                            a.setAssType(type);
                        }
                        
                        JOptionPane.showMessageDialog(
                                this, 
                                "Assessment creted.\nType: " + type+"\nName: " + name+"\nFull Marks: "+fullMarks+"\nScore Percent: " + percent,
                                "Assessment Creation", 
                                1);
                        
                        saveChanges.setEnabled(true);
                        saveStatus = false;
                        generateAssessmentList(workingAssessmentList);
                        loop = false;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Assessment edit failed.\nScore Percentage input must be an integer.", "Error - Invalid Value", 0);
                    } catch (ItemNameMaxLengthException | IntegerRangeException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error - Invalid Value", 0);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Assessment edit failed.\nReport this error.", "Error - Unknown Error", 0);
                    } 
                } 

            } else {
                break;
            }
        }
    }
    
    private void identifyInUseAssessment() {
        ArrayList<String> oldAssIds = new ArrayList<>();
        
        designIM.IM_Assessments.forEach(a -> {
            oldAssIds.add(a.getAssId());
        });
        
        for (Student s : InteractTxt.allStudent) {
            for (StudentScore score : s.Stu_Scores) {
                // check if assessment has been scored before exist
                if (oldAssIds.contains(score.getAssessment().getAssId())) {
                    usedAssIds.add(score.getAssessment().getAssId());
                }
            }
        }
        
        usedAssIds.forEach(a -> {
            System.out.println(a);
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

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        assessmentScroll = new javax.swing.JScrollPane();
        assessmentContainer = new javax.swing.JPanel();
        saveChanges = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        intakeText = new javax.swing.JLabel();
        moduleText = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("+ Add New Assessment");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        assessmentContainer.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout assessmentContainerLayout = new javax.swing.GroupLayout(assessmentContainer);
        assessmentContainer.setLayout(assessmentContainerLayout);
        assessmentContainerLayout.setHorizontalGroup(
            assessmentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );
        assessmentContainerLayout.setVerticalGroup(
            assessmentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 242, Short.MAX_VALUE)
        );

        assessmentScroll.setViewportView(assessmentContainer);

        saveChanges.setText("Save");
        saveChanges.addActionListener(this::saveChangesActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Update Module Assessment Design");

        intakeText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        intakeText.setText("Intake: ");

        moduleText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        moduleText.setText("Module:");

        backButton.setText("Back");
        backButton.addActionListener(this::backButtonActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(moduleText, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                                    .addComponent(intakeText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(263, 263, 263)
                        .addComponent(saveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(assessmentScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(intakeText)
                .addGap(28, 28, 28)
                .addComponent(moduleText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(assessmentScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveChanges)
                .addGap(47, 47, 47))
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
        int total = 0;
        for (Assessment ass : workingAssessmentList) {
            total += Integer.parseInt(ass.getAssPercentage());
        }
        
        try {
            if (total != 100) {
                throw new ValueErrorException("100", "total combined percentage");
            } 

            ArrayList<Assessment> deletingAss = new ArrayList<>();
            
            // check if existing assessment change does not involve percent or full marks
            // if yes, that assessment's existing records must be deleted 
            // therefore, add it into a deletingAss arraylist 
            for (Assessment newAss : workingAssessmentList) {
                for (Assessment oldAss : designIM.IM_Assessments) {

                    if (newAss.getAssId().equals(oldAss.getAssId())) {
                        if (usedAssIds.contains(oldAss.getAssId()) && (!oldAss.getAssPercentage().equals(newAss.getAssPercentage()) || !oldAss.getAssFullMarks().equals(newAss.getAssFullMarks()))) {
                            deletingAss.add(oldAss);
                        }
                    }
                }
            }

            ArrayList<String> deletingID = new ArrayList<>();
            for (Assessment a : deletingAss) {
                deletingID.add(a.getAssId()+" ("+a.getAssName()+")");
            }
            
            // if deletingAss has items --> got assessment needed to be deleted
            // ask user for confirmation
            if (!deletingAss.isEmpty()) {
                int userConfirm = JOptionPane.showConfirmDialog(
                this, 
                "There are already student scores recorded for "+String.join(", ", deletingID)+".\nIf you insist this change, all existing student scores and grades will be deleted for this intake module.\nProceed with the change?", 
                "Assessment Creation", 
                JOptionPane.YES_NO_OPTION, 
                2);

                if (userConfirm == JOptionPane.YES_OPTION) {
                    //reset all student grades
                    Tools.setAllAssStuGrades(designIM, "NA");
                    //delete score
                    for (Assessment byeAss : deletingAss) {
                        Tools.deleteAllAssStuScores(byeAss);
                    }
                } else {
                    throw new DontSaveChangesException();
                }
            } 
            
            // clear original assessments
            designIM.IM_Assessments.clear();
            
            // add new assessments - allAssessment global variable also
            designIM.IM_Assessments.addAll(workingAssessmentList);
            Iterator<Assessment> assIterator = InteractTxt.allAssessment.iterator();

            // loop using the iterator's methods
            while (assIterator.hasNext()) {
                Assessment a = assIterator.next();
                // search for assessments that are under the current intake_module
                if (a.getAssIM().getIMID().equals(designIM.getIMID())) {
                    // remove the old assessments
                    assIterator.remove(); 
                }
            }
            // add new assessments for the intake_module & save in txt file
            InteractTxt.allAssessment.addAll(workingAssessmentList);
            InteractTxt.saveDatabase();
            
            //indicate that changes have been saved
            saveChanges.setEnabled(false);
            saveStatus = true;

        } catch (ValueErrorException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error - Invalid Value", 0);
        } catch (DontSaveChangesException e) {
            JOptionPane.showMessageDialog(this, "No changes are made.", "Info - Discard Changes", 1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage()+"\nReport this error.", "Error - Unknown Error", 0);
            e.printStackTrace();
        }
        
        
    }//GEN-LAST:event_saveChangesActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        if (saveStatus) {
            new DesignAssessment0(sessionUser).setVisible(true);
            this.dispose();
        } else {
            int userConfirm = JOptionPane.showConfirmDialog(
                        this, 
                        "You have not saved your changes.\nDo you wish to save your changes?\nSelect \"Cancel\" to cancel the page exit.", 
                        "Exit Page", 
                        JOptionPane.YES_NO_CANCEL_OPTION);
            
            switch (userConfirm) {
                case JOptionPane.YES_OPTION -> {
                    saveChanges.doClick();
                    JOptionPane.showMessageDialog(
                                this, 
                                "Your changes are saved.\nYou will now safely exit the page.",
                                "Save Changes and Exit", 
                                1);
                    new DesignAssessment0(sessionUser).setVisible(true);
                    this.dispose();
                }
                case JOptionPane.NO_OPTION -> {
                    JOptionPane.showMessageDialog(
                                this, 
                                "Your changes did not save.\nYou will not exit the page.",
                                "Discard Changes and Exit", 
                                1);
                    new DesignAssessment0(sessionUser).setVisible(true);
                    this.dispose();
                }
                default -> {
                }
            }
        }
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
