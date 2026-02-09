package project.utils;

import java.awt.event.*;
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
    
    public static void enableAdvancedTooltip(JTable table) {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row < 0 || col < 0) {
                    table.setToolTipText(null);
                    return;
                }

                Object cell = table.getValueAt(row, col);
                if (cell == null) {
                    table.setToolTipText(null);
                    return;
                }

                String tooltip = null;

                switch (col) {
                    case 2: { // Lecturer ID
                        String lecId = cell.toString();
                        if (!lecId.equals("NA")) {
                            tooltip = InteractTxt.checkLecID(lecId).getName();
                        }
                        break;
                    }
                    case 3: { // Intake Module ID
                        String imId = cell.toString();
                        tooltip = InteractTxt.checkIMID(imId).getIMName();
                        break;
                    }
                    case 4: { // Students
                        tooltip = cell.toString();
                        break;
                    }
                }

                table.setToolTipText(tooltip);
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
    
    public static void showLeaName(JTable table, Set<Integer> columns) {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row > -1 && columns.contains(col)) {
                    String LeaId = (table.getValueAt(row, col)).toString();
                    String value = InteractTxt.checkLeaID(LeaId).getName();
                    table.setToolTipText(value == null ? null : value);
                } else {
                    table.setToolTipText(null);
                }
            }
        });
    }
    
    public static void showIntakeName(JTable table, Set<Integer> columns) {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row > -1 && columns.contains(col)) {
                    String IntakeId = (table.getValueAt(row, col)).toString();
                    String value = InteractTxt.checkIntID(IntakeId).getIntakeName();
                    table.setToolTipText(value == null ? null : value);
                } else {
                    table.setToolTipText(null);
                }
            }
        });
    }
}
