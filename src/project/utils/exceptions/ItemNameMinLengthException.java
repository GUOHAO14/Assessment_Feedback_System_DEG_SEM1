/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils.exceptions;

import project.utils.Constants;
/**
 *
 * @author Khoo Guo Hao
 */
public class ItemNameMinLengthException extends Exception {
    public ItemNameMinLengthException() {
        super("Item name must be longer than "+Constants.ITEM_NAME_MIN_LENGTH+" characters.");
    }

    public ItemNameMinLengthException(String item) {
        super(item+" name must be longer than "+Constants.ITEM_NAME_MIN_LENGTH+" characters.");
    }
}
