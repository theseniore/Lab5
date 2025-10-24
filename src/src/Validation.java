public class Validation{

    public static boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
    }

    public static boolean isValidGPA(float gpa) {
        return gpa >= 0 && gpa <= 4;
    }

    public static boolean isValidAge(int age) {
        return age > 0 && age < 99;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

}


