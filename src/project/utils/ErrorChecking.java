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
            
            System.out.println("IM Assessment array size: "+im.IM_Assessments.size());
            
            if (im.IM_Assessments.size() == 0) continue;
            
            for (Assessment a : im.IM_Assessments) {
                totalPercent += Integer.parseInt(a.getAssPercentage());
                System.out.println("Ass id: "+a.getAssId());
                System.out.println("Percent: "+a.getAssPercentage());
            }
            
            System.out.println("Total Percent: "+totalPercent);
            
            if (totalPercent != 100) {
                System.out.println("wtf");
                return false;
            }
        }
        System.out.println("Lol");
        return true;
    }
    
    public static void checkEmptyLine() {
        
    }
}
