package project.utils;

import java.awt.event.*;
import java.security.SecureRandom;
import java.util.*;
import javax.swing.*;
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
    
    public static void enableTooltip(JTable table, Set<Integer> columns) {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row > -1 && columns.contains(col)) {
                    Object value = table.getValueAt(row, col);
                    table.setToolTipText(value == null ? null : value.toString());
                } else {
                    table.setToolTipText(null);
                }
            }
        });
    }
    
    public static void setAllAssStuGrades(IntakeModule im, String grade) {
        for (project.roles.Class c : im.IM_Classes) {
            Iterator<Student> stuIterator = c.Class_Students.iterator();

            while (stuIterator.hasNext()) {
                Student student = stuIterator.next();
                student.setSpecificGrade(c, grade);
            }
        }
    }
    
    public static void deleteAllAssStuScores(Assessment a) {
        IntakeModule im = a.getAssIM();
        
        for (project.roles.Class c : im.IM_Classes) {
            for (Student s : c.Class_Students) {
                Iterator<StudentScore> ssIterator = s.Stu_Scores.iterator();

                while (ssIterator.hasNext()) {
                    StudentScore score = ssIterator.next();
                    if (score.getAssessment().getAssId().equals(a.getAssId())) {
                        ssIterator.remove(); 
                    }
                }
            }
        }
    }
}
