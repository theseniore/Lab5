import java.util.ArrayList;

public class StudentDatabase extends FileHandling implements Database {
    private final ArrayList <Student> Records;
    public StudentDatabase()
    {
        ArrayList<Student> loaded = FileHandling.loadFromFile();
        if (loaded != null) {
            Records = loaded;
        } else {
            Records = new ArrayList<>();
        }
    }
    @Override
    public void addStudent(Student s) {
    this.Records.add(s);
    saveToFile(this.Records);
    }

    @Override
    public ArrayList<Student> getAllStudents() {

        return this.Records;
    }
    public boolean contains(int key )
    {
        for(Student rec: this.Records)
        {
            if(rec.getStudentId()==(key))
                return true;
        }
        return false;
    }
    @Override
    public Student searchByID(int id) {
        for(Student  d: this.Records)
        {
            int x = d.getStudentId();
            if(id == x)
                return d;
        }
        return null;
    }

    @Override
    public void updateStudent(Student s) {
        for(Student std:this.Records)
        {
            if(contains(s.getStudentId()))
                std = s;
        }
        saveToFile(this.Records);
    }

    @Override
    public Student searchByName(String name) {
    for(Student d:this.Records)
    {
        if(name.toLowerCase().equals(d.getFullName().toLowerCase()))
            return d;
    }
    return null;
    }

    @Override
    public void deleteStudent(Student s) {
        this.Records.remove(s);
        saveToFile(this.Records);
    }
    public void updateStudentField(int id, String field, String newValue) {
        for (Student s : Records) {
            if (s.getStudentId() == id) {
                switch (field) {
                    case "Name":
                        s.setFullName(newValue);
                        break;
                    case "Age":
                        try {
                            s.setAge(Integer.parseInt(newValue));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid age input");
                        }
                        break;
                    case "Gender":
                        s.setGender(newValue);
                        break;
                    case "Department":
                        s.setDepartment(newValue);
                        break;
                    case "GPA":
                        try {
                            s.setGPA(Float.parseFloat(newValue));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid GPA input");
                        }
                        break;
                    default:
                        System.out.println("Invalid field name");
                }
                // Save the updated records
                saveToFile(Records);
                return; // stop after updating the correct student
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

}

