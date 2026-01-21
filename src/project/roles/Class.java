package project.roles;

import java.util.ArrayList;

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
}
