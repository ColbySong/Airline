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
    private static JLabel invalidPassportNoLabel;

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
        panel.setLayout(new FlowLayout());
        frame.add(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JButton searchPassengerButton = new JButton("Search for Passengers");
        panel.add(searchPassengerButton);
        searchPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryPassengers qp = new QueryPassengers();
                qp.init();
            }
        });

        JButton searchBaggagesForPassengerButton = new JButton(("Search for Baggages for a Passenger"));
        panel.add(searchBaggagesForPassengerButton);
        searchBaggagesForPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryJoin qj = new QueryJoin();
                qj.init();
            }
        });

        JButton searchFlightsReservesPassenger = new JButton(("Search for Reserved Flights for a Passenger"));
        panel.add(searchFlightsReservesPassenger);
        searchFlightsReservesPassenger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryJoin2 qj2 = new QueryJoin2();
                qj2.init();
            }
        });

        JLabel loginLabel = new JLabel("Login with your Passport Number");
        panel.add(loginLabel);

        passportNoLogin = new JTextField(20);
        passportNoLogin.setSize(100, 10);
        panel.add(passportNoLogin);

        JButton loginAsPassengerButton = new JButton("Login as Passenger");
        loginAsPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValidPassenger(passportNoLogin.getText())) {
                    panel.setVisible(false);
                    Passenger p = new Passenger();
                    p.init(passportNoLogin.getText());
                }
            }
        });
        panel.add(loginAsPassengerButton);

        invalidPassportNoLabel = new JLabel();
        panel.add(invalidPassportNoLabel);

        frame.setVisible(true);
    }

    private static boolean isValidPassenger(String p) {
        try {
            ResultSet mySet = Main.myStat.executeQuery("select passport_no from passengers where passport_no = \"" + p + "\"");

            if (mySet.isBeforeFirst() && mySet.next() && mySet.getString("passport_no").equals(p)) {
                invalidPassportNoLabel.setText("");
                return true;
            } else {
                invalidPassportNoLabel.setText("Invalid passport number, please try again");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
