package main.java.Menu.Admin;

import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Colby on 2016-03-24.
 */
public class AdminPanel {

    public static JPanel panel;
    private JLabel passengerFlightDetail = new JLabel("Passenger Flight Details");

    private JTable PFTable;
    private JTable AFTable;

    private Object[][] data;
    private JScrollPane scrollPanePF;
    private JScrollPane scrollPaneAF;
    private String[] passengerFlight = new String[] {"Passport Number", "Passenger ID", "First Name", "Last Name",
    "Flight Number", "Confirmation Number"};
    private String[] airplaneFlight = new String[] {"Airplane ID", "Manufacturer", "Model", "Capacity", "Flight Number"};
    private JButton backButton;
    private GridBagConstraints c;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);


        /**
         * Search for Passenger Flights
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        JButton searchFlightsReservesPassenger = new JButton(("Additional Flight Information"));
        panel.add(searchFlightsReservesPassenger, c);
        searchFlightsReservesPassenger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryReservation qj2 = new QueryReservation();
                qj2.init();
            }
        });

        /**
         * Search for Passenger Baggage
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        JButton searchBaggagesForPassengerButton = new JButton(("Search for Baggage"));
        panel.add(searchBaggagesForPassengerButton, c);
        searchBaggagesForPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryBaggageInfo qj = new QueryBaggageInfo();
                qj.init();
            }
        });

        /**
         * Statistics Button
         */

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        JButton searchPassengerDivisionButton = new JButton("Flight Statistics");
        panel.add(searchPassengerDivisionButton, c);
        searchPassengerDivisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryPassengerDivision qd = new QueryPassengerDivision();
                qd.init();
            }
        });

        /**
         * Logout Button
         */
        c.gridx = 4;
        c.gridy = 0;
        backButton = new JButton("Logout");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton, c);

        /**
         * Delete Passenger
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JButton deletePassenger = new JButton(("Delete a Passenger"));
        panel.add(deletePassenger, c);
        deletePassenger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                DeletePassenger dp = new DeletePassenger();
                dp.init();
            }
        });

        /**
         * Delete Baggage
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        JButton deleteBaggage = new JButton(("Delete a Passenger Baggage"));
        panel.add(deleteBaggage, c);
        deleteBaggage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                DeleteBaggage db = new DeleteBaggage();
                db.init();
            }
        });

        /**
         * Delete Flight
         */
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        JButton deleteFlight = new JButton(("Delete a Flight"));
        panel.add(deleteFlight, c);
        deleteFlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                DeleteFlight dbb = new DeleteFlight();
                dbb.init();
            }
        });

        /**
         * CreateFlight Button
         */

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        JButton createFlight = new JButton("Create New Flight");
        panel.add(createFlight, c);
        createFlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                CreateFlight qf = new CreateFlight();
                qf.init();
            }
        });

        /**
         * Passenger Flight Title and Table
         */
        c.gridx = 0;
        c.gridy = 2;
        panel.add(passengerFlightDetail, c);
        displayAllPassengerFlightInto();

        /**
         * Airplane Detail Title and Table
         */
        c.gridx = 0;
        c.gridy = PFTable.getRowHeight();
        displayAirplaneDetails();

    }

    private void displayAirplaneDetails() {
        try {
            ResultSet results = Main.myStat.executeQuery("select a.airplane_id, a.manufacturer, a.model, ac.capacity, " +
                    "f.flight_no from airplanes a, airplane_capacities ac, flights f where a.airplane_id = f.airplane_id and " +
                    "a.model = ac.model");

            int rowCount = 0;
            if (results.last()) {
                rowCount = results.getRow();
                results.beforeFirst();
            }

            data = new Object[rowCount][airplaneFlight.length];
            int j = 0;

            while (results.next()) {
                for (int i = 0; i < airplaneFlight.length; i++) {
                    data[j][i] = results.getObject(i + 1);
                }
                j++;
            }
            makeAirplaneFlightTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayAllPassengerFlightInto() {
        try {
            ResultSet results = Main.myStat.executeQuery("select p.passport_no, p.passenger_id, p.first_name, p.last_name, r.flight_no, " +
                    "r.confirmation_no from passengers p, reserves r where p.passport_no = r.passport_no");

            int rowCount = 0;
            if (results.last()) {
                rowCount = results.getRow();
                results.beforeFirst();
            }

            data = new Object[rowCount][passengerFlight.length];
            int j = 0;

            while (results.next()) {
                for (int i = 0; i < passengerFlight.length; i++) {
                    data[j][i] = results.getObject(i + 1);
                }
                j++;
            }

            makePassengerFlightTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makePassengerFlightTable() {
        if (scrollPanePF != null) {
            panel.remove(scrollPanePF);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 5;
        c.gridheight = 5;
        c.gridy = 3;
        PFTable = new JTable(data, passengerFlight);
        scrollPanePF = new JScrollPane(PFTable);
        PFTable.setPreferredScrollableViewportSize(PFTable.getPreferredSize());
        panel.add(scrollPanePF, c);
        panel.revalidate();
        panel.repaint();
    }

    private void makeAirplaneFlightTable() {
        if (scrollPaneAF != null) {
            panel.remove(scrollPaneAF);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 5;
        c.gridheight = 5;
        c.gridy = PFTable.getRowHeight() + 2;
        AFTable = new JTable(data, airplaneFlight);
        scrollPaneAF = new JScrollPane(AFTable);
        AFTable.setPreferredScrollableViewportSize(AFTable.getPreferredSize());
        panel.add(scrollPaneAF, c);
        panel.revalidate();
        panel.repaint();
    }

}
