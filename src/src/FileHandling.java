import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHandling {

        public static ArrayList<Student> loadFromFile () {
            try {
                Path filePath = Paths.get(System.getProperty("user.dir"), "Students.txt");
                BufferedReader r = new BufferedReader(new FileReader(filePath.toFile()));
                ArrayList<Student> students = new ArrayList<>();
                String line;
                Student e;
                while ((line = r.readLine()) != null) {
                    e = createRecordFrom(line);
                    students.add(e);
                }
                r.close();
                return students;
            } catch (IOException e) {
                System.out.println("this file cannot be opened!");


            }
            return null;

        }
    public static Student createRecordFrom(String line) {
        Student e;
        String[] data = line.split(",");
        int StudentId= Integer.parseInt(data[0]);
        String Name = data[1];
        int Age = Integer.parseInt(data[2]);
        String Gender = data[3];
        String Department = data[4];
        float GPA = Float.parseFloat(data[5]);
        e = new Student( StudentId, Name,  Age, Gender, Department,  GPA);
        return e;
    }

    public void saveToFile(ArrayList<Student> students)
    {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "Students.txt");
            BufferedWriter w = new BufferedWriter(new FileWriter(filePath.toFile()));
            String line;
            for(Student rec :students )
            {
                line = rec.lineRepresentation();
                w.write(line);
                w.newLine();
            }
            w.close();
        }
        catch(IOException e)
        {
            System.out.println("There is an error while writing in file!");
            return;
        }
    }

}
