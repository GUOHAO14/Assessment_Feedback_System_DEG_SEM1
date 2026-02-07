package project.roles;

import java.util.ArrayList;

public class Leader extends User {

    public ArrayList<Lecturer> leaderTeam = new ArrayList<Lecturer>();
    public ArrayList<Module> Lea_Modules = new ArrayList<Module>();

    public Leader(String[] userData) {
        super(userData);
    }

    public String[] getFullLeaderData() {
        String[] fullData = {getId(), getName(), getEmail(), getPW(), getRole()};
        return fullData;
    }

    public void printFullLeaderData() {
        String[] fullData = {getId(), getName(), getEmail(), getPW(), getRole()};
        System.out.println(String.join(", ", fullData));
    }
    
    public String getLecturersAsString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < leaderTeam.size(); i++) {
            sb.append(leaderTeam.get(i).getId());

            if (i < leaderTeam.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public String getModulesAsString() {
    StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Lea_Modules.size(); i++) {
            sb.append(Lea_Modules.get(i).getModuleId());

            if (i < Lea_Modules.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
