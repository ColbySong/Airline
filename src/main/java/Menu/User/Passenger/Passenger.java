package main.java.Menu.User.Passenger;

import main.java.Menu.*;
import main.java.Menu.User.Flight.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Daniel on 2016-03-22.
 */
public class Passenger {

    public static String passportNo;
    public static String firstName;
    public static String lastName;
    public static String password;

    public static JPanel panel;

    public void init(String passportNo) {
        Passenger.passportNo = passportNo;

        try {
            ResultSet mySet = Main.myStat.executeQuery("select first_name, last_name, password from passengers where passport_no = \"" + passportNo + "\"");
            if (mySet.next()) {
                firstName = mySet.getString("first_name");
                lastName = mySet.getString("last_name");
                password = mySet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        JLabel loggedInLabel = new JLabel("You are logged in as " + firstName + " " + lastName + "!");
        panel.add(loggedInLabel);

        JButton searchFlightButton = new JButton("Search for Flights");
        panel.add(searchFlightButton);
        searchFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryFlights qf = new QueryFlights();
                qf.init();
            }
        });

        JButton myFlightsButton = new JButton("My Flights");
        myFlightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFlights mf = new MyFlights();
                mf.init(Passenger.passportNo);
                panel.setVisible(false);
            }
        });
        panel.add(myFlightsButton);

        JButton updatePassengerButton = new JButton("My Info");
        updatePassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PassengerInfo pi = new PassengerInfo();
                pi.init();
                panel.setVisible(false);
            }
        });
        panel.add(updatePassengerButton);

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
}
