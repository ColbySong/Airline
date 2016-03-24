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
    private static JTextField adminIdLogin;
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
        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
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

        JButton searchPassengerDivisionButton = new JButton("Search for Passenger who booked all flights");
        panel.add(searchPassengerDivisionButton);
        searchPassengerDivisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryDivision qd = new QueryDivision();
                qd.init();
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

        // passenger login
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


        //admin login
        final JLabel adminLoginLabel = new JLabel("Login with your admin ID");
        panel.add(adminLoginLabel);

        adminIdLogin = new JTextField(20);
        adminLoginLabel.setSize(100, 10);
        panel.add(adminIdLogin);

        JButton adminIdLoginButton = new JButton("Login as Admin");
        panel.add(adminIdLoginButton);
        adminIdLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                AdminPanel ap = new AdminPanel();
                ap.init();
                // TODO: create valid admin usernames
            }
        });

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
