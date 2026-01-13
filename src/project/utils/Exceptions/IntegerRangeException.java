/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils.exceptions;

/**
 *
 * @author Khoo Guo Hao
 */
public class IntegerRangeException extends Exception {
    
    public IntegerRangeException() {
        super("Item value is out of range.");
    }
    
    public IntegerRangeException(String num1, String num2) {
        super("Item value must be ranged between "+num1+" and "+num2+".");
    }

    public IntegerRangeException(String item, String num1, String num2) {
        super(item+" value must be ranged between "+num1+" and "+num2+".");
    }
    
    public IntegerRangeException(int num1, int num2) {
        super("Item value must be ranged between "+num1+" and "+num2+".");
    }

    public IntegerRangeException(String item, int num1, int num2) {
        super(item+" value must be ranged between "+num1+" and "+num2+".");
    }
}
