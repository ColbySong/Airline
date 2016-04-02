package main.java.Menu.Admin;

import main.java.Menu.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Daniel on 2016-03-24.
 */
public class CreatePassenger {

    public static JPanel panel;

    private JTextField passportNoField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField passwordField;

    private JLabel passportNoErrorLabel;
    private JLabel firstNameErrorLabel;
    private JLabel lastNameErrorLabel;
    private JLabel passwordErrorLabel;


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
        passportNoField = new JTextField(20);
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
        passwordField = new JTextField(30);
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
        firstNameField = new JTextField(20);
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
        lastNameField = new JTextField(20);
        lastNameField.setSize(100, 10);
        panel.add(lastNameField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 8;
        lastNameErrorLabel = new JLabel("");
        lastNameErrorLabel.setForeground(Color.RED);
        panel.add(lastNameErrorLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        JButton createPassengerButton = new JButton("Create Passenger Account");
        createPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValid()) {
                    createPassenger();
                    panel.setVisible(false);
                    Main.panel.setVisible(true);
                }
            }
        });
        panel.add(createPassengerButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 9;
        JButton backButton = new JButton("Cancel");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton, c);
    }

    private void createPassenger() {
        String passportNo = passportNoField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getText();

        try {
            int maxPassengerID = 0;
            ResultSet mySet = Main.myStat.executeQuery("select max(passenger_id) as maxPID from passengers");

            if (mySet.next()) {
                maxPassengerID = mySet.getInt("maxPID");
            }

            if (maxPassengerID != 0) {
                Main.myStat.executeUpdate("insert into passengers values ('" + passportNo + "', '" + (maxPassengerID + 1)
                        + "', '" + firstName + "', '" + lastName + "', '" + password + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        passportNoErrorLabel.setText("");
        passwordErrorLabel.setText("");
        firstNameErrorLabel.setText("");
        lastNameErrorLabel.setText("");

        if (!passportNoField.getText().matches("([A-Z])\\d{6}") || passportNoField.getText().equals("") ||
                passwordField.getText().equals("") || firstNameField.getText().equals("") ||
                lastNameField.getText().equals("")) {
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

            return false;
        }

        String passportNo = passportNoField.getText();
        int count = 1;

        try {
            ResultSet mySet = Main.myStat.executeQuery("select count(*) as count from passengers where passport_no = '" + passportNo + "'");

            if (mySet.next()) {
                count = mySet.getInt("count");
            }

            return count == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
