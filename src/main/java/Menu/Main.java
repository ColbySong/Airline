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

    private static JLabel invalidPassportOrPassword;

    private static GridBagConstraints c;

    public static void main(String[] args) {
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "1234");
            myStat = myConn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Airline");
        panel = new JPanel();
        c = new GridBagConstraints();
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
        c.gridy = 0;
        JLabel loginLabel = new JLabel("Passport Number");
        panel.add(loginLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        passportNoLogin = new JTextField(5);
        panel.add(passportNoLogin, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JLabel passwordLabel = new JLabel("Password");
        panel.add(passwordLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        passwordField = new JPasswordField(5);
        panel.add(passwordField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
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

        /**
         * Create New Account
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
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

        /**
         * Error Messages
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        invalidPassportOrPassword = new JLabel(" ");
        invalidPassportOrPassword.setForeground(Color.RED);
        panel.add(invalidPassportOrPassword, c);

        insertBlankSpace(0, 4);

        /**
         * Admin Login
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        JLabel adminLoginLabel = new JLabel("Login with your Admin ID");
        panel.add(adminLoginLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 5;
        adminIdLogin = new JTextField(5);
        panel.add(adminIdLogin, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
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

        frame.setVisible(true);
    }

    private static void insertBlankSpace(int x, int y) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = x;
        c.gridy = y;
        JLabel blank = new JLabel(" ");
        panel.add(blank, c);
    }

    private static boolean isValidLogin(String passportNo, char[] password) {
        String passwordString = new String(password);

        invalidPassportOrPassword.setText(" ");

        try {
            ResultSet results = Main.myStat.executeQuery("select passport_no, password from passengers where passport_no = " +
                    "\'" + passportNo + "\'" + " and password = " + "\'" + passwordString + "\'");

            if (results.isBeforeFirst() && results.next()) {
                if (results.getString("passport_no").equals(passportNo) && results.getString("password").equals(passwordString)) {
                    return true;
                }
            } else {
                invalidPassportOrPassword.setText("Invalid passport number or password");
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
