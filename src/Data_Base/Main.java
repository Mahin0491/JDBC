package Data_Base;

import java.sql.Connection;

public class Main {
    public static void main(String[]args){
        JDBC_CURD db = new JDBC_CURD();
       Connection mahin =  db.connectToDB("JDBC Project","postgres","10062002");
db.createTable(mahin,"employee");
    }
}
