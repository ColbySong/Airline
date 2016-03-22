import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-03-20.
 */
public class QueryFlights {
    private JPanel panel;
    private JComboBox departureAirportIDComboBox;
    private JScrollPane scrollPane;
    private JLabel label;
    private JButton getFlightsButton;
    private JButton backButton;
    private JTable table;
    private String[] columns = new String[] {
            "Flight Number", "Cost", "Depart From", "Departure Date", "Departure Time",
            "Arrive In", "Arrival Date", "Arrival Time", "Seats Remaining"};
    private Object[][] data;


    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        label = new JLabel("Filter by Departing Airport");
        panel.add(label);

        departureAirportIDComboBox = new JComboBox();
        try{
            ResultSet mySet = Main.myStat.executeQuery("select * from flights");

            List<String> results = new ArrayList<String>();

            while(mySet.next()){
                String result = mySet.getString("airportid_depart");
                if (!results.contains(result)) {
                    results.add(result);
                }
            }

            for (int i = 0; i < results.size(); i++) {
                departureAirportIDComboBox.addItem(results.get(i));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        panel.add(departureAirportIDComboBox);

        getFlightsButton = new JButton("Get Flights");
        getFlightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFlights(departureAirportIDComboBox.getSelectedItem());
            }

        });
        panel.add(getFlightsButton);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton);
    }

    private void searchFlights(Object selectedItem) {
        try{
            ResultSet mySet = Main.myStat.executeQuery("select flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, data_arrive, time_arrive, available_seats from flights where airportid_depart = \"" + selectedItem + "\"");

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
