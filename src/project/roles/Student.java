package project.roles;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import project.utils.InteractTxt;
import project.utils.exceptions.IntegerRangeException;

public class Student extends User {
    private String intakeId, dob;
    public ArrayList<Class> Stu_Classes = new ArrayList<>();
    public ArrayList<StudentScore> Stu_Scores = new ArrayList<>();
    public ArrayList<StudentGradeAndComment>GradesAndComments = new ArrayList<>();
    
    public Student(String[] userData) {
        super(userData);
    }

    public Student(String[] userData, String[] studentData) {
        super(userData);
        this.intakeId = studentData[1];
        this.dob = studentData[2];
    }

    public void setIntakeId(String intakeId) {
        this.intakeId = intakeId;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public String getIntakeId() {
        return this.intakeId;
    }
    
    public String getDob() {
        return this.dob;
    }
    
    public String[] getStudentData() {
        String [] stuData = {getId(), intakeId, dob};
        return stuData;
    }
    
    public String[] getFullStudentData() {
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole(), intakeId, dob};
        return fullData;
    }
    
    @Override
    public void setDetails(String[] fullData) {
        super.setDetails(fullData);
        
        this.intakeId = fullData[4];
        this.dob = fullData[5];
    }
    
    // overloading
    public void setDetails(String[] userData, String[] studentData) {
        super.setDetails(userData);
        
        this.intakeId = studentData[1];
        this.dob = studentData[2];
    }
    
    public String calcStuScore(IntakeModule im) {
        ArrayList<String> assIds = new ArrayList<>();
        int score = 0;
        int count = 0;
        
        im.IM_Assessments.forEach(i -> {
           assIds.add(i.getAssId());
        });
        
        for (StudentScore ss : this.Stu_Scores) {
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
    
    public String calcStuGrade(String input) {
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
    
    
    public String getSpecificGrade(project.roles.Class c) {
        String grade = "NA";
        for (StudentGradeAndComment gc : this.GradesAndComments) {
            if (c.getClassId().equals(gc.getStuClass().getClassId())) {
                grade = gc.getGrade();
            }
        }
        return grade;
    }
    
    public void setSpecificGrade(project.roles.Class c, String grade) {
        for (StudentGradeAndComment gc : this.GradesAndComments) {
            if (c.getClassId().equals(gc.getStuClass().getClassId())) {
                gc.setGrade(grade);
            }
        }
    }
    
    public String getSpecificComment(project.roles.Class c) {
        String grade = "NA";
        for (StudentGradeAndComment gc : this.GradesAndComments) {
            if (c.getClassId().equals(gc.getStuClass().getClassId())) {
                grade = gc.getComment();
            }
        }
        return grade;
    }
    
    public static ArrayList<Student> search(String input){
        input = input.toLowerCase();
        ArrayList<Student> matched = new ArrayList<Student>();
        for(Student x : InteractTxt.allStudent){
            if(x.getId().toLowerCase().contains(input) || x.getName().toLowerCase().contains(input)){
                matched.add(x);
            }
        }
        return matched;
    }
}
