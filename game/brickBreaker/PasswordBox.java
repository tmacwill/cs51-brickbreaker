package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Jacob
 */
public class PasswordBox extends JFrame implements ActionListener {
    private static String OK = "Enter";
    private static String SKIP = "Skip";

    boolean passed = false;    // set to true when the user has entered the correct password
    boolean skipped = false;    // set to true if the user chooses not to enter a password

    private JTextField nameField;
    private JPasswordField passwordField;

    private String username, password;

    public PasswordBox() {
        super("Login");
        setBackground(Color.gray);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        nameField = new JTextField(20);
        JLabel ulabel = new JLabel("Username: ");
        ulabel.setLabelFor(nameField);

        passwordField = new JPasswordField(20);
        passwordField.setActionCommand(OK);
        passwordField.addActionListener(this);
        JLabel plabel = new JLabel("Password: ");
        plabel.setLabelFor(passwordField);

        JComponent buttonPane = createButtonPanel();

        JPanel textPane = new JPanel(new GridLayout(0,2));
        textPane.add(ulabel);
        textPane.add(nameField);
        textPane.add(plabel);
        textPane.add(passwordField);

        JPanel upperPane = new JPanel();
        upperPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        upperPane.add(textPane);
        upperPane.add(buttonPane);

        add(upperPane);
        add(new JLabel("Don't have an account?  Go to http://brickbreaker.zxq.net to sign up!"));

        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JComponent createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JButton okButton = new JButton("Enter");
        JButton skipButton = new JButton("Skip Login");

        okButton.setActionCommand(OK);
        skipButton.setActionCommand(SKIP);
        okButton.addActionListener(this);
        skipButton.addActionListener(this);

        p.add(okButton);
        p.add(skipButton);

        return p;
    }

    private void checkPassword() {
        passed = true; // check with website
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (OK.equals(cmd)) {   // Process the password
            password = new String(passwordField.getPassword());
            username = nameField.getText();
            checkPassword();
        }
        else if (SKIP.equals(cmd)) {
            skipped = true;
        }
    }

    /**
     * Waits until the user has passed the password box, either by entering the correct
     * username/password combo or by hitting the skip button.
     *
     * @return Returns the username if the user logged in correctly
     * Returns an empty string if the user merely skipped the box
     */
    public String getResult() {
        boolean finished = false;
        while (!finished) {
            if (skipped) {
                finished = true;
                return "";
            }
            else if (passed) {
                finished = true;
                return username;
            }
        }
        return "";
    }

}
