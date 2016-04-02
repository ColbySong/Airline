
package main.java.Menu.Admin;

import main.java.Menu.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by Daniel on 2016-03-24.
 */
public class CreateFlight {

    public static JPanel panel;

    private JTextField flight_no;
    private JTextField available_seats;
    private JTextField cost;
    private JTextField airplane_id;
    private JTextField airportid_depart;
    private JTextField airportid_arrive;
    private JTextField date_depart;
    private JTextField date_arrive;
    private JTextField time_depart;
    private JTextField time_arrive;
    private JLabel successMsg = new JLabel();
    private JLabel typeErrorMsg = new JLabel();
    private JLabel flight_noErrorLabel = new JLabel();
    private GridBagConstraints c;


    public void init() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);

        successMsg.setVisible(false);
        typeErrorMsg.setVisible(false);
        flight_noErrorLabel.setVisible(false);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        flight_no = new JTextField("Flight No",20);
        flight_no.setForeground(Color.GRAY);
        flight_no.setSize(100, 10);
        panel.add(flight_no,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;

        available_seats = new JTextField("Available Seats",20);
        available_seats.setForeground(Color.GRAY);
        available_seats.setSize(100, 10);
        panel.add(available_seats,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;

        cost = new JTextField("Cost",20);
        cost.setForeground(Color.GRAY);
        cost.setSize(100, 10);
        panel.add(cost, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;

        airplane_id = new JTextField("Airplane ID",20);
        airplane_id.setForeground(Color.GRAY);
        airplane_id.setSize(100, 10);
        panel.add(airplane_id, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;

        airportid_depart = new JTextField("Airport ID - Depart",20);
        airportid_depart.setForeground(Color.GRAY);
        airportid_depart.setSize(100, 10);
        panel.add(airportid_depart, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;

        airportid_arrive = new JTextField("Airport ID - Arrive",20);
        airportid_arrive.setForeground(Color.GRAY);
        airportid_arrive.setSize(100, 10);
        panel.add(airportid_arrive, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;

        date_depart = new JTextField("Departure Date",20);
        date_depart.setForeground(Color.GRAY);
        date_depart.setSize(100, 10);
        panel.add(date_depart, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;

        time_depart = new JTextField("Departure Time",20);
        time_depart.setForeground(Color.GRAY);
        time_depart.setSize(100, 10);
        panel.add(time_depart, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;

        date_arrive = new JTextField("Arrival Date",20);
        date_arrive.setForeground(Color.GRAY);
        date_arrive.setSize(100, 10);
        panel.add(date_arrive, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;


        time_arrive = new JTextField("Arrival Time",20);
        time_arrive.setForeground(Color.GRAY);
        time_arrive.setSize(100, 10);
        panel.add(time_arrive,c);

        successMsg.setText("Flight Created Successfully");
        typeErrorMsg.setText("Please Enter a Valid Field");
        flight_noErrorLabel.setText("Please Enter a Valid Flight Number");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 10;

        JButton createFlight = new JButton("Create Flight");
        createFlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValid()) {
                    successMsg.setVisible(false);
                    typeErrorMsg.setVisible(false);
                    createFlight();
                }
            }
        });
        panel.add(createFlight, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 11;


        JButton backButton = new JButton("Cancel");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                AdminPanel.panel.setVisible(true);
            }
        });
        panel.add(backButton,c);

        c.gridx = 0;
        c.gridy = -1;
        successMsg.setForeground(Color.GREEN);
        panel.add(successMsg, c);

        c.gridx = 0;
        c.gridy = -1;
        typeErrorMsg.setForeground(Color.RED);
        panel.add(typeErrorMsg, c);

        c.gridx = 0;
        c.gridy = -1;
        flight_noErrorLabel.setForeground(Color.RED);
        panel.add(flight_noErrorLabel, c);
    }

    private void createFlight() {
        String flightno = flight_no.getText();
        String availSeat = available_seats.getText();
        String cost = available_seats.getText();
        String airplaneID = airplane_id.getText();
        String aIDArrive = airportid_arrive.getText();
        String aIDDepart = airportid_depart.getText();
        String dateArrive = date_arrive.getText();
        String dateDepart = date_depart.getText();
        String timeArrive = time_arrive.getText();
        String timeDepart = time_depart.getText();
        try {
            Main.myStat.executeUpdate("insert into flights values(\"" + flightno + "\", "+ availSeat +"," + cost +", \""+airplaneID+"\", \""+aIDDepart+"\", \"" + aIDArrive +"\", \"" + dateDepart + "\", \"" + timeDepart + "\", \"" + dateArrive + "\", \"" + timeArrive +"\")");
            successMsg.setVisible(true);
        }catch (SQLException e) {
            successMsg.setVisible(false);
            typeErrorMsg.setVisible(true);
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        flight_noErrorLabel.setVisible(false);
        if (!flight_no.getText().matches("\\d{3}[A-Z]{3}")) {

            flight_noErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }


}
