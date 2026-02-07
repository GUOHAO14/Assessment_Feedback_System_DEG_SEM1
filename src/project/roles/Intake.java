package project.roles;

import java.util.ArrayList;
import project.utils.*;

public class Intake {
    private String intakeId, intakeName;
    
    public Intake(String intakeId, String intakeName) {
        this.intakeId = intakeId;
        this.intakeName = intakeName;
    }

    public String getIntakeId() {
        return intakeId;
    }

    public void setIntakeId(String intakeId) {
        this.intakeId = intakeId;
    }

    public String getIntakeName() {
        return intakeName;
    }

    public void setIntakeName(String intakeName) {
        this.intakeName = intakeName;
    }
    
    public String getModulesAsString() {
        ArrayList<Module> modules = InteractTxt.checkInt_Modules(this.getIntakeId());

        if (modules == null || modules.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modules.size(); i++) {
            sb.append(modules.get(i).getModuleId());
            if (i < modules.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
}
