package project.roles;

import java.util.ArrayList;

public class Module {

    private String moduleId, moduleName;
    private Leader leader;

    public ArrayList<Lecturer> Mod_Lecturers = new ArrayList<Lecturer>();
    

    public Module(String moduleId, String moduleName, Leader leader) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.leader = leader;
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
}