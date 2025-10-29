public class Student {
    private int StudentId;
    private String FullName;
    private int Age;
    private String Gender;
    private String Department;
    private float GPA;
    public Student(int StudentID, String FullName, int Age, String Gender, String Department, float GPA) {
        this.StudentId=StudentID;
        this.FullName=FullName;
        this.Age=Age;
        this.Gender=Gender;
        this.Department=Department;
        this.GPA=GPA;
    }
    public void setStudentId(int StudentId) {
        this.StudentId=StudentId;
    }
    public int getStudentId() {
        return StudentId;
    }
    public void setFullName(String FullName) {
        this.FullName=FullName;
    }
    public String getFullName() {
        return FullName;
    }
    public void setAge(int Age) {
        this.Age=Age;
    }
    public int getAge() {
        return Age;
    }
    public void setGender(String Gender) {
        this.Gender=Gender;
    }
    public String getGender() {
        return Gender;
    }
    public void setDepartment(String Department) {
        this.Department=Department;
    }
    public String getDepartment() {
        return Department;
    }
    public void setGPA(float GPA) {
        this.GPA=GPA;
    }
    public float getGPA() {
        return GPA;
    }
    public String lineRepresentation()
    {
        String Line = this.StudentId+","+this.FullName+","+this.Age+","+this.Gender+","+this.Department+","+this.GPA;
        return Line;
    }
}
