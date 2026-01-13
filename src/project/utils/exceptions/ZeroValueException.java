/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils.exceptions;
/**
 *
 * @author Khoo Guo Hao
 */
public class ZeroValueException extends Exception {
    
    public ZeroValueException() {
        super("Item value should not be 0.");
    }

    public ZeroValueException(String item) {
        super(item+" value should not be 0.");
    }
}
