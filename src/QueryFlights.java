import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-03-20.
 */
public class QueryFlights extends JFrame {
    private JComboBox cb_airportid_depart;
    private Container c;
    private JScrollPane sp;
    private JLabel label;
    private JButton button;
    private JTable table;
    private String[] columns = new String[] {
            "Flight Number", "Cost", "Depart From", "Departure Date", "Departure Time",
            "Arrive In", "Arrival Date", "Arrival Time", "Seats Remaining"};
    private Object[][] data;


    public void init() {
        setTitle("Search for Flights");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        cb_airportid_depart = new JComboBox();

        try{
            ResultSet mySet = Main.myStat.executeQuery("select * from flights");

            List<String> results = new ArrayList<String>();

            while(mySet.next()){
                String result = mySet.getString("airportid_depart");
                if (!results.contains(result)) {
                    results.add(result);
                }
            }

            for (int i = 0; i < results.size(); i++) {
                cb_airportid_depart.addItem(results.get(i));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        c = new Container();
        c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(cb_airportid_depart);

        button = new JButton("Search");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFlights(cb_airportid_depart.getSelectedItem());
            }
        });
        c.add(button);

        label = new JLabel("Filter by Departing Airport");
        c.add(label);
    }

    private void searchFlights(Object selectedItem) {
        try{
            ResultSet mySet = Main.myStat.executeQuery("select flight_no, cost, airportid_depart, date_depart, time_depart, airportid_arrive, data_arrive, time_arrive, available_seats from flights where airportid_depart = \"" + selectedItem + "\"");

            int rowCount = 0;
            if (mySet.last()) {
                rowCount = mySet.getRow();
                mySet.beforeFirst();
            }

            data = new Object[rowCount][columns.length];
            int j = 0;

            while(mySet.next()){
                for (int i = 0; i < 9; i++) {
                    data[j][i] = mySet.getObject(i+1);
                }
                j++;
            }

            refreshTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        if (sp != null) {
            c.remove(sp);
        }

        table = new JTable(data, columns);
        sp = new JScrollPane(table);
        c.add(sp);
        c.revalidate();
        c.repaint();
    }
}
