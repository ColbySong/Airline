import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    private List<String> filterColumns = new ArrayList<>();

    private String filteredBy = "";

    private String select_clause = "select ";
    private String from_clause = "from flights ";
    private String where_clause = "where ";
    private String group_by = "group by ";

    private Boolean select_triggered = false;
    private Boolean where_triggered = false;
    private Boolean groupby_triggered = false;

    public void init() {

        panel = new JPanel();
        c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        /**
         * Back button layout
         */
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

        /**
         * Filtering Checkboxes (select ...)
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        arrivalAirportCB = new JCheckBox("Arriving Airport");
        arrivalAirportCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (arrivalAirportCB.isSelected()) {
                    filterColumns.add("Arriving In");
                    select_clause += "airportid_arrive, ";
                }
                else {
                    filterColumns.remove("Arriving In");
                    select_clause = select_clause.replace("airportid_arrive, ", "");
                }
            }
        });
        panel.add(arrivalAirportCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        departingAirportCB = new JCheckBox("Departing Airport");
        departingAirportCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (departingAirportCB.isSelected()) {
                    filterColumns.add("Depart From");
                    select_clause += "airportid_depart, ";
                }
                else {
                    filterColumns.remove("Depart From");
                    select_clause = select_clause.replace("airportid_depart, ", "");
                }
            }
        });
        panel.add(departingAirportCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        arrivalDateCB = new JCheckBox("Arriving Date");
        arrivalDateCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (arrivalDateCB.isSelected()) {
                    filterColumns.add("Arrival Date");
                    select_clause += "date_arrive, ";
                }
                else {
                    filterColumns.remove("Arrival Date");
                    select_clause = select_clause.replace("date_arrive, ", "");
                }
            }
        });
        panel.add(arrivalDateCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        arrivalTimeCB = new JCheckBox("Arriving Time");
        arrivalTimeCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (arrivalTimeCB.isSelected()) {
                    filterColumns.add("Arrival Time");
                    select_clause += "time_arrive, ";
                }
                else {
                    filterColumns.remove("Arrival Time");
                    select_clause = select_clause.replace("time_arrive, ", "");
                }
            }
        });
        panel.add(arrivalTimeCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        departingDateCB = new JCheckBox("Departing Date");
        departingDateCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (departingDateCB.isSelected()) {
                    filterColumns.add("Departure Date");
                    select_clause += "date_depart, ";
                }
                else {
                    filterColumns.remove("Departure Date");
                    select_clause = select_clause.replace("date_depart, ", "");
                }
            }
        });
        panel.add(departingDateCB, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        departingTimeCB = new JCheckBox("Departing Time");
        departingTimeCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (departingTimeCB.isSelected()) {
                    filterColumns.add("Departure Time");

                    select_clause += "time_depart, ";
                }
                else {
                    filterColumns.remove("Departure Time");
                    select_clause = select_clause.replace("time_depart, ", "");
                }
            }
        });
        panel.add(departingTimeCB, c);

        /**
         * Departing Airport Dropdown (where airportid_depart = ...)
         */
        // TODO: implement these where queries

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

        /**
         * Arriving Airport Dropdown (where airportid_arrive = ...)
         */
        // TODO: implement these where queries

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

        /**
         * Sort by filter (group by ...)
         */
        // TODO: implement these group by queries

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

        /**
         * Flight Table Title with filter suffix
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        flightDetailLabel = new JLabel("All Flights " + filteredBy);
        panel.add(flightDetailLabel, c);

        /**
         * Filter Search Button
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 4;
        filterButton = new JButton("Filter Search");
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // TODO: implement requeryAll if no checkboxes are checked and filter button is pressed
                filterSearch();
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
            ResultSet allDetailsSet = Main.myStat.executeQuery("select flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, date_arrive, time_arrive, available_seats from flights");

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
            refreshTable(columns);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterSearch() {
        formatSelectClause();
        String sql = select_clause + from_clause;
        //System.out.println(sql);
        select_clause = "select ";
        try {
            ResultSet filteredSet = Main.myStat.executeQuery(sql);

            String[] filterArray = Arrays.copyOf(filterColumns.toArray(), filterColumns.toArray().length, String[].class);

            int rowCount = 0;
            if (filteredSet.last()) {
                rowCount = filteredSet.getRow();
                filteredSet.beforeFirst();
            }

            data = new Object[rowCount][filterArray.length];
            int j = 0;

            while (filteredSet.next()) {
                for (int i = 0; i < filterArray.length; i++) {
                    data[j][i] = filteredSet.getObject(i + 1);
                }
                j++;
            }
            filterColumns.clear();
            deselectAllCheckBoxes();
            refreshTable(filterArray);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void formatSelectClause() {
        select_clause = select_clause.substring(0, select_clause.length() - 2);
        select_clause += " ";
    }

    private void deselectAllCheckBoxes() {
        arrivalAirportCB.setSelected(false);
        departingAirportCB.setSelected(false);
        arrivalDateCB.setSelected(false);
        arrivalTimeCB.setSelected(false);
        departingDateCB.setSelected(false);
        departingTimeCB.setSelected(false);
    }

    private void refreshTable(String[] columnNames) {
        if (scrollPane != null) {
            panel.remove(scrollPane);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 10;
        c.gridheight = 0;
        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }
}
