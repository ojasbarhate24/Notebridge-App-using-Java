import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class MainAppFrame extends JFrame {

    private ArrayList<User> users;
    private ArrayList<Classroom> classrooms;
    private User currentUser;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Startup panel
    private JButton newUserBtn, existingUserBtn;

    // Login panel
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JButton loginBtn, goToSignupBtn;

    // Signup panel
    private JTextField signupEmailField;
    private JPasswordField signupPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton signupBtn, goToLoginBtn;

    // Teacher panel
    private JTextField classNameField, noteTitleField;
    private JTextArea noteContentArea, teacherNotesArea;
    private JComboBox<String> classComboBox;
    private JButton logoutTeacherBtn, postNoteBtn, createClassBtn;

    // Student panel
    private JTextField studentCodeField;
    private JTextArea studentNotesArea;
    private JButton joinClassBtn, logoutStudentBtn;

    public MainAppFrame() {
        setTitle("Classroom Notes Platform");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        users = new ArrayList<>();
        classrooms = new ArrayList<>();
        loadUsers();
        loadClassrooms();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        initStartupPanel();
        initLoginPanel();
        initSignupPanel();
        initTeacherPanel();
        initStudentPanel();

        cardLayout.show(mainPanel, "startup");
    }

    // -------------------- STARTUP --------------------
    private void initStartupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Welcome! Choose Option");
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(label, gbc);

        newUserBtn = new JButton("New User");
        existingUserBtn = new JButton("Existing User");
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1;
        panel.add(newUserBtn, gbc);
        gbc.gridx = 1;
        panel.add(existingUserBtn, gbc);

        newUserBtn.addActionListener(e -> cardLayout.show(mainPanel, "signup"));
        existingUserBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        mainPanel.add(panel, "startup");
    }

    // -------------------- LOGIN --------------------
    private void initLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        loginEmailField = new JTextField(20);
        panel.add(loginEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPasswordField = new JPasswordField(20);
        panel.add(loginPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        loginBtn = new JButton("Login");
        panel.add(loginBtn, gbc);
        gbc.gridx = 1;
        goToSignupBtn = new JButton("Sign Up");
        panel.add(goToSignupBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        goToSignupBtn.addActionListener(e -> cardLayout.show(mainPanel, "signup"));

        mainPanel.add(panel, "login");
    }

    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();

        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                currentUser = u;
                if (u.getRole().equals("teacher")) {
                    populateClassComboBox();
                    cardLayout.show(mainPanel, "teacher");
                } else {
                    cardLayout.show(mainPanel, "student");
                }
                loginEmailField.setText("");
                loginPasswordField.setText("");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid login!");
    }

    // -------------------- SIGNUP --------------------
    private void initSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Sign Up");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        signupEmailField = new JTextField(20);
        panel.add(signupEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        signupPasswordField = new JPasswordField(20);
        panel.add(signupPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(new String[]{"teacher", "student"});
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        signupBtn = new JButton("Sign Up");
        panel.add(signupBtn, gbc);
        gbc.gridx = 1;
        goToLoginBtn = new JButton("Go to Login");
        panel.add(goToLoginBtn, gbc);

        signupBtn.addActionListener(e -> handleSignup());
        goToLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        mainPanel.add(panel, "signup");
    }

    private void handleSignup() {
        String email = signupEmailField.getText().trim();
        String password = new String(signupPasswordField.getPassword()).trim();
        String role = roleComboBox.getSelectedItem().toString();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter all fields!");
            return;
        }

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                JOptionPane.showMessageDialog(this, "Email exists!");
                return;
            }
        }

        users.add(new User(email, password, role));
        saveUsers();
        JOptionPane.showMessageDialog(this, "Account created! Login now.");
        signupEmailField.setText("");
        signupPasswordField.setText("");
        cardLayout.show(mainPanel, "login");
    }

    // -------------------- TEACHER PANEL --------------------
    private void initTeacherPanel() {
        JPanel teacherPanel = new JPanel(new BorderLayout());

        JPanel top = new JPanel();
        classNameField = new JTextField(15);
        createClassBtn = new JButton("Create Classroom");
        logoutTeacherBtn = new JButton("Logout");
        top.add(new JLabel("Class Name:"));
        top.add(classNameField);
        top.add(createClassBtn);
        top.add(logoutTeacherBtn);
        teacherPanel.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        classComboBox = new JComboBox<>();
        noteTitleField = new JTextField();
        noteContentArea = new JTextArea(10, 40);
        postNoteBtn = new JButton("Post Note");
        teacherNotesArea = new JTextArea(10, 40);
        teacherNotesArea.setEditable(false);

        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.add(new JLabel("Note Title:"), BorderLayout.NORTH);
        postPanel.add(noteTitleField, BorderLayout.CENTER);
        postPanel.add(new JScrollPane(noteContentArea), BorderLayout.SOUTH);
        postPanel.add(postNoteBtn, BorderLayout.EAST);

        center.add(classComboBox, BorderLayout.NORTH);
        center.add(postPanel, BorderLayout.CENTER);
        center.add(new JScrollPane(teacherNotesArea), BorderLayout.SOUTH);

        teacherPanel.add(center, BorderLayout.CENTER);

        createClassBtn.addActionListener(e -> createClassroom());
        postNoteBtn.addActionListener(e -> postNote());
        classComboBox.addActionListener(e -> loadClassNotes());
        logoutTeacherBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "startup");
        });

        mainPanel.add(teacherPanel, "teacher");
    }

    private void createClassroom() {
        String name = classNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter class name!");
            return;
        }
        Classroom cls = new Classroom(name);
        classrooms.add(cls);
        saveClassrooms();
        populateClassComboBox();
        JOptionPane.showMessageDialog(this, "Classroom created! Code: " + cls.getAccessCode());
        classNameField.setText("");
    }

    private void populateClassComboBox() {
        if (classComboBox == null) return;
        classComboBox.removeAllItems();
        for (Classroom cls : classrooms) {
            classComboBox.addItem(cls.getClassName() + " (" + cls.getAccessCode() + ")");
        }
    }

    private void postNote() {
        if (classComboBox.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Select a class!");
            return;
        }
        String selected = classComboBox.getSelectedItem().toString();
        String code = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
        String title = noteTitleField.getText().trim();
        String content = noteContentArea.getText().trim();
        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter title and content!");
            return;
        }

        for (Classroom cls : classrooms) {
            if (cls.getAccessCode().equals(code)) {
                cls.addNote(title, currentUser.getEmail(), content);
                saveClassrooms();
                JOptionPane.showMessageDialog(this, "Note posted!");
                noteTitleField.setText("");
                noteContentArea.setText("");
                loadClassNotes();
                return;
            }
        }
    }

    private void loadClassNotes() {
        teacherNotesArea.setText("");
        if (classComboBox.getSelectedIndex() < 0) return;
        String selected = classComboBox.getSelectedItem().toString();
        String code = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
        for (Classroom cls : classrooms) {
            if (cls.getAccessCode().equals(code)) {
                for (Note n : cls.getNotes()) {
                    teacherNotesArea.append(n.toString() + "\n----------------\n");
                }
                return;
            }
        }
    }

    // -------------------- STUDENT PANEL --------------------
    private void initStudentPanel() {
        JPanel studentPanel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        studentCodeField = new JTextField(15);
        joinClassBtn = new JButton("Join Class");
        logoutStudentBtn = new JButton("Logout");
        top.add(new JLabel("Enter Classroom Code:"));
        top.add(studentCodeField);
        top.add(joinClassBtn);
        top.add(logoutStudentBtn);
        studentNotesArea = new JTextArea(20, 50);
        studentNotesArea.setEditable(false);
        studentPanel.add(top, BorderLayout.NORTH);
        studentPanel.add(new JScrollPane(studentNotesArea), BorderLayout.CENTER);

        joinClassBtn.addActionListener(e -> joinClassroom());
        logoutStudentBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "startup");
        });

        mainPanel.add(studentPanel, "student");
    }

    private void joinClassroom() {
        String code = studentCodeField.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter class code!");
            return;
        }
        for (Classroom cls : classrooms) {
            if (cls.getAccessCode().equals(code)) {
                studentNotesArea.setText("");
                for (Note n : cls.getNotes()) {
                    studentNotesArea.append(n.toString() + "\n----------------\n");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Class code not found!");
    }

    // -------------------- DATA HANDLING --------------------
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (Exception e) {
            users = new ArrayList<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClassrooms() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("classrooms.ser"))) {
            classrooms = (ArrayList<Classroom>) ois.readObject();
        } catch (Exception e) {
            classrooms = new ArrayList<>();
        }
    }

    private void saveClassrooms() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("classrooms.ser"))) {
            oos.writeObject(classrooms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainAppFrame().setVisible(true));
    }
}
