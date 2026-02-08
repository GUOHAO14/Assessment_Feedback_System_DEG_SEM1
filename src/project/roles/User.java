package project.roles;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import javax.swing.JOptionPane;
import project.utils.ErrorChecking;
import project.utils.FrameFormat;
import project.utils.InteractTxt;

public abstract class User {
    
    private final String id;
    private String name, email, password, role;
    
    public User(String[] data) {
        this.id = data[0];
        this.name = data[1];
        this.email = data[2];
        this.password = data[3];
        this.role = data[4];
    }
    
    public void setDetails(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        // add input validation here 
        // add try-catch
        // add JOptionPane (class)
    }
    
    public void setDetails(String [] data) {
        this.name = data[1];
        this.email = data[2];
        this.password = data[3];
        this.role = data[4];
        
        // add input validation here 
        // add try-catch
        // add JOptionPane (class)
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPW() {
        return this.password;
    }
    
    public void setPW(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String[] getFullUserData() {
        String [] userFullData = {id, name, email, password, role};
        return userFullData;
    }
    
    public void logout(FrameFormat yourPage) {
        String frameClassName = yourPage.getClass().getSimpleName();
                    
        int choice = JOptionPane.showConfirmDialog(
            yourPage, 
            "Logging out the program.\nAre you sure?", 
            "Exit Program", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);

        // YES triggers graceful exit
        if (choice == JOptionPane.YES_OPTION) {
            // more error checking functions if needed 
            
            if (ErrorChecking.checkIM_Assessments()) {

                System.out.println("--- Executing Program Logout ---");
                //run before shutdown
                //task 1: logging user data
                try {
                    FileWriter writer = new FileWriter("src/resources/user_log.txt", true);
                    writer.write("Application logged out by "+this.getId()+" at " + new java.util.Date() + "\n");
                    writer.close();
                    System.out.println("Log file updated successfully.");

                } catch (IOException e) {
                    System.err.println("Error during logging: " + e.getMessage());
                }
                //task 2: save all data

                InteractTxt.saveDatabase();
                
                new project.main.LoginPage0().setVisible(true);
                yourPage.dispose();
            } else {
                JOptionPane.showMessageDialog(yourPage, "Cannot log out from program. There is an error in IntakeModule Assessments", "Logout Program Failure", JOptionPane.WARNING_MESSAGE);
            }
        } else System.out.println("Continue to use the program");
    }
    
    public static String getNewLcID() {
        int max = 0;
        for(Lecturer x : InteractTxt.allLecturer){
            String numPart = x.getId().substring(2);
            int num = Integer.parseInt(numPart);
            if (num > max) {
                max = num;
            }
        }
        for(Leader x : InteractTxt.allLeader){
            String numPart = x.getId().substring(2);
            int num = Integer.parseInt(numPart);
            if (num > max) {
                max = num;
            }
        }
        String id = "lc" + (max + 1);
        return id;
    }
    
    public static String getNewStuID() {
        int max = 0;
        for(Student x : InteractTxt.allStudent){
            String numPart = x.getId().substring(2);
            int num = Integer.parseInt(numPart);
            if (num > max) {
                max = num;
            }
        }
        String id = "tp" + (max + 1);
        return id;
    }
    
    public static String GeneratePW(){
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String number = "0123456789";
        String symbol = "!@#$%^&*()-_=+[]{};:,.<>?/";
        SecureRandom random = new SecureRandom();
        ArrayList<Character> pw = new ArrayList<>();
        pw.add(upper.charAt(random.nextInt(upper.length())));
        pw.add(lower.charAt(random.nextInt(lower.length())));
        pw.add(number.charAt(random.nextInt(number.length())));
        pw.add(symbol.charAt(random.nextInt(symbol.length())));
        String all = upper + lower + number + symbol;
        for (int i = 4; i < 12; i++) {
            pw.add(all.charAt(random.nextInt(all.length())));
        }
        StringBuilder builder = new StringBuilder();
        for (char c : pw) {
            builder.append(c);
        }
        String password = builder.toString();
        return password;
    }
}
