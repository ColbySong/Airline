import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by yoonyok on 2016-03-20.
 */
public class MainFrame extends JFrame{
    public MainFrame(){
        setTitle("AirlineBiz");
        setSize(300,200);
        setLocation(10,200);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JTextArea textArea = new JTextArea();
        JButton button = new JButton();
        Container container = getContentPane();

        container.add(textArea, BorderLayout.CENTER);
        container.add(button, BorderLayout.SOUTH);


    }

}
