package main.java.Menu.Admin;

import main.java.Menu.Main;

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
    private JLabel baggageNotFoundMsg;
    private String baggage_id_to_query;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"First Name", "Last Name", "Passenger ID", "Baggage ID","Weight", "Type"};
    private GridBagConstraints c;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        Main.frame.add(panel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JLabel prompt = new JLabel();
        prompt.setText("Please enter Baggage ID to delete");
        panel.add(prompt, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        final JTextField baggage_id = new JTextField(15);
        panel.add(baggage_id, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        JButton search = new JButton();
        search.setText("Delete");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baggage_id_to_query = baggage_id.getText();
                try {
                    deleteBaggage();
                }catch(Exception e1){
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 1;
                    c.gridy = 2;
                    label.setText("Baggage Not Found");
                    panel.add(label, c);
                    e1.printStackTrace();

                }
            }
        });
        panel.add(search, c);

        baggageNotFoundMsg = new JLabel("");
        baggageNotFoundMsg.setForeground(Color.RED);
        panel.add(baggageNotFoundMsg);

        JButton backButton = new JButton("Back");
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
        }catch (Exception e){
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 2;
            label.setText("Baggage Not Found");
            panel.add(label, c);
            e.printStackTrace();
        }
    }

    public void deleteBaggage() throws Exception{
        ResultSet rs = Main.myStat.executeQuery("Select * from baggages where baggage_id = '" + baggage_id_to_query + "'");

        if (!rs.isBeforeFirst()) {
            baggageNotFoundMsg.setText("Baggage not found");
            return;
        }

        Main.myStat.executeUpdate(
                "Delete from baggages where baggage_id = " + baggage_id_to_query
        );

        baggageNotFoundMsg.setText("");

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
