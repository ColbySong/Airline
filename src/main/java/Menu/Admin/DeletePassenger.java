package main.java.Menu.Admin;

import main.java.Menu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by yoonyok on 2016-03-24.
 */
public class DeletePassenger {
    private JPanel panel;
    private JLabel errorMsg = new JLabel();
    private String passenger_id_to_query;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"First Name", "Last Name", "Passport No"};
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
        prompt.setText("Please enter Passport Number to delete");
        panel.add(prompt, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        final JTextField passenger_id = new JTextField();
        passenger_id.setPreferredSize(new Dimension(250, 20));
        panel.add(passenger_id, c);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        JButton search = new JButton();
        search.setText("Delete");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passenger_id_to_query = passenger_id.getText();
                try {
                    deletePassenger();
                } catch (Exception e1) {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 0;
                    c.gridy = 2;
                    errorMsg.setText("Passenger not found");
                    panel.add(errorMsg, c);
                    e1.printStackTrace();

                }
            }
        });
        panel.add(search, c);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                AdminPanel.panel.setVisible(true);
            }
        });
        panel.add(backButton);

        viewAllPassenger();
    }

    private void viewAllPassenger() {
        try {
            ResultSet mySet = Main.myStat.executeQuery("select first_name, last_name, passport_no  " +
                    "from passengers");

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
            c.gridx = 0;
            c.gridy = 2;
            errorMsg.setText("Passenger Not Found");
            panel.add(errorMsg, c);
            e.printStackTrace();
        }
    }

    public void deletePassenger() throws Exception {

        Main.myStat.executeUpdate(
                "Delete from passengers where passport_no = \"" + passenger_id_to_query + "\"");

        ResultSet mySet = Main.myStat.executeQuery("select first_name, last_name, passport_no  " +
                "from passengers");

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
