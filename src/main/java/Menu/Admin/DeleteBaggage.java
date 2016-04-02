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
public class DeleteBaggage {
    private JPanel panel;
    private JLabel label = new JLabel();
    private JLabel label2 = new JLabel();
    private JLabel prompt;
    private String baggage_id_to_query;
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"First Name", "Last Name", "Passenger ID", "Baggage ID","Weight", "Type"};
    private JButton backButton;


    public void init(){
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);


        prompt = new JLabel();
        prompt.setText("Please enter Baggage ID to delete");
        panel.add(prompt);

        final JTextField baggage_id = new JTextField();
        baggage_id.setPreferredSize(new Dimension(250,20));
        panel.add(baggage_id);


        JButton search = new JButton();
        search.setText("Delete");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baggage_id_to_query = baggage_id.getText();
                try {
                    deleteBaggage();
                }catch(Exception e1){
                    label.setText("Baggage Not Found");
                    panel.add(label);
                    e1.printStackTrace();

                }
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

        viewAllBaggages();

    }

    private void viewAllBaggages() {
        try{
            ResultSet mySet = Main.myStat.executeQuery("select first_name, last_name, passenger_id, baggage_id, weight, type " +
                    "from baggages  , passengers where passengers.passport_no = baggages.passport_no");

            int rowCount = 0;

            if(mySet.last()){
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for(int i=0; i<columns.length; i++) {
                    data[j][i] = mySet.getObject(i+1);
                }
                j++;


            }
            refreshTable();
        }catch(Exception e){
            label.setText("Baggage Not Found");
            panel.add(label);
            e.printStackTrace();
        }
    }

    public void deleteBaggage() throws Exception{

            Main.myStat.executeUpdate(
                    "Delete from baggages where baggage_id = " + baggage_id_to_query
            );
            ResultSet mySet = Main.myStat.executeQuery("select first_name, last_name, passenger_id, baggage_id, weight, type " +
                    "from baggages, passengers where passengers.passport_no = baggages.passport_no");
            int rowCount = 0;

            if(mySet.last()){
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for(int i=0; i<columns.length; i++) {
                    data[j][i] = mySet.getObject(i+1);
                }
                j++;

            }

            refreshTable();


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
