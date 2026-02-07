/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils;

import project.roles.*;

/**
 *
 * @author Khoo Guo Hao
 */
public class ErrorChecking {
    
    public static boolean checkIM_Assessments() {
        
        for (IntakeModule im : InteractTxt.allIntakeModule) {
            int totalPercent = 0;
            if (im.IM_Assessments.isEmpty()) continue;
            for (Assessment a : im.IM_Assessments) {
                totalPercent += Integer.parseInt(a.getAssPercentage());
            }
            if (totalPercent != 100) {
                return false;
            }
        }
        return true;
    }
    
    public static void checkEmptyLine() {
        
    }
    
    public static ValidationResult checkInput(String input) {
        if (input == null || input.isEmpty()) {
            return ValidationResult.EMPTY;
        }

        try {
            Integer.parseInt(input);
            return ValidationResult.NUMBER;
        } catch (NumberFormatException e) {
            return ValidationResult.OK;
        }
    }
    
    public static ValidationResult checkEmail(String email) {
        if (email == null || email.isEmpty()) {
            return ValidationResult.EMPTY;
        }

        if (!email.contains("@")) {
            return ValidationResult.INVALID;
        }

        if (emailExists(email)) {
            return ValidationResult.EXISTS;
        }

        return ValidationResult.OK;
    }

    private static boolean emailExists(String email) {
        for (Student x : InteractTxt.allStudent)
            if (email.equals(x.getEmail())) return true;

        for (Lecturer x : InteractTxt.allLecturer)
            if (email.equals(x.getEmail())) return true;

        for (Leader x : InteractTxt.allLeader)
            if (email.equals(x.getEmail())) return true;

        for (Admin x : InteractTxt.allAdmin)
            if (email.equals(x.getEmail())) return true;

        return false;
    }
    
    public static ValidationResult checkID(String input) {
        if (input == null || input.isEmpty()) {
            return ValidationResult.EMPTY;
        }
        
        for (Intake x : InteractTxt.allIntake){
            if(input.equals(x.getIntakeId())){
                return ValidationResult.EXISTS;
            }
        }
        
        return ValidationResult.OK;
    }
}
