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

    private Boolean isAAChecked = false;
    private Boolean isDAChecked = false;
    private Boolean isADChecked = false;
    private Boolean isATChecked = false;
    private Boolean isDDChecked = false;
    private Boolean isDTChecked = false;

    private Boolean isAASelected = false;
    private Boolean isDASelected = false;

    private JScrollPane scrollPane;

    private JLabel flightDetailLabel;
    private JLabel departureAirportLabel;
    private JLabel arrivalAirportLabel;
    private JLabel sortByLabel;

    private JButton backButton;
    private JButton filterButton;

    private JTable table;

    private String[] columns = new String[] {
            "Flight Number", "Cost", "Depart From", "Departure Date", "Departure Time",
            "Arrive In", "Arrival Date", "Arrival Time", "Seats Remaining"};
    private String[] sortByTypes = new String[] {
            "- select filter -", "Arrival Date", "Departure Date", "Arrival Time", "Departure Time", "Cost"};

    private Object[][] data;

    private List<String> filterColumns = new ArrayList<String>();

    private String select_clause = "select ";
    private String from_clause = "from flights ";
    private String where_clause = "";
//    private String order_by = "";

    private Boolean select_triggered = false;
    private Boolean where_triggered = false;
//    private Boolean orderby_triggered = false;

    public void init() {

        panel = new JPanel();
        c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        /**
         * Departing Airport Dropdown (where airportid_depart = ...)
         */
        // TODO: implement these where queries

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        departureAirportLabel = new JLabel("Select Departing Airport");
        panel.add(departureAirportLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        departureAirportIDComboBox = new JComboBox();
        departureAirportIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selected = departureAirportIDComboBox.getSelectedItem();
                formatWhereClause(selected, "airportid_depart");
            }
        });
        generateDepartureDropDown();
        panel.add(departureAirportIDComboBox, c);

        /**
         * Arriving Airport Dropdown (where airportid_arrive = ...)
         */
        // TODO: implement these where queries

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        arrivalAirportLabel = new JLabel("Select Arriving Airport");
        panel.add(arrivalAirportLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        arrivalAirportIDComboBox = new JComboBox();
        arrivalAirportIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selected = arrivalAirportIDComboBox.getSelectedItem();
                formatWhereClause(selected, "airportid_arrive");
            }
        });
        generateArrivalDropDown();
        panel.add(arrivalAirportIDComboBox, c);

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
                    isAAChecked = true;
                    filterColumns.add("Arriving In");
                    select_clause += "airportid_arrive, ";
                } else {
                    isAAChecked = false;
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
                    isDAChecked = true;
                    filterColumns.add("Depart From");
                    select_clause += "airportid_depart, ";
                } else {
                    isDAChecked = false;
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
                    isADChecked = true;
                    filterColumns.add("Arrival Date");
                    select_clause += "date_arrive, ";
                } else {
                    isADChecked = false;
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
                    isATChecked = true;
                    filterColumns.add("Arrival Time");
                    select_clause += "time_arrive, ";
                } else {
                    isATChecked = false;
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
                    isDDChecked = true;
                    filterColumns.add("Departure Date");
                    select_clause += "date_depart, ";
                } else {
                    isDDChecked = false;
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
                    isDTChecked = true;
                    filterColumns.add("Departure Time");
                    select_clause += "time_depart, ";
                } else {
                    isDTChecked = false;
                    filterColumns.remove("Departure Time");
                    select_clause = select_clause.replace("time_depart, ", "");
                }
            }
        });
        panel.add(departingTimeCB, c);

        /**
         * Sort by filter (group by ...)
         */
        // TODO: Possibly remove?

//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 4;
//        c.gridy = 3;
//        sortByLabel = new JLabel("Sort By: ");
//        panel.add(sortByLabel, c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 5;
//        c.gridy = 3;
//        sortByComboBox = new JComboBox();
//        sortByComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Object selected = sortByComboBox.getSelectedItem();
//                formatOrderBy(selected);
//            }
//        });
//        generateSortByDropDown();
//        panel.add(sortByComboBox, c);

        /**
         * Flight Table Title with filter suffix
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        flightDetailLabel = new JLabel("All Flights");
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
                setSelectTrigger();
                if (!select_triggered && !where_triggered) {
                    displayAllFlightDetails();
                } else {
                    filterSearch();
                }
            }
        });
        panel.add(filterButton, c);
        displayAllFlightDetails();
    }

    private void generateDepartureDropDown() {
        try {
            ResultSet departureSet = Main.myStat.executeQuery("select airportid_depart from flights");

            List<String> results = new ArrayList<>();
            results.add("- select airport -");

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
            results.add("- select airport -");

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

//    private void generateSortByDropDown() {
//        for (int i = 0; i < sortByTypes.length; i++) {
//            sortByComboBox.addItem(sortByTypes[i]);
//        }
//    }

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
        String[] filterArray;
        formatSelectClause();
        String sql = select_clause + from_clause + where_clause;
        System.out.println(sql);
        select_clause = "select ";
//        order_by = "";
        try {
            ResultSet filteredSet = Main.myStat.executeQuery(sql);

            if (select_triggered) {
                filterArray = Arrays.copyOf(filterColumns.toArray(), filterColumns.toArray().length, String[].class);
            }
            else {
                filterArray = columns;
            }


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
            setAllCheckedToFalse();
            resetWhereConditions();
            refreshTable(filterArray);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void formatSelectClause() {
        if (!select_triggered) {
            select_clause += "flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, date_arrive, time_arrive, available_seats ";
        }
        else {
            select_clause = select_clause.substring(0, select_clause.length() - 2);
            select_clause += " ";
        }
    }

    private void formatWhereClause(Object selected, String attr) {
        String selectedAirport = selected.toString();
        if (selectedAirport.equals("- select airport -")) {
            System.out.println(where_clause);
            where_triggered = false;
            where_clause = "";
            System.out.println(where_clause);
        }
        else {
            where_triggered = true;
            where_clause = "where ";
            where_clause += buildWhereClause(selectedAirport, attr);
            System.out.println(where_clause);
        }
    }

    private String buildWhereClause(String selectedAirport, String attr) {
        return attr + " = " + "\'" + selectedAirport + "\'";
    }

//    private void formatOrderBy(Object selected) {
//        String[] sortByAttr = new String[] {"", "date_arrive ", "date_depart ", "time_arrive ", "time_depart ", "cost "};
//        String sortBy = selected.toString();
//        int matchingStringIndex = 0;
//
//        if (sortBy.equals(sortByTypes[0])) {
//            orderby_triggered = false;
//        }
//        else {
//            orderby_triggered = true;
//            for (int i = 1; i < sortByTypes.length; i++) {
//                if (sortBy.equals(sortByTypes[i])) {
//                    matchingStringIndex = i;
//                    break;
//                }
//            }
//        }
//        if (orderby_triggered) {
//            order_by += "order by ";
//            order_by += sortByAttr[matchingStringIndex];
//            order_by += "asc";
//        }
//    }

    private void setSelectTrigger() {
        if (isAAChecked || isDAChecked || isADChecked || isATChecked || isDDChecked || isDTChecked) {
            select_triggered = true;
        }
        else {
            select_triggered = false;
        }
    }

    private void deselectAllCheckBoxes() {
        arrivalAirportCB.setSelected(false);
        departingAirportCB.setSelected(false);
        arrivalDateCB.setSelected(false);
        arrivalTimeCB.setSelected(false);
        departingDateCB.setSelected(false);
        departingTimeCB.setSelected(false);
    }

    private void setAllCheckedToFalse() {
        isAAChecked = false;
        isADChecked = false;
        isATChecked = false;
        isDAChecked = false;
        isDDChecked = false;
        isDTChecked = false;
    }

    private void resetWhereConditions() {
        arrivalAirportIDComboBox.setSelectedIndex(0);
        departureAirportIDComboBox.setSelectedIndex(0);
        where_triggered = false;
        where_clause = "";
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
