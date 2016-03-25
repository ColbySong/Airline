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

    private JLabel passportNoErrorLabel;
    private JLabel firstNameErrorLabel;
    private JLabel lastNameErrorLabel;


    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        JLabel passportNoLabel = new JLabel("Passport Number:");
        panel.add(passportNoLabel);

        passportNoField = new JTextField(20);
        passportNoField.setSize(100, 10);
        panel.add(passportNoField);

        passportNoErrorLabel = new JLabel("");
        panel.add(passportNoErrorLabel);

        JLabel firstNameLabel = new JLabel("First Name:");
        panel.add(firstNameLabel);

        firstNameField = new JTextField(20);
        firstNameField.setSize(100, 10);
        panel.add(firstNameField);

        firstNameErrorLabel = new JLabel("");
        panel.add(firstNameErrorLabel);

        JLabel lastNameLabel = new JLabel("Last Name:");
        panel.add(lastNameLabel);

        lastNameField = new JTextField(20);
        lastNameField.setSize(100, 10);
        panel.add(lastNameField);

        lastNameErrorLabel = new JLabel("");
        panel.add(lastNameErrorLabel);

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
        panel.add(createPassengerButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton);
    }

    private void createPassenger() {
        String passportNo = passportNoField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        try {
            int maxPassengerID = 0;
            ResultSet mySet = Main.myStat.executeQuery("select max(passenger_id) as maxPID from passengers");

            if (mySet.next()) {
                maxPassengerID = mySet.getInt("maxPID");
            }

            if (maxPassengerID != 0) {
                Main.myStat.executeUpdate("insert into passengers values ('" + passportNo + "', '" + (maxPassengerID + 1) + "', '" + firstName + "', '" + lastName + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        passportNoErrorLabel.setText("");
        firstNameErrorLabel.setText("");
        lastNameErrorLabel.setText("");

        if (passportNoField.getText().equals("") || firstNameField.getText().equals("") || lastNameField.getText().equals("")) {
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
