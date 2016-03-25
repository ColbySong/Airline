import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


/**
 * Created by BenGee on 2016-03-24.
 */
public class FlightReserver {

    private GridBagConstraints c;

    private JPanel panel;

    private static int MINIMUM_SEATNO = 1;
    private static int MAXIMUM_CONFNO = Integer.MAX_VALUE;

    private int seatCapacity = 0;

    private int seatNumber = 0;
    private int confirmationNo;
    private String reservedFlightNo;
    private String passportNo = "N239942";
    private String insertClause = "insert into reserves values";

    public void init(String reservedFlightNo) {
        this.reservedFlightNo = reservedFlightNo;

        panel = new JPanel();
        c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        Main.frame.add(panel);

        bookFlight();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        JLabel thankYouLabel = new JLabel("Thank you for reserving a flight!");
        panel.add(thankYouLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JButton showFlights = new JButton("Show My Flights");
        showFlights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Passenger.MyFlights mf = new Passenger.MyFlights();
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
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton, c);
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
            ResultSet results = Main.myStat.executeQuery("select seat_no from reserves");

            List<Integer> bookedSeats = new ArrayList<>();
            int i = 1;

            while (results.isBeforeFirst() && results.next()) {
                bookedSeats.add(results.getInt(i));
                i++;
            }

            while (seatNumber == 0) {
                Random rand = new Random();
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
            ResultSet results = Main.myStat.executeQuery("select confirmation_no from reserves");

            List<Integer> confirmedNums = new ArrayList<>();
            int i = 1;

            while (results.isBeforeFirst() && results.next()) {
                confirmedNums.add(results.getInt(i));
                i++;
            }

            while (confirmationNo == 0) {
                Random rand = new Random();
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
