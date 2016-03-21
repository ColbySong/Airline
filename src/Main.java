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

    public static void main(String[] args) {
        // write your code here
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "1234");
            myStat = myConn.createStatement();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Airline");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        frame.add(panel);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton searchFlightButton = new JButton("Search for flights!");
        panel.add(searchFlightButton);
        searchFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryFlights qf = new QueryFlights();
                qf.init();
            }
        });

        JButton searchPassengerButton = new JButton("Search for Passenger");
        panel.add(searchPassengerButton);
        searchPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainComboBox.run(new MainComboBox());
            }
        });

        frame.setVisible(true);
    }
}
