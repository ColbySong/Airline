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
    private GridBagConstraints c;

    private JComboBox departureAirportIDComboBox;
    private JComboBox arrivalAirportIDComboBox;
    private JComboBox sortByComboBox;

    private JCheckBox arrivalAirportCB;
    private JCheckBox departingAirportCB;
    private JCheckBox arrivalDateCB;
    private JCheckBox arrivalTimeCB;
    private JCheckBox departingDateCB;
    private JCheckBox departingTimeCB;

    private JScrollPane scrollPane;

    private JLabel flightDetailLabel;
    private JLabel departureAirportLabel;
    private JLabel arrivalAirportLabel;
    private JLabel sortByLabel;

    private JButton backButton;
    private JButton filterButton;

    private JTable table;

    private String[] columns = new String[]{
            "Flight Number", "Cost", "Depart From", "Departure Date", "Departure Time",
            "Arrive In", "Arrival Date", "Arrival Time", "Seats Remaining"};
    private Object[][] data;

    private String filteredBy = "";

    public void init() {

        panel = new JPanel();
        c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 0;
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton, c);

        // ****** //

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        arrivalAirportCB = new JCheckBox("Arriving Airport");
        panel.add(arrivalAirportCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        departingAirportCB = new JCheckBox("Departing Airport");
        panel.add(departingAirportCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        arrivalDateCB = new JCheckBox("Arriving Date");
        panel.add(arrivalDateCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        arrivalTimeCB = new JCheckBox("Arriving Time");
        panel.add(arrivalTimeCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        departingDateCB = new JCheckBox("Departing Date");
        panel.add(departingDateCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        departingTimeCB = new JCheckBox("Departing Time");
        panel.add(departingTimeCB, c);

        // ****** //

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        departureAirportLabel = new JLabel("Select Departing Airport");
        panel.add(departureAirportLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        departureAirportIDComboBox = new JComboBox();
        generateDepartureDropDown();
        panel.add(departureAirportIDComboBox, c);

        // ****** //

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        arrivalAirportLabel = new JLabel("Select Arriving Airport");
        panel.add(arrivalAirportLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 3;
        arrivalAirportIDComboBox = new JComboBox();
        generateArrivalDropDown();
        panel.add(arrivalAirportIDComboBox, c);

        // ****** //

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 3;
        sortByLabel = new JLabel("Sort By: ");
        panel.add(sortByLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 3;
        sortByComboBox = new JComboBox();
        generateSortByDropDown();
        panel.add(sortByComboBox, c);

        // ****** //

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        flightDetailLabel = new JLabel("All Flights " + filteredBy);
        panel.add(flightDetailLabel, c);

        // ****** //

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 4;
        filterButton = new JButton("Filter Search");
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(filterButton, c);

        displayAllFlightDetails();
    }

    private void generateDepartureDropDown() {
        try {
            ResultSet departureSet = Main.myStat.executeQuery("select airportid_depart from flights");

            List<String> results = new ArrayList<>();

            while (departureSet.next()) {
                String result = departureSet.getString("airportid_depart");
                if (!results.contains(result)) {
                    results.add(result);
                }
            }

            for (int i = 0; i < results.size(); i++) {
                departureAirportIDComboBox.addItem(results.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateArrivalDropDown() {
        try {
            ResultSet arrivalSet = Main.myStat.executeQuery("select airportid_arrive from flights");

            List<String> results = new ArrayList<>();

            while (arrivalSet.next()) {
                String result = arrivalSet.getString("airportid_arrive");
                if (!results.contains(result)) {
                    results.add(result);
                }
            }

            for (int i = 0; i < results.size(); i++) {
                arrivalAirportIDComboBox.addItem(results.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateSortByDropDown() {
        String[] sortByTypes = new String[] {"Arrival Date", "Departure Data", "Arrival Time", "Departure Time", "Cost"};

        for (int i = 0; i < sortByTypes.length; i++) {
            sortByComboBox.addItem(sortByTypes[i]);
        }
    }

    private void displayAllFlightDetails() {
        try {
            ResultSet allDetailsSet = Main.myStat.executeQuery("select flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, data_arrive, time_arrive, available_seats from flights");

            int rowCount = 0;
            if (allDetailsSet.last()) {
                rowCount = allDetailsSet.getRow();
                allDetailsSet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while (allDetailsSet.next()) {
                for (int i = 0; i < columns.length; i++) {
                    data[j][i] = allDetailsSet.getObject(i + 1);
                }
                j++;
            }
            refreshTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        if (scrollPane != null) {
            panel.remove(scrollPane);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 10;
        c.gridheight = 0;
        table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }
}
