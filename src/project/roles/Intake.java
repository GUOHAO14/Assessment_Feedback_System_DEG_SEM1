package project.roles;

import java.util.ArrayList;

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
    
    
}
