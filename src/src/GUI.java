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
        loginMenu();
    }
    private void loginMenu(){
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10)); // rows, cols, hgap, vgap
        panel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        JButton login = new JButton("Log in");
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        login.addActionListener(e -> {
            if (Validation.isValiduser(username.getText(), password.getText())) {
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password!");
            }
        });
        login.setSize(50, 50);
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(login);
        ;
     add(panel);
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
        JComboBox<String> genderField = new JComboBox<>(new String[]{"Male", "Female"});
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
                String gender = genderField.getSelectedItem().toString();
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
        JComboBox<String> fieldBox = new JComboBox<>(new String[]{"Full Name", "Age", "Department", "GPA"});
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
        JLabel label = new JLabel("Enter Student Name Or ID:");
        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setPreferredSize(new Dimension(120, 30)); // Make the button bigger
        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false); // Make result area read-only

        // Create top panel for search input
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(label,BorderLayout.NORTH);
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
        setLayout(new BorderLayout(10, 10));

        try {
            ArrayList<Student> students = db.getAllStudents();

            if (students.isEmpty()) {
                JLabel emptyLabel = new JLabel("No students available.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
                add(emptyLabel, BorderLayout.CENTER);
            } else {
                // Table headers
                String[] columnNames = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
                Object[][] data = new Object[students.size()][6];

                // Fill table data
                for (int i = 0; i < students.size(); i++) {
                    Student s = students.get(i);
                    data[i][0] = s.getStudentId();
                    data[i][1] = s.getFullName();
                    data[i][2] = s.getAge();
                    data[i][3] = s.getGender();
                    data[i][4] = s.getDepartment();
                    data[i][5] = s.getGPA();
                }

                // Create table
                JTable table = new JTable(data, columnNames);
                table.setRowHeight(25);
                table.setFillsViewportHeight(true);
                table.setAutoCreateRowSorter(true);
                table.setEnabled(false); // make it read-only

                // Add scroll pane
                JScrollPane scrollPane = new JScrollPane(table);
                add(scrollPane, BorderLayout.CENTER);
            }

            // Back button
            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(e -> showMainMenu());
            JPanel btnPanel = new JPanel();
            btnPanel.add(backBtn);
            add(btnPanel, BorderLayout.SOUTH);

            revalidate();
            repaint();

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading student data.", SwingConstants.CENTER);
            add(errorLabel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
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








