package Data_Base;

import java.sql.Connection;      //to connect with Database and It is one of the interface of JDBC-API
import java.sql.DriverManager;   //to load PostgreSQL DRIVER
import java.sql.ResultSet;       //to read data from SELECT query
import java.sql.Statement;       //to execute SQL Query CURD operartion and It is an interface of JDBC-API

public class JDBC_CURD {

    public Connection connectToDB(String dbName, String userName, String passWord) {
        Connection mahin = null;    //Connection is an interface type here

        try {
            String url = "jdbc:postgresql://localhost:5432/" + dbName;
            mahin = DriverManager.getConnection(url, userName, passWord);
            System.out.println("✅ Connected to DB");
        }
        catch (Exception e) {
            System.out.println("❌ Connection failed: " + e);
        }
        return mahin;
    }

    public void createCustomerTable(Connection mahin, String tableName) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + tableName +
                    "(customer_id VARCHAR(20) PRIMARY KEY, name VARCHAR(100), " +
                    "address VARCHAR(200), balance DOUBLE PRECISION DEFAULT 0)";

            //CREATE TABLE IF NOT EXISTS customer
            //(customer_id VARCHAR(20) PRIMARY KEY, name VARCHAR(100),
            // address VARCHAR(200), balance DOUBLE PRECISION DEFAULT 0)


            Statement stmt = mahin.createStatement();
            stmt.executeUpdate(query);
            System.out.println("✅ Customer table ready!");
        }
        catch (Exception e) {
            System.out.println("❌ Table error: " + e);
        }
    }

    // AUTO GENERATE 3-DIGIT ID (001, 002, 003...)
    public String generateCustomerId(Connection mahin, String tableName) {
        try {

            String countQuery = "SELECT COUNT(*) FROM " + tableName; // Count existing customers
            Statement stmt = mahin.createStatement();
            ResultSet rs = stmt.executeQuery(countQuery);            //Come a resulset from DB
            rs.next();                                               //push coursor in next to read from result row and table
            int nextId = rs.getInt(1) + 1;

            return String.format("C%03d", nextId);
        }
        catch (Exception e) {
            System.out.println("❌ ID generation error: " + e);
            return "C001";
        }
    }

    public boolean customerExists(Connection mahin, String tableName, String id) {
        try {
            String query = String.format("SELECT COUNT(*) FROM %s WHERE customer_id = '%s'", tableName, id);
            Statement stmt = mahin.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void showMenu() {
        System.out.println("\n🏦 BANK MANAGEMENT SYSTEM of <<-- MAHIN BANK -->> 🏦");
        System.out.println("1. ➕ Create New Account (Auto ID)");
        System.out.println("2. 👁️  View Details");
        System.out.println("3. 💰 Deposit");
        System.out.println("4. 💸 Withdraw");
        System.out.println("5. 📋 All Customers");
        System.out.println("6. 🗑️  Delete");
        System.out.println("0. ❌ Exit");
    }

    public void insertCustomer(Connection mahin, String tableName, String id, String name, String address, double balance) {
        try {
            String query = String.format(
                    "INSERT INTO %s(customer_id, name, address, balance) VALUES('%s','%s','%s',%f)",
                    tableName, id, name, address, balance
            );
            Statement stmt = mahin.createStatement();
            stmt.executeUpdate(query);
            System.out.println("✅ Account '" + id + "' created!");
            System.out.printf("   👤 %s | %s | Initial Balance: %.2f%n", name, address, balance);
        } catch (Exception e) {
            System.out.println("❌ Create error: " + e.getMessage());
        }
    }

    public void readCustomer(Connection mahin, String tableName, String id) {
        try {
            String query = String.format("SELECT * FROM %s WHERE customer_id = '%s'", tableName, id);
            Statement stmt = mahin.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                System.out.printf("\n👤 ID: %s | %s | %s | Balance: %.2f%n",
                        rs.getString("customer_id"), rs.getString("name"),
                        rs.getString("address"), rs.getDouble("balance"));
            } else {
                System.out.println("❌ Customer not found!");
            }
        } catch (Exception e) {
            System.out.println("❌ Read error: " + e);
        }
    }

    public void deposit(Connection mahin, String tableName, String id, double amount) {
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive!");
            return;
        }
        try {
            String query = String.format(
                    "UPDATE %s SET balance = balance + %f WHERE customer_id = '%s'",
                    tableName, amount, id
            );
            Statement stmt = mahin.createStatement();
            int rows = stmt.executeUpdate(query);
            if (rows > 0) {
                System.out.println("✅ Deposited " + amount + " to " + id);
            } else {
                System.out.println("❌ Customer not found!");
            }
        } catch (Exception e) {
            System.out.println("❌ Deposit error: " + e);
        }
    }

    public void withdraw(Connection mahin, String tableName, String id, double amount) {
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive!");
            return;
        }
        try {
            ResultSet rs = mahin.createStatement().executeQuery(
                    String.format("SELECT balance FROM %s WHERE customer_id = '%s'", tableName, id)
            );
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (amount > balance) {
                    System.out.println("❌ Insufficient balance: " + balance);
                    return;
                }
            }
            String query = String.format(
                    "UPDATE %s SET balance = balance - %f WHERE customer_id = '%s'",
                    tableName, amount, id
            );
            int rows = mahin.createStatement().executeUpdate(query);
            if (rows > 0) {
                System.out.println("✅ Withdrawn " + amount + " from " + id);
            }
        } catch (Exception e) {
            System.out.println("❌ Withdraw error: " + e);
        }
    }

    public void readAllCustomers(Connection mahin, String tableName) {
        try {
            ResultSet rs = mahin.createStatement().executeQuery("SELECT * FROM " + tableName + " ORDER BY customer_id");
            System.out.println("\n📊 ALL CUSTOMERS:");
            while (rs.next()) {
                System.out.printf("ID:%-6s | %-15s | Balance:%.2f%n",
                        rs.getString("customer_id"), rs.getString("name"), rs.getDouble("balance"));
            }
        } catch (Exception e) {
            System.out.println("❌ List error: " + e);
        }
    }

    public void deleteCustomer(Connection mahin, String tableName, String id) {
        try {
            String query = String.format("DELETE FROM %s WHERE customer_id = '%s'", tableName, id);
            int rows = mahin.createStatement().executeUpdate(query);
            System.out.println(rows > 0 ? "✅ Deleted " + id : "❌ Not found");
        } catch (Exception e) {
            System.out.println("❌ Delete error: " + e);
        }
    }
}
