package project.utils;

import project.utils.exceptions.IntegerRangeException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import project.roles.*;

public class Tools {
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
        ArrayList<Assessment> assessments = new ArrayList<>();
        ArrayList<String> assIds = new ArrayList<>();
        int score = 0;
        int count = 0;
        
        im.IM_Assessments.forEach(i -> {
           assIds.add(i.getAssId());
        });
        
        for (StudentScore ss : stu.Stu_Scores) {
            String assId = ss.getAssessment().getAssId();
           
            if (assIds.contains(assId)) {
                score += Integer.parseInt(ss.getFinalScore());
                count++;
            }
        }
        
        if (assIds.size() == 0) return "NA";
        if (count != assIds.size()) return "Incomp.";
        else return String.valueOf(score);
    }
    
    public static String calcStuGPA(String input) {
        String grade = null;
        try {
            if (input.equals("NA") || input.equals("NA")) return input;
            
            int num = Integer.parseInt(input);
            
            if (num < 0 || num > 100) {
                throw new IntegerRangeException("Score input", 1, 100);
            } else {
                
                if (num >= 0 && num < 20) grade = "F-";
                else if (num >= 20 && num < 30) grade = "F";
                else if (num >= 30 && num < 40) grade = "F+";
                else if (num >= 40 && num < 50) grade = "D";
                else if (num >= 50 && num < 55) grade = "C-";
                else if (num >= 55 && num < 60) grade = "C";
                else if (num >= 60 && num < 65) grade = "C+";
                else if (num >= 65 && num < 70) grade = "B-";
                else if (num >= 70 && num < 75) grade = "B";
                else if (num >= 75 && num < 80) grade = "B+";
                else if (num >= 80 && num < 85) grade = "A-";
                else if (num >= 85 && num < 90) grade = "A";
                else if (num >= 90 && num <= 100) grade = "A+";
            }
            
        } catch (NumberFormatException e) {
            grade = "Number format exception";
            System.out.println("calcStuGPA: "+grade);
        } catch (IntegerRangeException e) {
            grade = "Integer range exception";
            System.out.println("calcStuGPA: "+grade);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GPA calculation failed.\nReport this error.", "Error - Unknown Error", 0);
            grade = "General exception";
        }
        return grade;
    }
}
