package main.java.Menu.User.Flight;

import main.java.Menu.*;
import main.java.Menu.User.Passenger.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by BenGee on 2016-03-25.
 */
public class MyFlights {


    private static final String[] columns = new String[]{
            "Flight Number", "Cost", "Depart From", "Departure Date", "Departure Time",
            "Arrive In", "Arrival Date", "Arrival Time", "Seat Number", "Confirmation Number"};
    private Object[][] data;

    private JPanel panel;
    private JScrollPane scrollPane;
    private GridBagConstraints c;


    public void init(String passportNo) {
        panel = new JPanel();
        c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        JLabel loggedInLabel = new JLabel("You are logged in as " + Passenger.firstName + " " + Passenger.lastName + "!");
        panel.add(loggedInLabel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Passenger.panel.setVisible(true);
            }
        });
        panel.add(backButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(logoutButton,c);

        searchFlights(passportNo);
    }

    private void searchFlights(Object selectedItem) {
        try {
            ResultSet mySet = Main.myStat.executeQuery("select flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, date_arrive, time_arrive, seat_no, confirmation_no from reserves natural join flights where passport_no = \"" + selectedItem + "\"");

            int rowCount = 0;
            if (mySet.last()) {
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while (mySet.next()) {
                for (int i = 0; i < columns.length; i++) {
                    data[j][i] = mySet.getObject(i + 1);
                }
                j++;
            }

            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        if (scrollPane != null) {
            panel.remove(scrollPane);
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 10;
        c.gridheight = 2;

        JTable table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }
}

