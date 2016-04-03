package main.java.Menu.Admin;

import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Colby on 2016-03-31.
 */
public class QueryFlightDivision {
    private JPanel panel;
    private JLabel label = new JLabel();
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"Flight Number", "Number of Passengers"};
    private JButton backButton;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        findFlightWithAllPassengers();
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                QueryPassengerDivision.panel.setVisible(true);
            }
        });
        panel.add(backButton);

    }

    public void findFlightWithAllPassengers(){
        try{
            ResultSet mySet = Main.myStat.executeQuery(
                    "SELECT flights.flight_no, count(passport_no) as 'number_of_passengers' " +
                            "FROM flights left join reserves on flights.flight_no = reserves.flight_no " +
                            "group by flights.flight_no " +
                            "having count(*) = (select count(*) from passengers);");
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

            label.setText("Flights booked by all passengers");
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
