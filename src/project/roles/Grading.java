package project.roles;


public class Grading {
    private String Grade, MarksFrom, MarksTo;
    
    public Grading(String Grade, String MarksFrom, String MarksTo) {
        this.Grade = Grade;
        this.MarksFrom = MarksFrom;
        this.MarksTo = MarksTo;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String Grade) {
        this.Grade = Grade;
    }

    public String getMarksFrom() {
        return MarksFrom;
    }

    public void setMarksFrom(String MarksFrom) {
        this.MarksFrom = MarksFrom;
    }

    public String getMarksTo() {
        return MarksTo;
    }

    public void setMarksTo(String MarksTo) {
        this.MarksTo = MarksTo;
    }
    
}
