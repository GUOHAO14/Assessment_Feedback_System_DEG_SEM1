package project.roles;

import java.util.ArrayList;

public class Leader extends User {  
    
    public ArrayList<Lecturer> leaderTeam = new ArrayList<Lecturer>();
    public ArrayList<Module> Lead_Modules = new ArrayList<Module>();
            
    public Leader (String[] userData) {
        super(userData);
    }
    
    public String[] getFullLeaderData() {
        // returns leader id instead of leader object
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole()};
        return fullData;
    }
    
    public void printFullLeaderData() {
        // returns leader id instead of leader object
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole()};
        System.out.println(String.join(", ", fullData));
    }
    
}
