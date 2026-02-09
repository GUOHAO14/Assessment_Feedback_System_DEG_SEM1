package project.roles;

import java.util.ArrayList;
import project.utils.*;

public class IntakeModule {
    private String IMID, intakeId, moduleId;
    public ArrayList<Class> IM_Classes = new ArrayList<Class>();
    public ArrayList<Assessment> IM_Assessments = new ArrayList<Assessment>();
    
    public IntakeModule(String IMID, String intakeId, String moduleId) {
        this.IMID = IMID;
        this.intakeId = intakeId;
        this.moduleId = moduleId;
    }

    public String getIMID() {
        return IMID;
    }

    public void setIMID(String IMID) {
        this.IMID = IMID;
    }

    public String getIntakeId() {
        return intakeId;
    }

    public void setIntakeId(String intakeId) {
        this.intakeId = intakeId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    
    public void getAllIMDetails() {
        System.out.println(getIMID() + ", " + getIntakeId() + ", " + getModuleId());
    }
    
    @Override
    public String toString() {
        return getIMID() + " (" + InteractTxt.checkIntID(getIntakeId()).getIntakeName() + " | " + InteractTxt.checkModID(getModuleId()).getModuleName() + ")";
    }
}
