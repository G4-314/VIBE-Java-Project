
import java.math.BigDecimal;
import java.util.Scanner;

public class PaymentManagement {

    private static PaymentService paymentService =
            new PaymentService();

    public static void showMenu(Scanner sc) {

        while (true) {

            System.out.println("\n===== PAYMENT MANAGEMENT =====");
            System.out.println("1. Make Payment");
            System.out.println("2. View Payment History");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Booking ID: ");
                    int bookingId = sc.nextInt();

                    System.out.print("Enter payment amount: ");
                    BigDecimal amount = sc.nextBigDecimal();

                    System.out.println("\nSelect Payment Method:");
                    System.out.println("1. UPI");
                    System.out.println("2. CARD");
                    System.out.println("3. NET_BANKING");
                    System.out.print("Enter choice: ");

                    int methodChoice = sc.nextInt();
                    String method;

                    switch (methodChoice) {
                        case 1:
                            method = "UPI";
                            break;
                        case 2:
                            method = "CARD";
                            break;
                        case 3:
                            method = "NET_BANKING";
                            break;
                        default:
                            System.out.println("Invalid payment method.");
                            continue;
                    }

                    paymentService.processPayment(
                            bookingId, amount, method);
                    break;

                case 2:
                    System.out.print("Enter Booking ID: ");
                    int historyBookingId = sc.nextInt();

                    paymentService.viewPaymentHistory(
                            historyBookingId);
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

