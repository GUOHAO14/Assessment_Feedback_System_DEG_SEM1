package project.roles;

import java.util.ArrayList;

public class Student extends User {
    private String intakeId, dob;
    public ArrayList<Class> Stu_Classes = new ArrayList<>();
    public ArrayList<StudentScore> Stu_Scores = new ArrayList<>();
    
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
}
