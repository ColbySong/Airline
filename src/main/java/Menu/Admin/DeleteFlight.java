package main.java.Menu.Admin;
import main.java.Menu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yoonyok on 2016-03-24.
 */
public class DeleteFlight {
    private JPanel panel;
    private JLabel errorMsg = new JLabel();
    private JLabel flightNotFoundMsg;
    private String flight_id_to_query;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"Flight No"};
    private GridBagConstraints c;


    public void init() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JLabel prompt = new JLabel();
        prompt.setText("Please enter Flight ID to delete");
        panel.add(prompt, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        final JTextField flight_id = new JTextField();
        flight_id.setPreferredSize(new Dimension(250, 20));
        panel.add(flight_id, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        JButton search = new JButton();
        search.setText("Delete");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flight_id_to_query = flight_id.getText();
                errorMsg.setVisible(false);
                try {
                    deleteFlight();
                } catch (Exception e1) {
                    flightNotFoundMsg.setText("");
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 1;
                    c.gridy = 2;
                    panel.add(errorMsg, c);
                    errorMsg.setText("Flight Cannot Be Deleted - Has Reservations");
                    errorMsg.setForeground(Color.RED);
                    errorMsg.setVisible(true);
                }
            }
        });
        panel.add(search, c);

        flightNotFoundMsg = new JLabel("");
        flightNotFoundMsg.setForeground(Color.RED);
        panel.add(flightNotFoundMsg);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                AdminPanel.panel.setVisible(true);
            }
        });
        panel.add(backButton);

        viewAllFlight();

    }

    private void viewAllFlight() {
        try {
            ResultSet mySet = Main.myStat.executeQuery("select flight_no from flights");

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
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 2;
            errorMsg.setText("Flight Not Found");
            panel.add(errorMsg, c);
            e.printStackTrace();
        }
    }

    public void deleteFlight() throws SQLException {
        ResultSet rs = Main.myStat.executeQuery("Select * from flights where flight_no = '" + flight_id_to_query + "'");

        if (!rs.isBeforeFirst()) {
            flightNotFoundMsg.setText("Flight not found");
            return;
        }

        Main.myStat.executeUpdate(
                "Delete from flights where flight_no = '" + flight_id_to_query + "'"
        );

        flightNotFoundMsg.setText("");

        ResultSet mySet = Main.myStat.executeQuery("select flight_no from flights");

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
    }

    private void refreshTable() {
        if (scrollPane != null) {
            panel.remove(scrollPane);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        JTable table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }
}
