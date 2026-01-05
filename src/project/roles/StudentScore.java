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
    private Student student;
    private Assessment assessment;
    private String score;
    
    public StudentScore(Assessment assessment, String score) {
        this.assessment = assessment;
        this.score = score;
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
}
