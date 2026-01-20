package project.roles;

import java.util.ArrayList;

public class Leader extends User {  
    
    public ArrayList<Lecturer> leaderTeam = new ArrayList<Lecturer>();
//    public ArrayList<String> createdModules = new ArrayList<>();
        
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
      public void printLeaderTeam() {
    if (leaderTeam.isEmpty()) {
        System.out.println("Leader team is empty.");
        return;
    }

    System.out.println("Leader Team Members:");
    for (Lecturer l : leaderTeam) {
        System.out.println(l.getId() + " - " + l.getName());
    }
    
}
//      public void addCreatedModule(String moduleId){
//    if(!createdModules.contains(moduleId)){
//        createdModules.add(moduleId);
//    }
}


