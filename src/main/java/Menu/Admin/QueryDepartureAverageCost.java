package main.java.Menu.Admin;

import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Colby on 2016-03-30.
 */
public class QueryDepartureAverageCost {
    private JPanel panel;
    private JLabel label = new JLabel();
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"Departing AirportID", "Average Cost"};
    private JButton backButton;
    private JButton maxButton;
    private JButton minButton;
    private JButton avgButton;
    private GridBagConstraints c;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        maxButton = new JButton("Most Expensive");
        maxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findMaxAverageDepartingCost();
            }
        });
        panel.add(maxButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        minButton = new JButton("Least Expensive");
        minButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findMinAverageDepartingCost();

            }
        });
        panel.add(minButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        avgButton = new JButton("Average Cost");
        avgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findAverageDepartingCost();
            }
        });
        panel.add(avgButton, c);


        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryPassengerDivision.panel.setVisible(true);
            }
        });
        panel.add(backButton);

        findAverageDepartingCost();
    }

    private void findMinAverageDepartingCost() {
        try{
            ResultSet mySet = Main.myStat.executeQuery(
                    "select airportid_depart, avg_cost from average_flight_cost as copy where avg_cost = (select Min(avg_cost) from average_flight_cost); ");
            int rowCount = 0;

            if(mySet.last()){
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for(int i=0; i<columns.length; i++) {
                    data[j][i] = mySet.getObject(i + 1);
                }
                j++;
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            label.setText("Mininum Average Flight Cost");
            panel.add(label, c);

            refreshTable();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void findMaxAverageDepartingCost() {
        try{
            ResultSet mySet = Main.myStat.executeQuery(
                    "select airportid_depart, avg_cost from average_flight_cost as copy where avg_cost = (select Max(avg_cost) from average_flight_cost);");
            int rowCount = 0;

            if(mySet.last()){
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for(int i=0; i<columns.length; i++) {
                    data[j][i] = mySet.getObject(i + 1);
                }
                j++;
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            label.setText("Maximum Average Flight Cost");
            panel.add(label, c);

            refreshTable();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void findAverageDepartingCost(){
        try{
            Main.myStat.executeUpdate("CREATE or replace VIEW average_flight_cost as select airportid_depart, AVG(cost) as avg_cost from flights group by airportid_depart;");
            ResultSet mySet = Main.myStat.executeQuery(
                    "select * from average_flight_cost ");
            int rowCount = 0;

            if(mySet.last()){
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for(int i=0; i<columns.length; i++) {
                    data[j][i] = mySet.getObject(i + 1);
                }
                j++;
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            label.setText("Average Cost of All Flights");
            panel.add(label, c);

            refreshTable();
        }catch(Exception e){
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
        c.gridwidth = 3;
        table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, c);
        panel.revalidate();
        panel.repaint();
    }
}
