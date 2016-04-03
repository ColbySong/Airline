package main.java.Menu.Admin;

import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by BenGee on 2016-04-02.
 */
public class QueryReservesDivision {

    private JPanel panel;
    private JScrollPane scrollPane;
    private Object[][] data;
    private String[] columns = new String[]{"passport", "number of flights"};
    private GridBagConstraints c;

    public void init() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);

        displayPassengersWhoBookedAll();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryPassengerDivision.panel.setVisible(true);
            }
        });
        panel.add(backButton);
    }

    private void displayPassengersWhoBookedAll() {
        try {
            ResultSet mySet = Main.myStat.executeQuery(
                    "SELECT passengers.passport_no, count(flight_no) as 'number_of_flights' " +
                            "FROM passengers left join reserves on passengers.passport_no = reserves.passport_no " +
                            "group by passengers.passport_no " +
                            "having count(*) = (select count(*) from flights);");
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

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            JLabel label = new JLabel();
            label.setText("Passenger who booked all flights");
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
        c.gridy = 1;
        c.gridwidth = 2;
        JTable table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }
}
