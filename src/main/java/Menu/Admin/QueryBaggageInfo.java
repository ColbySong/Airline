package main.java.Menu.Admin;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by yoonyok on 2016-03-22.
 */
public class QueryBaggageInfo {
    private JPanel panel;
    private JLabel label = new JLabel();
    private JLabel prompt;
    private String passport_no_to_query;
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"Baggage ID", "Weight", "Type"};
    private JButton backButton;
    private String firstName;
    private String lastName;
    private GridBagConstraints c;

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
        final JTextField passenger_id = new JTextField(15);
        panel.add(passenger_id, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        JButton search = new JButton();
        search.setText("Display Baggages");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passport_no_to_query = passenger_id.getText();
                searchBaggages();
                System.out.println(passport_no_to_query);
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

    public void searchBaggages() {
        try {
            ResultSet mySet = Main.myStat.executeQuery(

                    "select * from baggages, passengers where passengers.passport_no = baggages.passport_no AND " +
                            "passengers.passport_no = " + "\'" + passport_no_to_query + "\'");

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
            label.setText("Baggages of " + firstName + " " + lastName);
            panel.add(label, c);

            refreshTable();

        } catch (MySQLSyntaxErrorException e) {
            JLabel error = new JLabel();
            error.setText("Please enter a valid Passenger ID");
            panel.add(error);
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
