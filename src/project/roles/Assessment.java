package project.roles;

import project.utils.InteractTxt;

public class Assessment {
    private String assId, assName, assType, assPercentage, assFullMarks;
    private IntakeModule assIM;
    
    public Assessment(String assId, String assName, String assType, String assPercentage, String assFullMarks, IntakeModule assIM) {
        this.assId = assId;
        this.assName = assName;
        this.assType = assType;
        this.assPercentage = assPercentage;
        this.assFullMarks = assFullMarks;
        this.assIM = assIM;
    }
    
    public Assessment(Assessment ass) {
        this.assId = ass.getAssId();
        this.assName = ass.getAssName();
        this.assType = ass.getAssType();
        this.assPercentage = ass.getAssPercentage();
        this.assFullMarks = ass.getAssFullMarks();
        this.assIM = ass.getAssIM();
    }

    public String getAssId() {
        return assId;
    }

    public void setAssId(String assId) {
        this.assId = assId;
    }

    public String getAssName() {
        return assName;
    }

    public void setAssName(String assName) {
        this.assName = assName;
    }

    public String getAssType() {
        return assType;
    }

    public void setAssType(String assType) {
        this.assType = assType;
    }

    public String getAssPercentage() {
        return assPercentage;
    }

    public void setAssPercentage(String assPercentage) {
        this.assPercentage = assPercentage;
    }

    public IntakeModule getAssIM() {
        return assIM;
    }

    public void setAssIM(IntakeModule assIM) {
        this.assIM = assIM;
    }

    public String getAssFullMarks() {
        return assFullMarks;
    }

    public void setAssFullMarks(String assFullMarks) {
        this.assFullMarks = assFullMarks;
    }
    
    public void getAllAssDetails() {
        System.out.println(getAssName() + ", " + getAssId() + ", " + getAssType() + ", " + getAssPercentage() + ", " + getAssFullMarks() + ", " + getAssIM().getIMID());
    }
    
    public static String getNewAssID() {
        int max = 0;
        for(Assessment x : InteractTxt.allAssessment){
            String numPart = x.getAssId().substring(3);
            int num = Integer.parseInt(numPart);
            if (num > max) {
                max = num;
            }
        }
        String id = "Ass" + (max + 1);
        return id;
    }
}
