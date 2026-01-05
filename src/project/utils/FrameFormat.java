package project.utils;

/**
 *
 * @author Khoo Guo Hao
 */
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

abstract public class FrameFormat extends JFrame {
    
    public FrameFormat() {
        // intercept termination
        // set the default to DO_NOTHING_ON_CLOSE so the window doesn't disappear
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 

        // add a WindowListener to handle the event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // extract name of parent component
                JFrame closingFrame = (JFrame) windowEvent.getComponent();
                String frameClassName = closingFrame.getClass().getSimpleName();
                
                // when 'X' button is clicked, this method runs
                // ask for confirmation when exit (excluding login pages)
                if (!frameClassName.contains("LoginPage")) {
                    
                    int choice = JOptionPane.showConfirmDialog(
                        windowEvent.getComponent(), 
                        "Exiting the program. Save all changes?", 
                        "Exit Program", 
                        JOptionPane.YES_NO_CANCEL_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                
                    // YES triggers graceful exit
                    if (choice == JOptionPane.YES_OPTION) {

                        if (!frameClassName.contains("LoginPage")) {
                            if (ErrorChecking.checkIM_Assessments()) {
                                
                                System.out.println(windowEvent.getComponent());

                                System.out.println("--- Executing Program Termination ---");
                                //run before shutdown
                                //task 1: logging user data
                                try {
                                    FileWriter writer = new FileWriter("src/resources/user_log.txt", true);
                                    writer.write("Application terminated gracefully at " + new java.util.Date() + "\n");
                                    writer.close();
                                    System.out.println("Log file updated successfully.");

                                } catch (IOException e) {
                                    System.err.println("Error during shutdown cleanup: " + e.getMessage());
                                }
                                //task 2: save all data

                                InteractTxt.saveDatabase();
                                System.out.println("Cleanup completed: Application state saved.");

                                System.exit(0); 
                            } else {
                                JOptionPane.showMessageDialog(windowEvent.getComponent(), "Cannot exit program. There is an error in IntakeModule Assessments", "Exit Program Failure", JOptionPane.WARNING_MESSAGE);
                            }
                            

                        }
                    } else if (choice == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(windowEvent.getComponent(), "No changes are made.", "Exit Program", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0); 
                    }
                    else System.out.println("Continue to use the program");
                } else {
                    System.exit(0); 
                }
            }
        });
    }
    
    public void formatWindow(String title) {
        setTitle(title);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
    }
    
    
}
