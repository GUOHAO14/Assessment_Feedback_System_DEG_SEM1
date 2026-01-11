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
}
