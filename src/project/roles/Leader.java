package project.roles;

import java.util.ArrayList;

public class Leader extends User {  
    
    public ArrayList<Lecturer> leaderTeam = new ArrayList<Lecturer>();
    public ArrayList<Module> Lea_Modules = new ArrayList<Module>();
            
    public Leader (String[] userData) {
        super(userData);
    }
    
    public String[] getFullLeaderData() {
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole()};
        return fullData;
    }
    
    public void printFullLeaderData() {
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole()};
        System.out.println(String.join(", ", fullData));
    }
    
}
