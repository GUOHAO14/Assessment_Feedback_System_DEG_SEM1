/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.roles;

/**
 *
 * @author Khoo Guo Hao
 */
public class StudentScore {
    private final Assessment assessment;
    private String finalScore;
    private String orginalScore;
    private String originalFullMarks;
    private String feedback;
    
    public StudentScore(Assessment assessment, String finalScore, String originalScore, String originalFullMarks, String feedback) {
        this.assessment = assessment;
        this.finalScore = finalScore;
        this.orginalScore = originalScore;
        this.originalFullMarks = originalFullMarks;
        this.feedback = feedback;
    }
    
    public StudentScore(StudentScore other) {
        this.assessment = other.assessment;
        this.finalScore = other.finalScore;
        this.orginalScore = other.orginalScore;
        this.originalFullMarks = other.originalFullMarks;
        this.feedback = other.feedback;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getOrginalScore() {
        return orginalScore;
    }

    public void setOrginalScore(String orginalScore) {
        this.orginalScore = orginalScore;
    }

    public String getOriginalFullMarks() {
        return originalFullMarks;
    }

    public void setOirginalFullMarks(String originalFullMarks) {
        this.originalFullMarks = originalFullMarks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
