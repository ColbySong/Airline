import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenGee on 2016-03-20.
 */
public class MainComboBox extends JApplet {

    private Statement myStat;

    public MainComboBox(Statement myStat) {
        this.myStat = myStat;
    }

    private JTextField  t = new JTextField(15);

    private JComboBox c = new JComboBox();

    private JButton b = new JButton("Add new course");

    public void init() {
        try{
            ResultSet mySet = myStat.executeQuery("select * from passengers");

            while(mySet.next()){
                c.addItem(mySet.getString("passport_no"));
            }
            t.setEditable(false);

            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    query(c.getSelectedItem());
                }
            });

            Container cp = getContentPane();
            cp.setLayout(new FlowLayout());
            cp.add(t);
            cp.add(c);
            cp.add(b);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void query(Object selectedItem) {
        try{
            ResultSet mySet = myStat.executeQuery("select * from passengers where passport_no = \"" + selectedItem + "\"");
            while(mySet.next()){
                System.out.println(mySet.getString("first_name") + " " + mySet.getString("last_name"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void run(JApplet applet) {
        JFrame frame = new MainFrame();
        frame.getContentPane().add(applet);
        applet.init();
        applet.start();
    }
}
