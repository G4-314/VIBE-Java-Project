
import java.util.Scanner;

public class CustomerRegistration {

    public static void register(Scanner sc) {

        System.out.println("\n--- CUSTOMER REGISTRATION ---");

        System.out.print("Enter full name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        System.out.print("Create password: ");
        String password = sc.nextLine();

        String salt = PasswordUtil.generateSalt();

        String hash = PasswordUtil.hashPassword(password, salt);

        Customer customer = new Customer(
            0, name, email, phone
        );

        CustomerDAO dao = new CustomerDAO();

        boolean success = dao.registerCustomer(
            customer, hash, salt
        );

        if (success) {
            System.out.println("\nRegistration successful!");
        } else {
            System.out.println("\nRegistration failed!");
        }
    }
}

