package project.utils;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import project.roles.*;

public class Tools {
   
    public static User checkCredentials(ArrayList<? extends User> userCred, String emailInput, String passwordInput) {
        //in progress
        for (User user : userCred) {
            String emailCred = user.getEmail();
            String pwCred = user.getPW();
            
            if (emailCred.equals(emailInput) && pwCred.equals(passwordInput)) {
                return user;
            }
        }
        return null;
    }
    
    public static void enableTooltip(JTable table, Set<Integer> columns) {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row > -1 && columns.contains(col)) {
                    Object value = table.getValueAt(row, col);
                    table.setToolTipText(value == null ? null : value.toString());
                } else {
                    table.setToolTipText(null);
                }
            }
        });
    }
}
