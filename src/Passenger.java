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

    public static String passengerPassportNo;
    public static String passengerFirstName;
    public static String passengerLastName;

    public static JPanel panel;

    public void init(String passportNo) {
        this.passengerPassportNo = passportNo;

        try {
            ResultSet mySet = Main.myStat.executeQuery("select first_name, last_name from passengers where passport_no = \"" + passportNo + "\"");
            if (mySet.next()) {
                passengerFirstName = mySet.getString("first_name");
                passengerLastName = mySet.getString("last_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        JLabel loggedInLabel = new JLabel("You are logged in as " + passengerFirstName + " " + passengerLastName + "!");
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
                mf.init(passengerPassportNo);
                panel.setVisible(false);
            }
        });
        panel.add(myFlightsButton);

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
