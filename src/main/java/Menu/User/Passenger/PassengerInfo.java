package main.java.Menu.User.Passenger;

import main.java.Menu.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Daniel on 2016-03-25.
 */
public class PassengerInfo {

    public static JPanel panel;

    private JTextField passportNoField;
    private JTextField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;

    private JLabel passportNoErrorLabel;
    private JLabel passwordErrorLabel;
    private JLabel firstNameErrorLabel;
    private JLabel lastNameErrorLabel;
    private JLabel updateLabel;

    public void init() {
        panel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JLabel passportNoLabel = new JLabel("Passport Number:");
        panel.add(passportNoLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        passportNoField = new JTextField(Passenger.passportNo, 20);
        passportNoField.setSize(100, 10);
        panel.add(passportNoField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        passportNoErrorLabel = new JLabel("");
        passportNoErrorLabel.setForeground(Color.RED);
        panel.add(passportNoErrorLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        passwordField = new JTextField(Passenger.password, 30);
        passwordField.setSize(100, 10);
        panel.add(passwordField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setForeground(Color.RED);
        panel.add(passwordErrorLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        JLabel firstNameLabel = new JLabel("First Name:");
        panel.add(firstNameLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 5;
        firstNameField = new JTextField(Passenger.firstName, 20);
        firstNameField.setSize(100, 10);
        panel.add(firstNameField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 6;
        firstNameErrorLabel = new JLabel("");
        firstNameErrorLabel.setForeground(Color.RED);
        panel.add(firstNameErrorLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        JLabel lastNameLabel = new JLabel("Last Name:");
        panel.add(lastNameLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 7;
        lastNameField = new JTextField(Passenger.lastName, 20);
        lastNameField.setSize(100, 10);
        panel.add(lastNameField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 8;
        lastNameErrorLabel = new JLabel("");
        lastNameErrorLabel.setForeground(Color.RED);
        panel.add(lastNameErrorLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 9;
        updateLabel = new JLabel("");
        panel.add(updateLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 10;
        JButton updateInfoButton = new JButton("Update Information");
        updateInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValid()) {
                    updatePassenger();
                }
            }
        });
        panel.add(updateInfoButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 10;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Passenger.panel.setVisible(true);
            }
        });
        panel.add(backButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 10;
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(logoutButton, c);
    }

    private void updatePassenger() {
        String passportNo = passportNoField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        try {
            Main.myStat.executeUpdate("update passengers set passport_no = '" + passportNo
                    + "', password = '" + password + "', first_name = '" + firstName + "', last_name = '"
                    + lastName + "' where passport_no = '" + Passenger.passportNo + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        passportNoErrorLabel.setText("");
        firstNameErrorLabel.setText("");
        lastNameErrorLabel.setText("");
        updateLabel.setText("");

        if (!passportNoField.getText().matches("([A-Z])\\d{6}") || passportNoField.getText().equals("")
                || passwordField.getText().equals("") || firstNameField.getText().equals("")
                || lastNameField.getText().equals("")) {
            if (!passportNoField.getText().matches("([A-Z])\\d{6}")) {
                passportNoErrorLabel.setText("Please enter a valid passport number (ie. A123456)");
            }

            if (passportNoField.getText().equals("")) {
                passportNoErrorLabel.setText("Please enter your passport number.");
            }

            if (passwordField.getText().equals("")) {
                passwordErrorLabel.setText("Please enter a password.");
            }

            if (firstNameField.getText().equals("")) {
                firstNameErrorLabel.setText("Please enter your first name.");
            }

            if (lastNameField.getText().equals("")) {
                lastNameErrorLabel.setText("Please enter your last name.");
            }

            updateLabel.setForeground(Color.RED);
            updateLabel.setText("Unable to update your information.");

            return false;
        }

        updateLabel.setForeground(Color.BLACK);
        updateLabel.setText("Update successful!");

        return true;
    }
}
