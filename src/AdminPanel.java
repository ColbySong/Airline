import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Colby on 2016-03-24.
 */
public class AdminPanel {

    public static JPanel panel;
    private JLabel label = new JLabel();
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"passport", "number of flights"};
    private JButton backButton;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);


        // back button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });

        panel.add(backButton);

        // search for passenger's flight
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

        // search for passenger's baggage
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

        JButton deletePassenger = new JButton(("Delete a Passenger"));
        panel.add(deletePassenger);
        deletePassenger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                DeletePassenger dp = new DeletePassenger();
                dp.init();
            }
        });

        JButton deleteBaggage = new JButton(("Delete a Baggage for a Passenger"));
        panel.add(deleteBaggage);
        deleteBaggage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                DeleteBaggage db = new DeleteBaggage();
                db.init();
            }
        });



        // search for passenger who booked all flights

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


    }

}
