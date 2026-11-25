import java.util.Scanner;

public class App {
    public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    int choice;

    EventManagement event = new EventManagement(sc);
    Reports reports = new Reports();

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
                CustomerManagement.showMenu(sc);
                break;

            case 3:
                int eventChoice;

                do {
                    System.out.println("\n========== EVENT MANAGEMENT ==========");
                    System.out.println("1. Add Event");
                    System.out.println("2. View Events");
                    System.out.println("3. Search Event");
                    System.out.println("4. Delete Event");
                    System.out.println("5. Back to Main Menu");
                    System.out.print("Enter your choice: ");

                    try {
                        eventChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        eventChoice = 0;
                    }

                    switch (eventChoice) {

                        case 1:
                            event.addEvent();
                            break;

                        case 2:
                            event.viewEvents();
                            break;

                        case 3:
                            event.searchEvent();
                            break;

                        case 4:
                            event.deleteEvent();
                            break;

                        case 5:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice!");
                    }

                } while (eventChoice != 5);
                break;

            case 4:
                BookingMenu.showMenu(sc);
                break;

            case 5:
                PaymentManagement.showMenu(sc);
                break;

            case 6:
                int reportChoice;

                do {
                    System.out.println("\n========== REPORTS ==========");
                    System.out.println("1. Total Events Report");
                    System.out.println("2. Event Seat Report");
                    System.out.println("3. Category Report");
                    System.out.println("4. Back to Main Menu");
                    System.out.print("Enter your choice: ");

                    try {
                        reportChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        reportChoice = 0;
                    }

                    switch (reportChoice) {

                        case 1:
                            reports.totalEvents();
                            break;

                        case 2:
                            reports.eventSeatReport();
                            break;

                        case 3:
                            reports.categoryReport();
                            break;

                        case 4:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice!");
                    }

                } while (reportChoice != 4);
                break;

            case 7:
                System.out.println("Thank you for using VIBE!");
                break;

            default:
                System.out.println("Invalid choice. Try again.");
        }

    } while (choice != 7);

    sc.close();
}

    

}