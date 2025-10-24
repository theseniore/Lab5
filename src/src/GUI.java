import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private StudentDatabase db = new StudentDatabase();

    public GUI() {
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showMainMenu();
    }

    private void showMainMenu() {
        getContentPane().removeAll();
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JButton addBtn = new JButton("Add Student");
        JButton updateBtn = new JButton("Update Student");
        JButton deleteBtn = new JButton("Delete Student");
        JButton searchBtn = new JButton("Search Student");
        JButton displayBtn = new JButton("Display All Students");

        addBtn.addActionListener(e -> showAddStudentWindow());
        updateBtn.addActionListener(e -> showUpdateStudentWindow());
        deleteBtn.addActionListener(e -> showDeleteStudentWindow());
        searchBtn.addActionListener(e -> showSearchStudentWindow());
        displayBtn.addActionListener(e -> showAllStudentsWindow());

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(searchBtn);
        panel.add(displayBtn);

        add(panel);
        revalidate();
        repaint();
    }

    private void showAddStudentWindow() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField gpaField = new JTextField();

        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);
        panel.add(new JLabel("GPA:"));
        panel.add(gpaField);

        JButton saveBtn = new JButton("Add");
        JButton backBtn = new JButton("Back");

        saveBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (db.contains(id)) {
                    JOptionPane.showMessageDialog(this, "Student with this ID already exists!");
                    return;
                }

                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String gender = genderField.getText();
                String dept = deptField.getText();
                float gpa = Float.parseFloat(gpaField.getText());

                if (!Validation.isValidGender(gender) || !Validation.isValidGPA(gpa) ||
                        !Validation.isValidAge(age) || !Validation.isValidName(name)) {
                    JOptionPane.showMessageDialog(this, "Invalid input data!");
                    return;
                }

                Student s = new Student(id, name, age, gender, dept, gpa);
                db.addStudent(s);
                JOptionPane.showMessageDialog(this, "Student added successfully!");

                // clear fields after add
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                genderField.setText("");
                deptField.setText("");
                gpaField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid input format!");
            }
        });

        backBtn.addActionListener(e -> showMainMenu());

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showUpdateStudentWindow() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JTextField idOrNameField = new JTextField();
        JComboBox<String> fieldBox = new JComboBox<>(new String[]{"Full Name", "Age", "Gender", "Department", "GPA"});
        JTextField newValueField = new JTextField();

        panel.add(new JLabel("Enter Student ID or Name:"));
        panel.add(idOrNameField);
        panel.add(new JLabel("Select Field to Update:"));
        panel.add(fieldBox);
        panel.add(new JLabel("New Value:"));
        panel.add(newValueField);

        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("Back");

        updateBtn.addActionListener(e -> {
            String key = idOrNameField.getText().trim();
            Student s = null;
            try {
                s = db.searchByID(Integer.parseInt(key));
            } catch (Exception ignored) {
            }
            if (s == null) s = db.searchByName(key);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }

            String selectedField = (String) fieldBox.getSelectedItem();
            String newVal = newValueField.getText().trim();

            try {
                switch (selectedField) {
                    case "Full Name": s.setFullName(newVal); break;
                    case "Age": s.setAge(Integer.parseInt(newVal)); break;
                    case "Gender": s.setGender(newVal); break;
                    case "Department": s.setDepartment(newVal); break;
                    case "GPA": s.setGPA(Float.parseFloat(newVal)); break;
                }
                db.saveToFile(db.getAllStudents());
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                idOrNameField.setText("");
                newValueField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid update value!");
            }
        });

        backBtn.addActionListener(e -> showMainMenu());

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(backBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showDeleteStudentWindow() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JTextField keyField = new JTextField();
        panel.add(new JLabel("Enter Student ID or Name:"));
        panel.add(keyField);

        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        deleteBtn.addActionListener(e -> {
            String key = keyField.getText().trim();
            Student s = null;
            try {
                s = db.searchByID(Integer.parseInt(key));
            } catch (Exception ignored) {}
            if (s == null) s = db.searchByName(key);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }
            db.deleteStudent(s);
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            keyField.setText("");
        });

        backBtn.addActionListener(e -> showMainMenu());

        JPanel btnPanel = new JPanel();
        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showSearchStudentWindow() {
        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setPreferredSize(new Dimension(120, 30)); // make the button bigger
        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> {
            String key = searchField.getText().trim();
            Student s = null;
            try {
                s = db.searchByID(Integer.parseInt(key));
            } catch (Exception ignored) {}
            if (s == null) s = db.searchByName(key);
            if (s == null) {
                resultArea.setText("No student found.");
            } else {
                resultArea.setText(
                        "ID: " + s.getStudentId() + "\nName: " + s.getFullName() + "\nAge: " + s.getAge() +
                                "\nGender: " + s.getGender() + "\nDepartment: " + s.getDepartment() +
                                "\nGPA: " + s.getGPA()
                );
            }
            searchField.setText("");
        });

        backBtn.addActionListener(e -> showMainMenu());

        JPanel btnPanel = new JPanel();
        btnPanel.add(backBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void showAllStudentsWindow() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        ArrayList<Student> list = db.getAllStudents();

        StringBuilder sb = new StringBuilder();
        for (Student s : list) {
            sb.append(s.lineRepresentation()).append("\n");
        }

        area.setText(sb.length() > 0 ? sb.toString() : "No students available.");

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showMainMenu());

        add(new JScrollPane(area), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new GUI().setVisible(true);
    }
}
