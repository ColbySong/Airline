import java.sql.*;

/**
 * Created by Daniel on 2016-03-20.
 */
public class Main {
    public static void main(String[] args) {

        // write your code here
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "1234");
            Statement myStat = myConn.createStatement();

            MainComboBox.run(new MainComboBox(myStat));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
