package main.java.Menu.Admin;

import main.java.Menu.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by BenGee on 2016-03-20.
 */
public class QueryPassengers {

    private JPanel panel;
    private JComboBox passportNoComboBox;
    private JButton getPassengerButton;
    private JButton backButton;
    private JLabel label = new JLabel();

    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        passportNoComboBox = new JComboBox();
        try{
            ResultSet mySet = Main.myStat.executeQuery("select * from passengers");

            while(mySet.next()){
                passportNoComboBox.addItem(mySet.getString("passport_no"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        panel.add(passportNoComboBox);

        getPassengerButton = new JButton("Get Passenger");
        getPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query(passportNoComboBox.getSelectedItem());
            }
        });
        panel.add(getPassengerButton);

        panel.add(label);
        panel.setLayout(new FlowLayout());

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

    private void query(Object selectedItem) {
        try{
            ResultSet mySet = Main.myStat.executeQuery("select * from passengers where passport_no = '" + selectedItem + "'");
            while(mySet.next()){
                label.setText(mySet.getString("first_name") + " " + mySet.getString("last_name"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
