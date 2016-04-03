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

    public void init(){
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);


        prompt = new JLabel();
        prompt.setText("Please enter Passport Number");
        panel.add(prompt);

        final JTextField passenger_id = new JTextField();
        passenger_id.setPreferredSize(new Dimension(250,20));
        panel.add(passenger_id);


        JButton search = new JButton();
        search.setText("Search for Baggage Info");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passport_no_to_query = passenger_id.getText();
                searchBaggages();
                System.out.println(passport_no_to_query);
            }
        });
        panel.add(search);




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

    public void searchBaggages(){
        try{
            ResultSet mySet = Main.myStat.executeQuery(

                    "select * from baggages, passengers where passengers.passport_no = baggages.passport_no AND " +
                            "passengers.passport_no = " + "\'" + passport_no_to_query + "\'");

            int rowCount = 0;

            if (mySet.last()){
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
                firstName = mySet.getString("first_name");
                lastName = mySet.getString("last_name");

            }

            label.setText("Baggages Info of Passenger " + firstName + " " + lastName);
            panel.add(label);


            refreshTable();
        }catch(MySQLSyntaxErrorException e){
            JLabel error = new JLabel();
            error.setText("Please enter a valid Passenger ID");
            panel.add(error);
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
