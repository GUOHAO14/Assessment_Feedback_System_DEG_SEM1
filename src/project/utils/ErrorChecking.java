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
}
