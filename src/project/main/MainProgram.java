package project.main;

import project.utils.InteractTxt;


public class MainProgram {
    public static void main(String[]args) {
        new LoginPage0().setVisible(true);
        InteractTxt.initDatabase();
    }
}
