import java.util.ArrayList;

public class StudentDatabase extends FileHandling implements Database {
    private ArrayList <Student> Records = new ArrayList<>();
    @Override
    public void addStudent(Student s) {
    this.Records = loadFromFile();
    this.Records.add(s);
    saveToFile(this.Records);
    }

    @Override
    public ArrayList<Student> getAllStudents() {
        this.Records = loadFromFile();
        return this.Records;
    }

    @Override
    public Student searchByID(int id) {
        this.Records = loadFromFile();
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
        this.Records = loadFromFile();
        for(Student d:this.Records)
        {
            if(s.equals(d))
                d = s;
        }
        saveToFile(this.Records);
    }

    @Override
    public ArrayList<Student> searchByName(String name) {
        return null;
    }

    @Override
    public void deleteStudent(Student s) {

    }
}
