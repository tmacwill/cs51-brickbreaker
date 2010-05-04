package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import brickBreaker.web.*;

/**
 *
 * @author Jacob
 */
public class PasswordBox extends JFrame {
    private static String OK = "Enter";
    private static String SKIP = "Skip";

    private boolean passed = false; // set to true when the user has entered the correct password
    private boolean skipped = false;

    private JTextField nameField;
    private JPasswordField passwordField;


    public PasswordBox() {
        super("Login");
        setBackground(Color.gray);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        nameField = new JTextField(20);
        JLabel ulabel = new JLabel("Username: ");
        ulabel.setLabelFor(nameField);

        passwordField = new JPasswordField(20);
        passwordField.setActionCommand(OK);
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
        add(new JLabel("Don't have an account?  Go to http://bit.ly/cs51-brickbreaker to sign up!"));

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

        // check username and password when OK is clicked
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = nameField.getText();
                String password = passwordField.getText();
                if (WebService.verifyUser(username, password)) {
                    // save user credentials in UserConfig object
                    passed = true;
                    UserConfig.getInstance().setUsername(username);
                    UserConfig.getInstance().setPassword(password);
                }
                else {
                    // show error message if combination is incorrect
                    JOptionPane.showMessageDialog(null, "Incorrect username/password combination");
                }
            }
        });

        // skip login, leaving UserConfig parameters blank
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                skipped = true;
            }
        });

        p.add(okButton);
        p.add(skipButton);

        return p;
    }

    /**
     * Check if user is logged in
     *
     * @return True if user is logged in, else false
     */
    public boolean checkLogin() {
        return passed;
    }

    /**
     * Check if user has skipped login screen
     *
     * @return True if login has been skipped, else false
     */
    public boolean checkSkipped() {
        return skipped;
    }

}
