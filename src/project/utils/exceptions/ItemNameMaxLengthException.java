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
public class ItemNameMaxLengthException extends Exception {

    public ItemNameMaxLengthException() {
        super("Item name must not exceed "+Constants.ITEM_NAME_MAX_LENGTH+" characters.");
    }

    public ItemNameMaxLengthException(String item) {
        super(item+" name must not exceed "+Constants.ITEM_NAME_MAX_LENGTH+" characters.");
    }
}
