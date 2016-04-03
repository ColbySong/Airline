package main.java.Menu.User.Flight;

import main.java.Menu.Main;
import main.java.Menu.User.Passenger.Passenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by BenGee on 2016-03-24.
 */
public class FlightReserver {

    private JPanel panel;
    private JLabel reservationAlert = new JLabel();

    private int seatCapacity = 0;

    private int seatNumber = 0;
    private int confirmationNo;
    private String reservedFlightNo;
    private String passportNo = Passenger.passportNo;

    public void init(String reservedFlightNo) {
        this.reservedFlightNo = reservedFlightNo;

        panel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        if (isReserveValid()) {
            bookFlight();
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        panel.add(reservationAlert, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JButton showFlights = new JButton("Show My Flights");
        showFlights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFlights mf = new MyFlights();
                mf.init(passportNo);
                panel.setVisible(false);
            }
        });
        panel.add(showFlights, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryFlights.panel.setVisible(true);
            }
        });
        panel.add(backButton, c);
    }

    private boolean isReserveValid() {
        try {
            ResultSet result = Main.myStat.executeQuery("select passport_no, flight_no from reserves where passport_no = "
                    + "\'" + passportNo + "\'" + " and " + "flight_no = " + "\'" + reservedFlightNo + "\'");

            if (result.isBeforeFirst() && result.next()
                    && result.getString("passport_no").equals(passportNo) && result.getString("flight_no").equals(reservedFlightNo)) {
                reservationAlert.setText("You have already booked this flight");
                return false;
            } else {
                reservationAlert.setText("Thank you for reserving a flight!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void bookFlight() {
        getAvailableSeats();
        generateValidSeatNo();
        generateValidConfirmationNo();
        String insert = generateInsertClause();
        System.out.println(insert);

        try {
            Main.myStat.execute(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        seatCapacity--;
        updateAvailableSeats();
    }

    private void updateAvailableSeats() {
        String updateClause = generateUpdateClause();
        System.out.println(updateClause);

        try {
            Main.myStat.execute(updateClause);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateUpdateClause() {
        return "update flights set available_seats = " + seatCapacity-- + " where flight_no = " + "\'" + reservedFlightNo + "\'";
    }

    private void generateValidSeatNo() {
        try {
            ResultSet results = Main.myStat.executeQuery("select seat_no from reserves where flight_no =" + "\'" + reservedFlightNo + "\'");

            List<Integer> bookedSeats = new ArrayList<Integer>();
            int i = 1;

            while (results.isBeforeFirst() && results.next()) {
                bookedSeats.add(results.getInt(i));
                i++;
            }

            while (seatNumber == 0) {
                Random rand = new Random();
                int MINIMUM_SEATNO = 1;
                int tempSeatNum = rand.nextInt((seatCapacity - MINIMUM_SEATNO) + 1) + MINIMUM_SEATNO;

                if (!bookedSeats.contains(tempSeatNum)) {
                    seatNumber = tempSeatNum;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateValidConfirmationNo() {
        try {
            ResultSet results = Main.myStat.executeQuery("select confirmation_no from reserves where flight_no =" + "\'" + reservedFlightNo + "\'");

            List<Integer> confirmedNums = new ArrayList<Integer>();
            int i = 1;

            while (results.isBeforeFirst() && results.next()) {
                confirmedNums.add(results.getInt(i));
                i++;
            }

            while (confirmationNo == 0) {
                Random rand = new Random();
                int MAXIMUM_CONFNO = Integer.MAX_VALUE;
                int tempConfNum = rand.nextInt(MAXIMUM_CONFNO);

                if (!confirmedNums.contains(tempConfNum)) {
                    confirmationNo = tempConfNum;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateInsertClause() {
        String insertClause = "insert into reserves values";
        return insertClause + "(" + "\'" + passportNo + "\'" + ", " + "\'" + reservedFlightNo + "\'" + ", "
                + seatNumber + ", " + confirmationNo + ")";
    }

    private void getAvailableSeats() {
        try {
            ResultSet results = Main.myStat.executeQuery("select available_seats from flights where flight_no = "
                    + "\'" + reservedFlightNo + "\'");

            results.beforeFirst();
            results.next();
            seatCapacity = results.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
