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
    
    public static String getNewIMID() {
        int max = 0;
        for(IntakeModule x : InteractTxt.allIntakeModule){
            String numPart = x.getIMID().substring(4);
            int num = Integer.parseInt(numPart);
            if (num > max) {
                max = num;
            }
        }
        String IMID = "IMID" + (max + 1);
        return IMID;
    }
    
    public boolean checkEmptyStu() {
        boolean emptyStu = true;
        for(Student x : InteractTxt.allStudent){
            if(x.getIntakeId().equals(this.getIntakeId())){
                emptyStu = false;
            }
        }
        return emptyStu;
    }
    
    public void createIntakeModule(String y) {
        String IMID = Intake.getNewIMID();
        InteractTxt.allIntakeModule.add(new IntakeModule(IMID, this.getIntakeId(), y));

        String ClassId = project.roles.Class.getNewClaID();
        InteractTxt.allClass.add(new project.roles.Class(ClassId, "Default Class", "NA", IMID));
        InteractTxt.checkIMID(IMID).IM_Classes.add(InteractTxt.checkClassID(ClassId));
    }
    
    public void deleteIntakeModule(Module x) {
        IntakeModule IMID = InteractTxt.checkIMID(this.getIntakeId(), x.getModuleId());
        InteractTxt.allIntakeModule.remove(IMID);
        for(project.roles.Class a : IMID.IM_Classes){
            InteractTxt.allClass.remove(a);
            if(!a.getLecId().equals("NA")){
                InteractTxt.checkLecID(a.getLecId()).Lec_Classes.remove(a);
            }                                
        }
        for(Assessment b : IMID.IM_Assessments){
            InteractTxt.allAssessment.remove(b);
        }
        IMID.IM_Classes.clear();
        IMID.IM_Assessments.clear();
    }
    
    @Override
    public String toString() {
        return getIntakeId() + " (" + getIntakeName() + ")";
    }
    
}
