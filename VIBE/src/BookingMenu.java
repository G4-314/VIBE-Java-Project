
import java.util.Scanner;

public class BookingMenu {

    public static void showMenu(Scanner sc) {

        int choice;

        do {
            System.out.println("\n========== SEAT & BOOKING MANAGEMENT ==========");
            System.out.println("1. View Seats");
            System.out.println("2. View Theatre Seat Layout");
            System.out.println("3. Book Seats");
            System.out.println("4. View Booking History");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {

                case 1:
                    System.out.print("Enter Event ID: ");
                    int eventId1 = readInt(sc);
                    SeatManagement.viewSeats(eventId1);
                    break;

                case 2:
                    System.out.print("Enter Event ID: ");
                    int eventId2 = readInt(sc);
                    SeatManagement.showSeatLayout(eventId2);
                    break;

                case 3:
                    System.out.print("Enter Customer ID: ");
                    int customerId = readInt(sc);

                    System.out.print("Enter Event ID: ");
                    int eventId3 = readInt(sc);

                    SeatManagement.showSeatLayout(eventId3);

                    System.out.print("How many seats do you want to book? ");
                    int count = readInt(sc);

                    if (count <= 0 || count > 6) {
                        System.out.println("Please select between 1 and 6 seats.");
                        break;
                    }

                    int[] seatIds = new int[count];

                    for (int i = 0; i < count; i++) {
                        System.out.print("Enter Seat ID " + (i + 1) + ": ");
                        seatIds[i] = readInt(sc);
                    }

                    BookingManagement.createBooking(
                        customerId, eventId3, seatIds
                    );
                    break;

                case 4:
                    System.out.print("Enter Customer ID: ");
                    int historyCustomerId = readInt(sc);

                    BookingManagement.viewBookings(historyCustomerId);
                    break;

                case 5:
                    System.out.print("Enter Customer ID: ");
                    int cancelCustomerId = readInt(sc);

                    System.out.print("Enter Booking ID: ");
                    int bookingId = readInt(sc);

                    BookingManagement.cancelBooking(
                        cancelCustomerId, bookingId
                    );
                    break;

                case 6:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);
    }

    private static int readInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return -1;
        }
    }
}
