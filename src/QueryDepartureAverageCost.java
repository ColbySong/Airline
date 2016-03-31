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
    private int count = 0;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        findAverageDepartingCost();

        maxButton = new JButton("Most Expensive");
        maxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findMaxAverageDepartingCost();
            }
        });
        panel.add(maxButton);

        minButton = new JButton("Least Expensive");
        minButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findMinAverageDepartingCost();

            }
        });
        panel.add(minButton);


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

            label.setText("Mininum Average Flight Cost");
            panel.add(label);

            refreshTable();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void findMaxAverageDepartingCost() {
        try{
            ResultSet mySet = Main.myStat.executeQuery(
                    "select airportid_depart, avg_cost from average_flight_cost as copy where avg_cost = (select Max(avg_cost) from average_flight_cost); ");
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

            label.setText("Maximum Average Flight Cost");
            panel.add(label);

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

            label.setText("Average flight cost based on departure location");
            panel.add(label);

            refreshTable();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        if (scrollPane != null) {
            panel.remove(scrollPane);
        }
        table = new JTable(data, columns);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        panel.revalidate();
        panel.repaint();
    }
}
