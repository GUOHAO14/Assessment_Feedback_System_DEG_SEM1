package project.utils;

import java.io.File;
import project.roles.*;
import project.roles.Class;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class InteractTxt {
    public static ArrayList<Student> allStudent = new ArrayList<Student>();
    public static ArrayList<Leader> allLeader = new ArrayList<Leader>();
    public static ArrayList<Lecturer> allLecturer = new ArrayList<Lecturer>();
    public static ArrayList<Admin> allAdmin = new ArrayList<Admin>();
    
    public static void writeUser(){
        try{
            PrintWriter a = new PrintWriter("src/resources/user.txt");
            for(Admin x : allAdmin){
                a.println(x.getId());
                a.println(x.getName());
                a.println(x.getEmail().toLowerCase());
                a.println(x.getPW());
                a.println(x.getRole());
                a.println();
            }
            for(Leader x : allLeader){
                a.println(x.getId());
                a.println(x.getName());
                a.println(x.getEmail().toLowerCase());
                a.println(x.getPW());
                a.println(x.getRole());
                a.println();
            }
            for(Lecturer x : allLecturer){
                a.println(x.getId());
                a.println(x.getName());
                a.println(x.getEmail().toLowerCase());
                a.println(x.getPW());
                a.println(x.getRole());
                a.println();
            }
            for(Student x : allStudent){
                a.println(x.getId());
                a.println(x.getName());
                a.println(x.getEmail().toLowerCase());
                a.println(x.getPW());
                a.println(x.getRole());
                a.println();
            }
            a.close();
            PrintWriter b = new PrintWriter("src/resources/leader.txt");
            for(Leader x : allLeader){
                b.println(x.getId());
                b.println();
            }
            b.close();
            PrintWriter c = new PrintWriter("src/resources/lecturer.txt");
            for(Lecturer x : allLecturer){
                c.println(x.getId());
                c.println(x.getLeader().getId());
                c.println();
            }
            c.close();
            PrintWriter d = new PrintWriter("src/resources/student.txt");
            for(Student x : allStudent){
                d.println(x.getId());
                d.println(x.getIntakeId());
                d.println(x.getDob());
                d.println();
            }
            d.close();
        }catch(Exception e){
            System.out.println("Error in write ..........");
            e.printStackTrace();
        }
    }

    public static void readUser(){
        try{
            Scanner s1 = new Scanner(new File("src/resources/user.txt"));
            while(s1.hasNext()){
                String id = s1.nextLine();
                String name = s1.nextLine();
                String email = s1.nextLine();
                String password = s1.nextLine();
                String role = s1.nextLine();
                s1.nextLine();
                String [] userdata = {id,name,email,password,role};
                switch (role) {
                    case "student" -> allStudent.add(new Student(userdata));
                    case "lecturer" -> allLecturer.add(new Lecturer(userdata));
                    case "leader" -> allLeader.add(new Leader(userdata));
                    case "admin" -> allAdmin.add(new Admin(userdata));
                }
            }
            Scanner s2 = new Scanner(new File("src/resources/student.txt"));
            while(s2.hasNext()){
                String id = s2.nextLine();
                String intakeid = s2.nextLine();
                String dob = s2.nextLine();
                s2.nextLine();
                InteractTxt.checkStuID(id).setIntakeId(intakeid);
                InteractTxt.checkStuID(id).setDob(dob);
            }
            Scanner s3 = new Scanner(new File("src/resources/lecturer.txt"));
            while(s3.hasNext()){
                String id = s3.nextLine();
                String leaderid = s3.nextLine();
                s3.nextLine();
                InteractTxt.checkLecID(id).setLeader(checkLeaID(leaderid));
                InteractTxt.checkLeaID(leaderid).leaderTeam.add(checkLecID(id));
            }
        }catch(Exception e){
            System.out.println("Error in read ..........");
            e.printStackTrace();
        }
    }
    
    public static Student checkStuID(String id){
        for(Student x : allStudent){
            if(id.equals(x.getId())){
                return x;
            }
        }
        return null;
    }
    
    public static Lecturer checkLecID(String id){
        for(Lecturer x : allLecturer){
            if(id.equals(x.getId())){
                return x;
            }
        }
        return null;
    }
    
    public static Leader checkLeaID(String id){
        for(Leader x : allLeader){
            if(id.equals(x.getId())){
                return x;
            }
        }
        return null;
    }
    
    public static ArrayList<Intake> allIntake = new ArrayList<Intake>();
    public static ArrayList<project.roles.Module> allModule = new ArrayList<>();
    public static ArrayList<IntakeModule> allIntakeModule = new ArrayList<IntakeModule>();
    
    public static void writeIntake(){
        try{
            PrintWriter a = new PrintWriter("src/resources/intake.txt");
            for(Intake x : allIntake){
                a.println(x.getIntakeId());
                a.println(x.getIntakeName());
                a.println();
            }
            a.close();
            PrintWriter b = new PrintWriter("src/resources/module.txt");
            for(project.roles.Module x : allModule){
                b.println(x.getModuleId());
                b.println(x.getModuleName());
                b.println(x.getLeader().getId());
                b.println();
            }
            b.close();
            PrintWriter c = new PrintWriter("src/resources/intake_module.txt");
            for(IntakeModule x : allIntakeModule){
                c.println(x.getIMID());
                c.println(x.getIntakeId());
                c.println(x.getModuleId());
                c.println();
            }
            c.close();
        }catch(Exception e){
            System.out.println("Error in write ..........");
            e.printStackTrace();
        }
    }
    
    public static void readIntake(){
        try{
            Scanner s1 = new Scanner(new File("src/resources/intake.txt"));
            while(s1.hasNext()){
                String id = s1.nextLine();
                String name = s1.nextLine();
                s1.nextLine();
                allIntake.add(new Intake(id, name));
            }
            Scanner s2 = new Scanner(new File("src/resources/module.txt"));
            while(s2.hasNext()){
                String id = s2.nextLine();
                String name = s2.nextLine();
                String leader = s2.nextLine();
                s2.nextLine();
                allModule.add(new project.roles.Module(id, name, InteractTxt.checkLeaID(leader)));
                InteractTxt.checkLeaID(leader).Lea_Modules.add(checkModID(id));
            }
            Scanner s3 = new Scanner(new File("src/resources/intake_module.txt"));
            while(s3.hasNext()){
                String IMID = s3.nextLine();
                String intakeId = s3.nextLine();
                String moduleId = s3.nextLine();
                s3.nextLine();
                allIntakeModule.add(new IntakeModule(IMID, intakeId, moduleId));
            }
        }catch(Exception e){
            System.out.println("Error in read ..........");
            e.printStackTrace();
        }
    }
    
    public static Intake checkIntID(String id){
        for(Intake x : allIntake){
            if(id.equals(x.getIntakeId())){
                return x;
            }
        }
        return null;
    }
    
    public static project.roles.Module checkModID(String id){
        for(project.roles.Module x : allModule){
            if(id.equals(x.getModuleId())){
                return x;
            }
        }
        return null;
    }
    
    public static IntakeModule checkIMID(String IMID){
        for(IntakeModule x : allIntakeModule){
            if(IMID.equals(x.getIMID())){
                return x;
            }
        }
        return null;
    }
    
    public static IntakeModule checkIMID(String intakeId, String moduleId){
        for(IntakeModule x : allIntakeModule){
            if(intakeId.equals(x.getIntakeId()) && moduleId.equals(x.getModuleId())){
                return x;
            }
        }
        return null;
    }
    
    public static void writeModuleTaught(){
        try{
            PrintWriter a = new PrintWriter("src/resources/module_taught.txt");
            for(project.roles.Module x : allModule){
                for(Lecturer y : x.Mod_Lecturers){
                    a.println(x.getModuleId());
                    a.println(y.getId());
                    a.println();
                }
            }
            a.close();
        }catch(Exception e){
            System.out.println("Error in write ..........");
            e.printStackTrace();
        }
    }
    
    public static void readModuleTaught(){
        try{
            Scanner s1 = new Scanner(new File("src/resources/module_taught.txt"));
            while(s1.hasNext()){
                String moduleId = s1.nextLine();
                String lecturerId = s1.nextLine();
                s1.nextLine();
                InteractTxt.checkModID(moduleId).Mod_Lecturers.add(InteractTxt.checkLecID(lecturerId));
                InteractTxt.checkLecID(lecturerId).Lec_Modules.add(InteractTxt.checkModID(moduleId));
            }
            
        }catch(Exception e){
            System.out.println("Error in read ..........");
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Class> allClass = new ArrayList<Class>();
    
    public static void writeClass(){
        try{
            PrintWriter a = new PrintWriter("src/resources/class.txt");
            for(Class x : allClass){
                a.println(x.getClassId());
                a.println(x.getClassName());
                a.println(x.getLecId());
                a.println();
            }
            a.close();
            PrintWriter b = new PrintWriter("src/resources/intake_class.txt");
            for(IntakeModule x : allIntakeModule){
                for(Class y : x.IM_Classes){
                    b.println(x.getIMID());
                    b.println(y.getClassId());
                    b.println();
                }   
            }
            b.close();
            PrintWriter c = new PrintWriter("src/resources/student_class.txt");
            for(Student x : allStudent){
                for(Class y : x.Stu_Classes){
                    c.println(x.getId());
                    c.println(y.getClassId());
                    c.println(Tools.getSpecificGrade(y, x));
                    c.println(Tools.getSpecificComment(y, x));
                    c.println();
                }   
            }
            c.close();
        }catch(Exception e){
            System.out.println("Error in write ..........");
            e.printStackTrace();
        }
    }
    
    public static void readClass(){
        try{
            Scanner s1 = new Scanner(new File("src/resources/class.txt"));
            while(s1.hasNext()){
                String id = s1.nextLine();
                String name = s1.nextLine();
                String lecId = s1.nextLine();
                s1.nextLine();
                allClass.add(new Class(id, name, lecId));
                if(!lecId.equals("NA")){
                    InteractTxt.checkLecID(lecId).Lec_Classes.add(InteractTxt.checkClassID(id));
                }
            }
            Scanner s2 = new Scanner(new File("src/resources/intake_class.txt"));
            while(s2.hasNext()){
                String IMID = s2.nextLine();
                String classId = s2.nextLine();
                s2.nextLine();
                InteractTxt.checkIMID(IMID).IM_Classes.add(InteractTxt.checkClassID(classId));
            }
            Scanner s3 = new Scanner(new File("src/resources/student_class.txt"));
            while(s3.hasNext()){
                String studentId = s3.nextLine();
                String classId = s3.nextLine();
                String grade = s3.nextLine();
                String comment = s3.nextLine();
                s3.nextLine();
                Student targetStu = InteractTxt.checkStuID(studentId);
                Class targetClass = InteractTxt.checkClassID(classId);
                targetStu.Stu_Classes.add(targetClass);
                targetStu.GradesAndComments.add(new StudentGradeAndComment(targetClass, grade, comment));
                InteractTxt.checkClassID(classId).Class_Students.add(InteractTxt.checkStuID(studentId));
            }
        }catch(Exception e){
            System.out.println("Error in read ..........");
            e.printStackTrace();
        }
    }
    
    public static Class checkClassID(String id){
        for(Class x : allClass){
            if(id.equals(x.getClassId())){
                return x;
            }
        }
        return null;
    }
    
    public static ArrayList<Assessment> allAssessment = new ArrayList<Assessment>();
    
    public static void writeAssessment(){
        try{
            PrintWriter a = new PrintWriter("src/resources/assessment.txt");
            for(Assessment x : allAssessment){
                a.println(x.getAssId());
                a.println(x.getAssName());
                a.println(x.getAssType());
                a.println(x.getAssFullMarks());
                a.println(x.getAssPercentage());
                a.println(x.getAssIM().getIMID());
                a.println();
            }
            a.close();
            
            PrintWriter b = new PrintWriter("src/resources/student_assessment.txt");
            for(Student x : allStudent){
                if (x.Stu_Scores.size() >= 1) {
                    for (StudentScore y : x.Stu_Scores) {
                        b.println(x.getId());
                        b.println(y.getAssessment().getAssId());
                        b.println(y.getFinalScore());
                        b.println(y.getOrginalScore());
                        b.println(y.getOriginalFullMarks());
                        b.println(y.getFeedback());
                        b.println();
                    }
                }
            }
            b.close();
        }catch(Exception e){
            System.out.println("Error in write ..........");
            e.printStackTrace();
        }
    }
    
    public static void readAssessment(){
        try{
            Scanner s1 = new Scanner(new File("src/resources/assessment.txt"));
            while(s1.hasNext()){
                String id = s1.nextLine();
                String name = s1.nextLine();
                String type = s1.nextLine();
                String fullMarks = s1.nextLine();
                String percentage = s1.nextLine();
                String IMID = s1.nextLine();
                s1.nextLine();
                allAssessment.add(new Assessment(id, name, type, percentage,fullMarks, checkIMID(IMID)));
                InteractTxt.checkIMID(IMID).IM_Assessments.add(InteractTxt.checkAssID(id));
            } 
            Scanner s2 = new Scanner(new File("src/resources/student_assessment.txt"));
            while(s2.hasNext()){
                String stuId = s2.nextLine();
                String assId = s2.nextLine();
                String finalScore = s2.nextLine();
                String originalScore = s2.nextLine();
                String originalFullMarks = s2.nextLine();
                String feedback = s2.nextLine();
                s2.nextLine();
                
                if (stuId.isEmpty()) System.out.println("gay");
                InteractTxt.checkStuID(stuId).Stu_Scores.add(new StudentScore(InteractTxt.checkAssID(assId), finalScore, originalScore, originalFullMarks, feedback));
            } 
        }catch(Exception e){
            System.out.println("Error in read ..........");
            e.printStackTrace();
        }
    }
    
    public static Assessment checkAssID(String id){
        for(Assessment x : allAssessment){
            if(id.equals(x.getAssId())){
                return x;
            }
        }
        return null;
    }
    
    public static ArrayList<Grading> allGrading = new ArrayList<Grading>();
    
    public static void writeGrade(){
        try{
            PrintWriter a = new PrintWriter("src/resources/grading.txt");
            for(Grading x : allGrading){
                a.println(x.getGrade());
                a.println(x.getMarksFrom());
                a.println(x.getMarksTo());
                a.println();
            }
            a.close();
            
        }catch(Exception e){
            System.out.println("Error in write ..........");
            e.printStackTrace();
        }
    }
    
    public static void readGrade(){
        try{
            Scanner s1 = new Scanner(new File("src/resources/grading.txt"));
            while(s1.hasNext()){
                String Grade = s1.nextLine();
                String MarksFrom = s1.nextLine();
                String MarksTo = s1.nextLine();
                s1.nextLine();
                allGrading.add(new Grading(Grade, MarksFrom, MarksTo));
            } 
        }catch(Exception e){
            System.out.println("Error in read ..........");
            e.printStackTrace();
        }
    }
    
    public static void initDatabase() {
        InteractTxt.readUser();
        InteractTxt.readIntake();
        InteractTxt.readModuleTaught();
        InteractTxt.readClass();
        InteractTxt.readAssessment();
        InteractTxt.readGrade();
    }
    
    public static void saveDatabase() {
        InteractTxt.writeUser();
        InteractTxt.writeIntake();
        InteractTxt.writeModuleTaught();
        InteractTxt.writeClass();
        InteractTxt.writeAssessment();
        InteractTxt.writeGrade();
    }
}
