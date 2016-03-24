import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Colby on 2016-03-23.
 */

// division query expressing
public class QueryDivision {
    private JPanel panel;
    private JLabel label = new JLabel();
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollPane;
    private String[] columns = new String[]{"passport", "number of flights"};
    private JButton backButton;

    public void init(){
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Main.frame.add(panel);

        JButton search = new JButton();
        search.setText("Find passenger who booked all flights");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findPassengerWithAllFlights();
            }
        });
        panel.add(search);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                Main.panel.setVisible(true);
            }
        });
        panel.add(backButton);

    }

    public void findPassengerWithAllFlights(){
        try{
            ResultSet mySet = Main.myStat.executeQuery(
                    "SELECT passengers.passport_no, count(flight_no) as 'number_of_flights' " +
                            "FROM passengers left join reserves on passengers.passport_no = reserves.passport_no " +
                            "group by passengers.passport_no " +
                            "having count(*) = (select count(*) from flights);");
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

            label.setText("Passenger who booked all flights");
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
