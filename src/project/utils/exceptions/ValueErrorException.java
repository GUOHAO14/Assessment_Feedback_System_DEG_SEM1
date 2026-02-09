/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils.exceptions;

/**
 *
 * @author Khoo Guo Hao
 */
public class ValueErrorException extends Exception {

    public ValueErrorException() {
        super("Value Error.");
    }

    public ValueErrorException(String value) {
        super("Value Error - value must be "+value+".");
    }
    
    public ValueErrorException(String value, String field) {
        super("Value Error - "+field+" value must be "+value+".");
    }
}
