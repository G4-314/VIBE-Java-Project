import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.print("Enter MySQL password: ");
        DBConnection.setPassword(sc.nextLine());

        do {
            System.out.println("\n========== VIBE EVENT BOOKING SYSTEM ==========");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Management");
            System.out.println("3. Event Management");
            System.out.println("4. Seat & Booking Management");
            System.out.println("5. Payment Management");
            System.out.println("6. Reports");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter admin username: ");
                    String username = sc.nextLine();

                    System.out.print("Enter admin password: ");
                    String password = sc.nextLine();

                    Admin admin = AdminLogin.login(username, password);

                    if (admin != null) {
                        System.out.println("Login successful!");
                        System.out.println("Welcome, " + admin.getFullName());
                        AdminManagement.showMenu(sc, admin);
                    } else {
                        System.out.println("Login failed or database unavailable.");
                    }
                    break;

                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    System.out.println("This module will be added soon.");
                    break;

                case 7:
                    System.out.println("Thank you for using VIBE!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 7);
    }
}
