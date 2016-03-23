import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Daniel on 2016-03-23.
 */
public class MyFlights {

    private static final String[] columns = new String[] {
            "Flight Number", "Cost", "Depart From", "Departure Date", "Departure Time",
            "Arrive In", "Arrival Date", "Arrival Time", "Seats Remaining"};
    private Object[][] data;

    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable table;


    public void init(String passportNo) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        JLabel loggedInLabel = new JLabel("You are logged in as " + Passenger.passengerFirstName + " " + Passenger.passengerLastName + "!");
        panel.add(loggedInLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(logoutButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Passenger.panel.setVisible(true);
            }
        });
        panel.add(backButton);

        searchFlights(passportNo);
    }

    private void searchFlights(Object selectedItem) {
        try{
            ResultSet mySet = Main.myStat.executeQuery("select flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, date_arrive, time_arrive, available_seats from reserves natural join flights where passport_no = \"" + selectedItem + "\"");

            int rowCount = 0;
            if (mySet.last()) {
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for (int i = 0; i < 9; i++) {
                    data[j][i] = mySet.getObject(i+1);
                }
                j++;
            }

            refreshTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        if (scrollPane != null) {
            panel.remove(scrollPane);
        }

        table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        panel.revalidate();
        panel.repaint();
    }
}
