/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.gui.admin;

import project.utils.*;

/**
 *
 * @author User
 */
public class testmain {
    public static void main(String[]args) {
        InteractTxt.initDatabase();
        new Dashboard().setVisible(true);
    }
}
