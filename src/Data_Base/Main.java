/// File 1: Main.java (package Data_Base)
package Data_Base;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        JDBC_CURD db = new JDBC_CURD();

        Connection conn = db.connectToDB("JDBC Project", "postgres", "10062002");
        db.createCustomerTable(conn, "customer");

        while (true) {
            db.showMenu();
            System.out.print("Enter choice (0-6): ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 0) {
                System.out.println("👋 Thank you!");
                break;
            }

            switch (choice) {
                case 1:
                    // ✅ AUTO GENERATE ID
                    String newId = db.generateCustomerId(conn, "customer");
                    System.out.println("🎉 Your new Customer ID: " + newId);
                    System.out.println("📝 Save this ID for future login!");

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Address: ");
                    String address = sc.nextLine();
                    System.out.print("Enter Initial Deposit: ");
                    double deposit = sc.nextDouble();
                    sc.nextLine();

                    db.insertCustomer(conn, "customer", newId, name, address, deposit);
                    break;

                case 2:
                case 3:
                case 4:
                case 6:
                    System.out.print("Enter Customer ID: ");
                    String id = sc.nextLine();

                    if (!db.customerExists(conn, "customer", id)) {
                        System.out.println("❌ Customer '" + id + "' not found!");
                        System.out.println("💡 Create new account first or use correct ID.");
                        break;
                    }

                    switch (choice) {
                        case 2: db.readCustomer(conn, "customer", id); break;
                        case 3:
                            System.out.print("Enter Deposit Amount: ");
                            db.deposit(conn, "customer", id, sc.nextDouble());
                            sc.nextLine(); break;
                        case 4:
                            System.out.print("Enter Withdraw Amount: ");
                            db.withdraw(conn, "customer", id, sc.nextDouble());
                            sc.nextLine(); break;
                        case 6: db.deleteCustomer(conn, "customer", id); break;
                    }
                    break;

                case 5:
                    db.readAllCustomers(conn, "customer");
                    break;

                default:
                    System.out.println("❌ Invalid choice!");
            }
            System.out.println();
        }
        sc.close();
    }
}
