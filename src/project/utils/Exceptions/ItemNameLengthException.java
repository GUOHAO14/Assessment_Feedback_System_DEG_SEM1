/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.utils.Exceptions;

import project.utils.Constants;
/**
 *
 * @author Khoo Guo Hao
 */
public class ItemNameLengthException extends Exception {

    public ItemNameLengthException() {
        super("Item name should not exceed "+Constants.ITEM_NAME_LENGTH+" characters.");
    }

    public ItemNameLengthException(String item) {
        super(item+" name should not exceed "+Constants.ITEM_NAME_LENGTH+" characters.");
    }
}
