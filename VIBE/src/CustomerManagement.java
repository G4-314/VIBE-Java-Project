
import java.util.Scanner;

public class CustomerManagement {

    public static void showMenu(Scanner sc) {

        while (true) {

            System.out.println("\n===== CUSTOMER MANAGEMENT =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    CustomerRegistration.register(sc);
                    break;

                case 2:
                    Customer customer = CustomerLogin.login(sc);

                    if (customer != null) {
                        System.out.println("Welcome, "
                                + customer.getFullName() + "!");
                        System.out.println("Customer ID: "
                                + customer.getCustomerId());
                    }
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
