package main.java.Menu;

import main.java.Menu.Admin.*;
import main.java.Menu.User.Passenger.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by Daniel on 2016-03-20.
 */
public class Main {

    public static Statement myStat;
    public static JFrame frame;
    public static JPanel panel;

    private static JTextField passportNoLogin;
    private static JPasswordField passwordField;
    private static JTextField adminIdLogin;

    private static JLabel invalidPassportNoLabel;
    private static JLabel invalidPasswordLabel;

    public static void main(String[] args) {
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "1234");
            myStat = myConn.createStatement();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        frame = new JFrame("Airline");
        panel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        frame.add(panel);
        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * Passenger Login
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JLabel loginLabel = new JLabel("Login with your Passport Number");
        panel.add(loginLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 5;
        passportNoLogin = new JTextField(20);
        passportNoLogin.setSize(100, 10);
        panel.add(passportNoLogin, c);

        // TODO: UI
        JLabel passwordLabel = new JLabel("Password");
        panel.add(passwordLabel);

        // TODO: UI
        passwordField = new JPasswordField(30);
        panel.add(passwordField);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 8;
        c.gridy = 1;
        JButton loginAsPassengerButton = new JButton("Login as Passenger");
        loginAsPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValidLogin(passportNoLogin.getText(), passwordField.getPassword())) {
                    panel.setVisible(false);
                    Passenger p = new Passenger();
                    p.init(passportNoLogin.getText());
                }
            }
        });
        panel.add(loginAsPassengerButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        invalidPassportNoLabel = new JLabel();
        invalidPassportNoLabel.setForeground(Color.RED);
        panel.add(invalidPassportNoLabel, c);

        // TODO: UI
        invalidPasswordLabel = new JLabel();
        invalidPasswordLabel.setForeground(Color.RED);
        panel.add(invalidPasswordLabel);

        /**
         * Admin Login
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        final JLabel adminLoginLabel = new JLabel("Login with your Admin ID");
        panel.add(adminLoginLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 5;
        adminIdLogin = new JTextField(20);
        adminLoginLabel.setSize(100, 10);
        panel.add(adminIdLogin, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 8;
        c.gridy = 3;
        JButton adminIdLoginButton = new JButton("Login as Admin");
        panel.add(adminIdLoginButton, c);
        adminIdLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (adminIdLogin.getText().equals("j4w9a") || adminIdLogin.getText().equals("p7x8")
                        || adminIdLogin.getText().equals("v5e0b") || adminIdLogin.getText().equals("u3b9")) {
                    panel.setVisible(false);
                    AdminPanel ap = new AdminPanel();
                    ap.init();
                }
            }
        });

        /**
         * Create New Account
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 8;
        c.gridy = 4;
        JButton createPassengerAccount = new JButton("Create Passenger Account");
        createPassengerAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                CreatePassenger cp = new CreatePassenger();
                cp.init();
            }
        });
        panel.add(createPassengerAccount, c);

        frame.setVisible(true);
    }

    private static boolean isValidLogin(String passportNo, char[] password) {
        try {
            ResultSet mySet = Main.myStat.executeQuery("select passport_no, password from passengers where passport_no = \"" + passportNo + "\"");

            if (mySet.isBeforeFirst() && mySet.next()) {
                if (mySet.getString("passport_no").equals(passportNo) && mySet.getString("password").equals(new String(password))) {
                    invalidPassportNoLabel.setText("");
                    invalidPasswordLabel.setText("");
                    return true;
                } else {
                    if (!mySet.getString("passport_no").equals(passportNo)) {
                        invalidPassportNoLabel.setText("Invalid passport number, please try again.");
                        return false;
                    }
                    if (!mySet.getString("password").equals(new String(password))) {
                        invalidPasswordLabel.setText("Incorrect password, please try again.");
                    }
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
