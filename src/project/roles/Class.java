package project.roles;

import java.util.*;
import project.utils.*;

public class Class {
    private String classId, className, lecId, IMID;
    public ArrayList<Student> Class_Students = new ArrayList<Student>();
    
    public Class(String classId, String className, String lecId, String IMID) {
        this.classId = classId;
        this.className = className;
        this.lecId = lecId;
        this.IMID = IMID;
    }

    public String getIMID() {
        return IMID;
    }

    public void setIMID(String IMID) {
        this.IMID = IMID;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLecId() {
        return lecId;
    }

    public void setLecId(String lecId) {
        this.lecId = lecId;
    }
    
    public String getStudentsAsString() {
        if (Class_Students == null || Class_Students.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Class_Students.size(); i++) {
            sb.append(Class_Students.get(i).getId());

            if (i < Class_Students.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public static String getNewClaID() {
        int max = 0;
        for(project.roles.Class x : InteractTxt.allClass){
            String numPart = x.getClassId().substring(1);
            int num = Integer.parseInt(numPart);
            if (num > max) {
                max = num;
            }
        }
        String ClassId = "C" + (max + 1);
        return ClassId;
    }
    
    public boolean checkDelete() {
        boolean delete = true;
        if (InteractTxt.checkIMID(this.getIMID()).IM_Classes.size() <= 1) {
            delete = false;
        } else {
            outer:
            for(Student y : this.Class_Students){
                for(StudentScore z : y.Stu_Scores){
                    if(InteractTxt.checkIMID(this.getIMID()).IM_Assessments.contains(z.getAssessment())){
                        delete = false;
                        break outer;
                    }
                }
            }
        }
        return delete;
    }
}
