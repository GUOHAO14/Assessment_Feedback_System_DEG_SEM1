/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.roles;

/**
 *
 * @author Khoo Guo Hao
 */
public class StudentGradeAndComment {

    private Class stuClass;
    private String grade;
    private String comment;
    
    public StudentGradeAndComment(Class stuClass, String grade, String comment) {
        this.stuClass = stuClass;
        this.grade = grade;
        this.comment = comment;
    }

    public Class getStuClass() {
        return stuClass;
    }

    public void setStuClass(Class stuClass) {
        this.stuClass = stuClass;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
}
