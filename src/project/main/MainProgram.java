package project.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import project.gui.lecturer.ViewStudentComments;
import project.utils.InteractTxt;


public class MainProgram {
    public static void main(String[]args) {
        try {
            /* Create and display the form */
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            new LoginPage0().setVisible(true);
            InteractTxt.initDatabase();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ViewStudentComments.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (InstantiationException ex) {
            System.getLogger(ViewStudentComments.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IllegalAccessException ex) {
            System.getLogger(ViewStudentComments.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            System.getLogger(ViewStudentComments.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
