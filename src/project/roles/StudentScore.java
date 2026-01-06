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
    private String score;
    private String fullMarks;
    
    public StudentScore(Assessment assessment, String score, String fullMarks) {
        this.assessment = assessment;
        this.score = score;
        this.fullMarks = fullMarks;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(String fullMarks) {
        this.fullMarks = fullMarks;
    }
}
