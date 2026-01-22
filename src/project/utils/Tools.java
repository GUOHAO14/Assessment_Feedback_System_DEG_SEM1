package project.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
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
    
    public static String GeneratePW(){
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String number = "0123456789";
        String symbol = "!@#$%^&*()-_=+[]{};:,.<>?/";
        SecureRandom random = new SecureRandom();
        ArrayList<Character> pw = new ArrayList<>();
        pw.add(upper.charAt(random.nextInt(upper.length())));
        pw.add(lower.charAt(random.nextInt(lower.length())));
        pw.add(number.charAt(random.nextInt(number.length())));
        pw.add(symbol.charAt(random.nextInt(symbol.length())));
        String all = upper + lower + number + symbol;
        for (int i = 4; i < 12; i++) {
            pw.add(all.charAt(random.nextInt(all.length())));
        }
        StringBuilder builder = new StringBuilder();
        for (char c : pw) {
            builder.append(c);
        }
        String password = builder.toString();
        return password;
    }
}
