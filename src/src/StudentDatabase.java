import java.util.ArrayList;

public class StudentDatabase extends FileHandling implements Database {
    private ArrayList <Student> Records = loadFromFile();
    public StudentDatabase()
    {
        this.Records = new ArrayList<>();
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
        for(Student d:this.Records)
        {
            if(s.equals(d))
                d = s;
        }
        saveToFile(this.Records);
    }

    @Override
    public Student searchByName(String name) {
    for(Student d:this.Records)
    {
        if(name.equals(d.getFullName()))
            return d;
    }
    return null;
    }

    @Override
    public void deleteStudent(Student s) {
        this.Records.remove(s);
        saveToFile(this.Records);
    }
}
