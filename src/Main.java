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
            ResultSet mySet = myStat.executeQuery("select * from passengers");
            while(mySet.next()){
                System.out.println(mySet.getString("first_name"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
