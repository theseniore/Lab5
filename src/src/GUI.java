import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    
    // Database instance for handling student data operations
    private StudentDatabase db = new StudentDatabase();

    public GUI() {
        // Set window properties
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen

        // Display the main menu
        showMainMenu();
    }

    /**
     * Displays the main menu with navigation buttons
     */
    private void showMainMenu() {
        // Clear current content
        getContentPane().removeAll();
        
        // Create main panel with grid layout
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        // Create navigation buttons
        JButton addBtn = new JButton("Add Student");
        JButton updateBtn = new JButton("Update Student");
        JButton deleteBtn = new JButton("Delete Student");
        JButton searchBtn = new JButton("Search Student");
        JButton displayBtn = new JButton("Display All Students");

        // Add action listeners for navigation
        addBtn.addActionListener(e -> showAddStudentWindow());
        updateBtn.addActionListener(e -> showUpdateStudentWindow());
        deleteBtn.addActionListener(e -> showDeleteStudentWindow());
        searchBtn.addActionListener(e -> showSearchStudentWindow());
        displayBtn.addActionListener(e -> showAllStudentsWindow());

        // Add buttons to panel
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(searchBtn);
        panel.add(displayBtn);

        // Add panel to frame and refresh
        add(panel);
        revalidate();
        repaint();
    }

    /**
     * Displays the Add Student form window
     */
    private void showAddStudentWindow() {
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Create form panel with grid layout
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Create input fields
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField gpaField = new JTextField();

        // Add labels and fields to panel
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

        // Create action buttons
        JButton saveBtn = new JButton("Add");
        JButton backBtn = new JButton("Back");

        // Save button action listener
        saveBtn.addActionListener(e -> {
            try {
                // Get and validate input data
                int id = Integer.parseInt(idField.getText());
                
                // Check if student ID already exists
                if (db.contains(id)) {
                    JOptionPane.showMessageDialog(this, "Student with this ID already exists!");
                    return;
                }

                // Get other field values
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String gender = genderField.getText();
                String dept = deptField.getText();
                float gpa = Float.parseFloat(gpaField.getText());

                // Validate input data using Validation class
                if (!Validation.isValidGender(gender) || !Validation.isValidGPA(gpa) ||
                    !Validation.isValidAge(age) || !Validation.isValidName(name)) {
                    JOptionPane.showMessageDialog(this, "Invalid input data!");
                    return;
                }

                // Create new student object and add to database
                Student s = new Student(id, name, age, gender, dept, gpa);
                db.addStudent(s);
                JOptionPane.showMessageDialog(this, "Student added successfully!");

                // Clear all input fields after successful addition
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                genderField.setText("");
                deptField.setText("");
                gpaField.setText("");
                
            } catch (Exception ex) {
                // Handle invalid input format
                JOptionPane.showMessageDialog(this, "Error: Invalid input format!");
            }
        });

        // Back button action listener
        backBtn.addActionListener(e -> showMainMenu());

        // Create button panel
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);

        // Add components to frame
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        
        // Refresh the display
        revalidate();
        repaint();
    }

    /**
     * Displays the Update Student form window
     */
    private void showUpdateStudentWindow() {
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Create form panel
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Create input components
        JTextField idOrNameField = new JTextField();
        JComboBox<String> fieldBox = new JComboBox<>(new String[]{"Full Name", "Age", "Gender", "Department", "GPA"});
        JTextField newValueField = new JTextField();

        // Add components to panel
        panel.add(new JLabel("Enter Student ID or Name:"));
        panel.add(idOrNameField);
        panel.add(new JLabel("Select Field to Update:"));
        panel.add(fieldBox);
        panel.add(new JLabel("New Value:"));
        panel.add(newValueField);

        // Create action buttons
        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("Back");

        // Update button action listener
        updateBtn.addActionListener(e -> {
            String key = idOrNameField.getText().trim();
            Student student = null;
            
            try {
                // Try to search by ID first
                student = db.searchByID(Integer.parseInt(key));
            } catch (Exception ignored) {
                // If ID search fails, continue to name search
            }
            
            // If not found by ID, try searching by name
            if (student == null) {
                student = db.searchByName(key);
            }
            
            // If student still not found, show error message
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }

            // Get selected field and new value
            String selectedField = (String) fieldBox.getSelectedItem();
            String newVal = newValueField.getText().trim();

            try {
                // Update the selected field based on user choice
                switch (selectedField) {
                    case "Full Name": 
                        student.setFullName(newVal); 
                        break;
                    case "Age": 
                        student.setAge(Integer.parseInt(newVal)); 
                        break;
                    case "Gender": 
                        student.setGender(newVal); 
                        break;
                    case "Department": 
                        student.setDepartment(newVal); 
                        break;
                    case "GPA": 
                        student.setGPA(Float.parseFloat(newVal)); 
                        break;
                }
                
                // Save changes to file
                db.saveToFile(db.getAllStudents());
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                
                // Clear input fields
                idOrNameField.setText("");
                newValueField.setText("");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid update value!");
            }
        });

        // Back button action listener
        backBtn.addActionListener(e -> showMainMenu());

        // Create button panel
        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(backBtn);

        // Add components to frame
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        
        // Refresh display
        revalidate();
        repaint();
    }

    /**
     * Displays the Delete Student window
     */
    private void showDeleteStudentWindow() {
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Create input panel
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(130, 80, 130, 80));

        // Create input field
        JTextField keyField = new JTextField();
        panel.add(new JLabel("Enter Student ID or Name:"));
        panel.add(keyField);

        // Create action buttons
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        // Delete button action listener
        deleteBtn.addActionListener(e -> {
            String key = keyField.getText().trim();
            Student student = null;
            
            try {
                // Try to search by ID first
                student = db.searchByID(Integer.parseInt(key));
            } catch (Exception ignored) {
                // If ID search fails, continue to name search
            }
            
            // If not found by ID, try searching by name
            if (student == null) {
                student = db.searchByName(key);
            }
            
            // If student not found, show error message
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }
            
            // Delete student from database
            db.deleteStudent(student);
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            
            // Clear input field
            keyField.setText("");
        });

        // Back button action listener
        backBtn.addActionListener(e -> showMainMenu());

        // Create button panel
        JPanel btnPanel = new JPanel();
        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);

        // Add components to frame
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        
        // Refresh display
        revalidate();
        repaint();
    }

    /**
     * Displays the Search Student window
     */
    private void showSearchStudentWindow() {
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));

        // Create main panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Create search components
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setPreferredSize(new Dimension(120, 30)); // Make the button bigger
        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false); // Make result area read-only

        // Create top panel for search input
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        // Add components to main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Create back button
        JButton backBtn = new JButton("Back");

        // Search button action listener
        searchBtn.addActionListener(e -> {
            String key = searchField.getText().trim();
            Student student = null;
            
            try {
                // Try to search by ID first
                student = db.searchByID(Integer.parseInt(key));
            } catch (Exception ignored) {
                // If ID search fails, continue to name search
            }
            
            // If not found by ID, try searching by name
            if (student == null) {
                student = db.searchByName(key);
            }
            
            // Display results
            if (student == null) {
                resultArea.setText("No student found.");
            } else {
                // Format and display student information
                resultArea.setText(
                    "ID: " + student.getStudentId() + "\n" +
                    "Name: " + student.getFullName() + "\n" +
                    "Age: " + student.getAge() + "\n" +
                    "Gender: " + student.getGender() + "\n" +
                    "Department: " + student.getDepartment() + "\n" +
                    "GPA: " + student.getGPA()
                );
            }
            
            // Clear search field
            searchField.setText("");
        });

        // Back button action listener
        backBtn.addActionListener(e -> showMainMenu());

        // Create button panel
        JPanel btnPanel = new JPanel();
        btnPanel.add(backBtn);

        // Add components to frame
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // Refresh display
        revalidate();
        repaint();
    }

    /**
     * Displays all students in a scrollable text area
     */
    private void showAllStudentsWindow() {
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Create text area for displaying students
        JTextArea area = new JTextArea();
        area.setEditable(false); // Make it read-only
        
        // Get all students from database
        ArrayList<Student> studentList = db.getAllStudents();

        // Build display string
        StringBuilder displayText = new StringBuilder();
        for (Student student : studentList) {
            displayText.append(student.lineRepresentation()).append("\n");
        }

        // Set text to area (or display message if no students)
        area.setText(displayText.length() > 0 ? displayText.toString() : "No students available.");

        // Create back button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showMainMenu());

        // Add components to frame
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        // Refresh display
        revalidate();
        repaint();
    }

    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        // Create and display the GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}








