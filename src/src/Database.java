import java.util.ArrayList;

public interface Database {
    void addStudent(Student s);
    ArrayList<Student> getAllStudents();
    Student searchByID(int id);
    void updateStudent(Student s);
    ArrayList<Student> searchByName(String name);
    void deleteStudent(Student s);
}
