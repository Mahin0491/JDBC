package Data_Base;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC_CURD {
    public Connection connectToDB(String dbName,String userName,String passWord){
        Connection mahin = null;

        try{
            String url = "jdbc:postgresql://localhost:5432/" + dbName;
            mahin = DriverManager.getConnection(url, userName, passWord);

       if(mahin!=null){
           System.out.println("Connection Stablished");
       }
       else{
           System.out.println("Connection Falied");

       }
        }

        catch (Exception e) {
            System.out.println(e);        }
        return mahin;
    }

    public static void main(String[]args){
        JDBC_CURD db = new JDBC_CURD();
        db.connectToDB("JDBC Project","postgres","10062002");

    }
}
