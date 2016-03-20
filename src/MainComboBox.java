import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by BenGee on 2016-03-20.
 */
public class MainComboBox extends JApplet {

    private String[] courseList = {"CPSC 110", "CPSC 210", "CPSC 221", "CPSC 213", "CPSC 310", "CPSC 304", "CPSC 317"};

    private JTextField  t = new JTextField(15);

    private JComboBox c = new JComboBox();

    private JButton b = new JButton("Add new course");

    private int count = 0;

    public void init() {
        for (int i = 0; i < 4; i++) {
            c.addItem(courseList[count++]);
        }
        t.setEditable(false);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count < courseList.length) {
                    c.addItem(courseList[count++]);
                }
            }
        });
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.setText("index: " + c.getSelectedIndex() + "   "
                        + ((JComboBox) e.getSource()).getSelectedItem().toString());
            }
        });

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(t);
        cp.add(c);
        cp.add(b);
    }

    public static void run(JApplet applet) {
        JFrame frame = new MainFrame();
        frame.getContentPane().add(applet);
        applet.init();
        applet.start();
    }
}
