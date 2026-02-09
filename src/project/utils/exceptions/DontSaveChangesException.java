/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils.exceptions;

/**
 *
 * @author Khoo Guo Hao
 */
public class DontSaveChangesException extends Exception {
    public DontSaveChangesException() {
        super("No changes are made.");
    }
}
