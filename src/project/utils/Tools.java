package project.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import project.utils.exceptions.IntegerRangeException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import project.roles.*;

public class Tools {
    public static void logout(FrameFormat yourPage, User sessionUser) {
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
                    writer.write("Application logged out by "+sessionUser.getId()+" at " + new java.util.Date() + "\n");
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
    public static User checkCredentials(ArrayList<? extends User> userCred, String emailInput, String passwordInput) {
        //in progress
        for (User user : userCred) {
            String emailCred = user.getEmail();
            String pwCred = user.getPW();
            
            if (emailCred.equals(emailInput) && pwCred.equals(passwordInput)) {
                return user;
            }
        }
        return null;
    }
    
    public static String calcStuScore(IntakeModule im, Student stu) {
        ArrayList<String> assIds = new ArrayList<>();
        int score = 0;
        int count = 0;
        
        im.IM_Assessments.forEach(i -> {
           assIds.add(i.getAssId());
        });
        
        for (StudentScore ss : stu.Stu_Scores) {
            String assId = ss.getAssessment().getAssId();
           
            if (assIds.contains(assId)) {
                score += Float.parseFloat(ss.getFinalScore());
                count++;
            }
        }
        
        System.out.println(count);
        System.out.println(assIds.size());
        
        if (assIds.isEmpty()) return "NA";
        if (count != assIds.size()) return "Incomp.";
        else return String.valueOf(score);
    }
    
    public static String calcStuGrade(String input) {
        String grade = null;
        try {
            if (input.equals("NA") || input.equals("NA")) return input;
            
            float num = Float.parseFloat(input);
            
            if (num < 0 || num > 100) {
                throw new IntegerRangeException("Score input", 1, 100);
            } else {
                
                System.out.println(num);
                for (Grading g : InteractTxt.allGrading) {
                    System.out.println(g.getMarksFrom()+"-"+g.getMarksTo());
                    if (num >= Float.parseFloat(g.getMarksFrom()) && num <= Float.parseFloat(g.getMarksTo())) {
                        System.out.println("hit");
                        grade = g.getGrade();
                    }
                }
            }
        } catch (NumberFormatException e) {
            grade = "NA";
            System.out.println("calcStuGPA: "+grade);
        } catch (IntegerRangeException e) {
            grade = "NA";
            System.out.println("calcStuGPA: "+grade);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GPA calculation failed.\nReport this error.", "Error - Unknown Error", 0);
            grade = "NA";
        }
        return grade;
    }
    
    public static String getSpecificGrade(project.roles.Class c, Student s) {
        String grade = "NA";
        for (StudentGradeAndComment gc : s.GradesAndComments) {
            if (c.getClassId().equals(gc.getStuClass().getClassId())) {
                grade = gc.getGrade();
            }
        }
        return grade;
    }
    
    public static String getSpecificComment(project.roles.Class c, Student s) {
        String grade = "NA";
        for (StudentGradeAndComment gc : s.GradesAndComments) {
            if (c.getClassId().equals(gc.getStuClass().getClassId())) {
                grade = gc.getComment();
            }
        }
        return grade;
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
