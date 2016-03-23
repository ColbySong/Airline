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

    public static void main(String[] args) {
        try{
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
        frame.setSize(700, 700);
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

        JButton searchFlightsReserversPassenger = new JButton(("Search for Reserved Flights for a Passenger"));
        panel.add(searchFlightsReserversPassenger);
        searchFlightsReserversPassenger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryJoin2 qj2 = new QueryJoin2();
                qj2.init();
            }
        });

        frame.setVisible(true);
    }
}
