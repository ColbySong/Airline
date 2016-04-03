package main.java.Menu.Admin;

import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by yoonyok on 2016-03-22.
 */
public class QueryReservation {
    private JPanel panel;
    private JLabel label = new JLabel();
    private JLabel prompt;
    private String passport_no_to_query;
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"Flight No", "Confirmation No", "Date Depart", "Time Depart", "Airport Depart"};
    private JButton backButton;
    private String firstName;
    private String lastName;
    private GridBagConstraints c;
    private JTextField passport_no;

    public void init() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        prompt = new JLabel();
        prompt.setText("Please enter Passport Number");
        panel.add(prompt, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        passport_no = new JTextField(15);
        panel.add(passport_no, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        JButton search = new JButton();
        search.setText("Display Reserved Flights");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passport_no_to_query = passport_no.getText();
                searchReservedFlights();
            }
        });
        panel.add(search, c);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                AdminPanel.panel.setVisible(true);
            }
        });
        panel.add(backButton);

    }

    public void searchReservedFlights() {
        try {
            ResultSet mySet = Main.myStat.executeQuery(
                    "select flights.flight_no, confirmation_no, date_depart, " +
                            "time_depart, airportid_depart, first_name, last_name from reserves, " +
                            "flights, passengers where passengers.passport_no = reserves.passport_no AND " +
                            "passengers.passport_no =" + "\'" + passport_no_to_query + "\'"
                            + " AND reserves.flight_no = flights.flight_no");
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
                firstName = mySet.getString("first_name");
                lastName = mySet.getString("last_name");

            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            label.setText("Reserved Flight for " + firstName + " " + lastName);
            panel.add(label, c);


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
        c.gridy = 3;
        c.gridwidth = 2;
        table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }

}
