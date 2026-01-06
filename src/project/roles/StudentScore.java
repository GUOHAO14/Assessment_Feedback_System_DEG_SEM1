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
    private Assessment assessment;
    private String finalScore;
    private String orginalScore;
    private String orginalfullMarks;
    
    public StudentScore(Assessment assessment, String finalScore, String originalScore, String originalFullMarks) {
        this.assessment = assessment;
        this.finalScore = finalScore;
        this.orginalScore = orginalScore;
        this.orginalfullMarks = originalFullMarks;
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

    public String getOrginalfullMarks() {
        return orginalfullMarks;
    }

    public void setOrginalfullMarks(String orginalfullMarks) {
        this.orginalfullMarks = orginalfullMarks;
    }
}
