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
    private JTextField firstNameField;
    private JTextField lastNameField;

    private JLabel passportNoErrorLabel;
    private JLabel firstNameErrorLabel;
    private JLabel lastNameErrorLabel;
    private JLabel updateLabel;

    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        JLabel passportNoLabel = new JLabel("Passport Number:");
        panel.add(passportNoLabel);

        passportNoField = new JTextField(Passenger.passengerPassportNo, 20);
        passportNoField.setSize(100, 10);
        panel.add(passportNoField);

        passportNoErrorLabel = new JLabel("");
        panel.add(passportNoErrorLabel);

        JLabel firstNameLabel = new JLabel("First Name:");
        panel.add(firstNameLabel);

        firstNameField = new JTextField(Passenger.passengerFirstName, 20);
        firstNameField.setSize(100, 10);
        panel.add(firstNameField);

        firstNameErrorLabel = new JLabel("");
        panel.add(firstNameErrorLabel);

        JLabel lastNameLabel = new JLabel("Last Name:");
        panel.add(lastNameLabel);

        lastNameField = new JTextField(Passenger.passengerLastName, 20);
        lastNameField.setSize(100, 10);
        panel.add(lastNameField);

        lastNameErrorLabel = new JLabel("");
        panel.add(lastNameErrorLabel);

        updateLabel = new JLabel("");
        panel.add(updateLabel);

        JButton updateInfoButton = new JButton("Update Information");
        updateInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValid()) {
                    updatePassenger();
                }
            }
        });
        panel.add(updateInfoButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Passenger.panel.setVisible(true);
            }
        });
        panel.add(backButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(logoutButton);
    }

    private void updatePassenger() {
        String passportNo = passportNoField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        try {
            Main.myStat.executeUpdate("update passengers set passport_no = '" + passportNo + "', first_name = '" + firstName + "', last_name = '" + lastName + "' where passport_no = '" + Passenger.passengerPassportNo + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        passportNoErrorLabel.setText("");
        firstNameErrorLabel.setText("");
        lastNameErrorLabel.setText("");
        updateLabel.setText("");

        if (!passportNoField.getText().matches("([A-Z])\\d{6}") || passportNoField.getText().equals("") || firstNameField.getText().equals("") || lastNameField.getText().equals("")) {
            if (!passportNoField.getText().matches("([A-Z])\\d{6}")) {
                passportNoErrorLabel.setText("Please enter a valid passport number (ie. A123456)");
            }

            if (passportNoField.getText().equals("")) {
                passportNoErrorLabel.setText("Please enter your passport number.");
            }

            if (firstNameField.getText().equals("")) {
                firstNameErrorLabel.setText("Please enter your first name.");
            }

            if (lastNameField.getText().equals("")) {
                lastNameErrorLabel.setText("Please enter your last name.");
            }

            updateLabel.setText("Unable to update your information.");

            return false;
        }

        updateLabel.setText("Update successful!");

        return true;
    }
}
