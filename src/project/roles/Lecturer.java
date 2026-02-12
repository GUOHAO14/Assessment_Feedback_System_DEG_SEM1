package project.roles;

import java.util.ArrayList;
import project.utils.*;

public class Lecturer extends User {
    private Leader leader;
    public ArrayList<Module> Lec_Modules = new ArrayList<Module>();
    public ArrayList<Class> Lec_Classes = new ArrayList<Class>();
    
    public Lecturer (String[] userData) {
        super(userData);
    }
    
    public Leader getLeader() {
        return this.leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }
    
    public String[] getLecturerData() {
        // returns leader id instead of leader object
        String [] lecData = {getId(), leader.getId()};
        return lecData;
    }
    
    public String[] getFullLecturerData() {
        // returns leader id instead of leader object
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole(), leader.getId()};
        return fullData;
    }
    
    public void printFullLecturerData() {
        // returns leader id instead of leader object
        String [] fullData = {getId(), getName(), getEmail(), getPW(), getRole(), leader.getId()};
        System.out.println(String.join(", ", fullData));
    }
    
    public static ArrayList<Lecturer> search(String input){
        input = input.toLowerCase();
        ArrayList<Lecturer> matched = new ArrayList<Lecturer>();
        for(Lecturer x : InteractTxt.allLecturer){
            if(x.getId().toLowerCase().contains(input) || x.getName().toLowerCase().contains(input)){
                matched.add(x);
            }
        }
        return matched;
    }
}
