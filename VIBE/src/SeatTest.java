
import java.util.Scanner;

public class SeatTest {
    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            System.out.print("Enter MySQL password: ");
            String password = sc.nextLine();

            DBConnection.setPassword(password);

            System.out.println("\nTesting Seat Management...");

            SeatManagement.viewSeats(1);
            SeatManagement.showSeatLayout(1);
            System.out.println("\n--- BOOKING CREATION TEST ---");

int[] selectedSeats = {16};

//BookingManagement.createBooking(1, 1, selectedSeats);
System.out.println("\n--- BOOKING HISTORY TEST ---");
BookingManagement.viewBookings(1);
System.out.println("\n--- BOOKING CANCELLATION TEST ---");

BookingManagement.cancelBooking(1, 1);

System.out.println("\n--- BOOKING HISTORY AFTER CANCELLATION ---");
BookingManagement.viewBookings(1);

System.out.println("\n--- SEAT AVAILABILITY AFTER CANCELLATION ---");

boolean availableAfterCancellation =
    BookingManagement.isSeatAvailable(1, 16);

System.out.println(
    availableAfterCancellation
        ? "Seat A2 is AVAILABLE again."
        : "Seat A2 is still NOT AVAILABLE."
);

System.out.println("\n--- SEAT AVAILABILITY TEST ---");

boolean available = BookingManagement.isSeatAvailable(1, 16);

if (available) {
    System.out.println("Seat is AVAILABLE for booking.");
} else {
    System.out.println("Seat is NOT AVAILABLE for booking.");
}

        }
    }
}

