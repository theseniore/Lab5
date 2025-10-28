import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        ImageIcon img = new ImageIcon(getClass().getResource("/student.png"));
        setIconImage(img.getImage());


        // Display the main menu
        loginMenu();
    }
    private void loginMenu(){
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10)); // rows, cols, hgap, vgap
        panel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        JButton login = new JButton("Log in");
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        login.addActionListener(e -> {//lambda expression
            if (Validation.isValiduser(username.getText(), password.getText())) {
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password!","Error",JOptionPane.WARNING_MESSAGE);
            }
        });
        login.setSize(50, 50);
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(login);
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
        updateBtn.addActionListener(e -> showUpdateOrSearchWindow());
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
    private void showUpdateOrSearchWindow() {
        JFrame frame = new JFrame("Update or Search Student");
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        String[] columns = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 500, 250);
        frame.add(scrollPane);

        ArrayList<Student> all = db.getAllStudents();
        for (Student s : all) {
            model.addRow(new Object[]{
                    s.getStudentId(),
                    s.getFullName(),
                    s.getAge(),
                    s.getGender(),
                    s.getDepartment(),
                    s.getGPA()
            });
        }

        JLabel searchLabel = new JLabel("Enter Name or ID ( You must search for the student ):");
        searchLabel.setBounds(30, 250, 350, 100);
        JTextField searchField = new JTextField();
        searchField.setBounds(180, 310, 150, 25);
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(340, 310, 90, 25);
        frame.add(searchLabel);
        frame.add(searchField);
        frame.add(searchButton);

        JLabel fieldLabel = new JLabel("Field to Update:");
        fieldLabel.setBounds(580, 50, 200, 25);
        frame.add(fieldLabel);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Name", "Age", "Department", "GPA"});
        comboBox.setBounds(580, 80, 200, 25);
        frame.add(comboBox);

        JLabel newValueLabel = new JLabel("New Value:");
        newValueLabel.setBounds(580, 120, 200, 25);
        frame.add(newValueLabel);

        JTextField newValueField = new JTextField();
        newValueField.setBounds(580, 150, 200, 25);
        frame.add(newValueField);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(580, 200, 90, 30);
        frame.add(saveButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(690, 200, 90, 30);
        frame.add(backButton);

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            model.setRowCount(0);

            if (query.isEmpty()) {
                for (Student s : db.getAllStudents()) {
                    model.addRow(new Object[]{
                            s.getStudentId(),
                            s.getFullName(),
                            s.getAge(),
                            s.getGender(),
                            s.getDepartment(),
                            s.getGPA()
                    });
                }
            } else {
                try {
                    int id = Integer.parseInt(query);
                    Student s = db.searchByID(id);
                    if (s != null) {
                        model.addRow(new Object[]{
                                s.getStudentId(),
                                s.getFullName(),
                                s.getAge(),
                                s.getGender(),
                                s.getDepartment(),
                                s.getGPA()
                        });
                    } else {
                        JOptionPane.showMessageDialog(frame, "No record found with ID: " + id);
                    }
                } catch (NumberFormatException ex) {
                    Student s = db.searchByName(query);
                    if (s != null) {
                        model.addRow(new Object[]{
                                s.getStudentId(),
                                s.getFullName(),
                                s.getAge(),
                                s.getGender(),
                                s.getDepartment(),
                                s.getGPA()
                        });
                    } else {
                        JOptionPane.showMessageDialog(frame, "No record found with Name: " + query);
                    }
                }
            }
        });

        saveButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter ID or Name first.");
                return;
            }

            String field = comboBox.getSelectedItem().toString();
            String newValue = newValueField.getText().trim();
            if (newValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter the new value.");
                return;
            }

            try {
                Student s;
                try {
                    int id = Integer.parseInt(query);
                    s = db.searchByID(id);
                } catch (NumberFormatException ex) {
                    s = db.searchByName(query);
                }

                if (s == null) {
                    JOptionPane.showMessageDialog(frame, "No record found to update.");
                    return;
                }

                boolean valid = true;

                switch (field) {
                    case "Name" -> {
                        if (Validation.isValidName(newValue)) {
                            s.setFullName(newValue);
                        } else {
                            valid = false;
                            JOptionPane.showMessageDialog(frame, "Invalid Name. Please enter a valid name.");
                        }
                    }

                    case "Age" -> {
                        try {
                            int age = Integer.parseInt(newValue);
                            if (Validation.isValidAge(age)) {
                                s.setAge(age);
                            } else {
                                valid = false;
                                JOptionPane.showMessageDialog(frame, "Invalid Age. Age must be between 1 and 98.");
                            }
                        } catch (NumberFormatException ex) {
                            valid = false;
                            JOptionPane.showMessageDialog(frame, "Please enter a valid number for Age.");
                        }
                    }

                    case "Department" -> {
                        if (!newValue.trim().isEmpty()) {
                            s.setDepartment(newValue);
                        } else {
                            valid = false;
                            JOptionPane.showMessageDialog(frame, "Department name cannot be empty.");
                        }
                    }

                    case "GPA" -> {
                        try {
                            float gpa = Float.parseFloat(newValue);
                            if (Validation.isValidGPA(gpa)) {
                                s.setGPA(gpa);
                            } else {
                                valid = false;
                                JOptionPane.showMessageDialog(frame, "Invalid GPA. It must be between 0 and 4.");
                            }
                        } catch (NumberFormatException ex) {
                            valid = false;
                            JOptionPane.showMessageDialog(frame, "Please enter a valid decimal number for GPA.");
                        }
                    }
                }

                if (valid) {
                    db.updateStudent(s);
                    JOptionPane.showMessageDialog(frame, "Record updated successfully!");

                    model.setRowCount(0);
                    model.addRow(new Object[]{
                            s.getStudentId(),
                            s.getFullName(),
                            s.getAge(),
                            s.getGender(),
                            s.getDepartment(),
                            s.getGPA()
                    });
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An unexpected error occurred.");
            }
        });

        backButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
    /**
     * Displays the Delete Student window
     */
    private void showDeleteStudentWindow() {
        // Clear current content
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));
        JLabel label = new JLabel("Select Student to Delete:");

        try {
            ArrayList<Student> students = db.getAllStudents();

            if (students.isEmpty()) {
                JLabel emptyLabel = new JLabel("No students available.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
                add(emptyLabel, BorderLayout.CENTER);
            } else {

                String[] columnNames = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
                Object[][] data = new Object[students.size()][6];

                for (int i = 0; i < students.size(); i++) {
                    Student s = students.get(i);
                    data[i][0] = s.getStudentId();
                    data[i][1] = s.getFullName();
                    data[i][2] = s.getAge();
                    data[i][3] = s.getGender();
                    data[i][4] = s.getDepartment();
                    data[i][5] = s.getGPA();
                }

                JTable table = new JTable(data, columnNames)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                table.setRowHeight(25);
                table.setFillsViewportHeight(true);
                table.setAutoCreateRowSorter(true);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                table.setSelectionBackground(Color.BLUE);
                table.setSelectionForeground(Color.WHITE);
                table.setShowGrid(true);

                table.addMouseListener(new java.awt.event.MouseAdapter() {
                    private void onRowClick(int modelRow, JTable table) {
                        String name = table.getValueAt(modelRow, 1).toString();
                        int id = Integer.parseInt(table.getValueAt(modelRow, 0).toString());
                        db.deleteStudent(db.searchByID(id));
                        JOptionPane.showMessageDialog(null, "Student Deleted Successfully");
                        showDeleteStudentWindow();
                    }
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int row = table.rowAtPoint(e.getPoint());
                        if (row >= 0) {
                            int modelRow = table.convertRowIndexToModel(row);
                            String id = table.getValueAt(modelRow, 0).toString();
                            int choice = JOptionPane.showConfirmDialog(
                                    null,
                                    "Do you want to delete this student \n" + "ID: " + id + "?", "Confirm Action", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (choice == JOptionPane.YES_OPTION) {
                                onRowClick(modelRow, table);
                            }
                        }
                    }
                });

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
            add(label, BorderLayout.NORTH);

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








