
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    public boolean processPayment(int bookingId, BigDecimal amount,
                                  String paymentMethod) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid payment amount.");
            return false;
        }

        if (!paymentMethod.equals("UPI")
                && !paymentMethod.equals("CARD")
                && !paymentMethod.equals("NET_BANKING")) {
            System.out.println("Invalid payment method.");
            return false;
        }

        String transactionReference =
                "VIBE-" + UUID.randomUUID()
                        .toString().substring(0, 8).toUpperCase();

        String bookingSql =
                "SELECT total_amount, booking_status " +
                "FROM bookings WHERE booking_id = ? FOR UPDATE";

        String checkPaymentSql =
                "SELECT payment_id FROM payments " +
                "WHERE booking_id = ? AND payment_status = 'SUCCESS'";

        String insertPaymentSql =
                "INSERT INTO payments " +
                "(booking_id, payment_method, amount, payment_status, " +
                "transaction_reference) VALUES (?, ?, ?, 'SUCCESS', ?)";

        String updateBookingSql =
                "UPDATE bookings SET booking_status = 'CONFIRMED' " +
                "WHERE booking_id = ? AND booking_status = 'PENDING'";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                BigDecimal actualAmount;
                String bookingStatus;

                // Step 1: Find and lock the booking
                try (PreparedStatement ps =
                        con.prepareStatement(bookingSql)) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Booking not found.");
                        }

                        actualAmount = rs.getBigDecimal("total_amount");
                        bookingStatus = rs.getString("booking_status");
                    }
                }

                // Step 2: Check booking status
                if (!"PENDING".equals(bookingStatus)) {
                    throw new SQLException(
                            "Only PENDING bookings can be paid for.");
                }

                // Step 3: Verify the amount
                if (actualAmount.compareTo(amount) != 0) {
                    throw new SQLException(
                            "Incorrect amount. Booking total is Rs. "
                                    + actualAmount);
                }

                // Step 4: Prevent duplicate successful payments
                try (PreparedStatement ps =
                        con.prepareStatement(checkPaymentSql)) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException(
                                    "This booking has already been paid.");
                        }
                    }
                }

                // Step 5: Insert payment
                try (PreparedStatement ps =
                        con.prepareStatement(insertPaymentSql)) {

                    ps.setInt(1, bookingId);
                    ps.setString(2, paymentMethod);
                    ps.setBigDecimal(3, actualAmount);
                    ps.setString(4, transactionReference);

                    ps.executeUpdate();
                }

                // Step 6: Confirm booking
                try (PreparedStatement ps =
                        con.prepareStatement(updateBookingSql)) {

                    ps.setInt(1, bookingId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Could not confirm booking.");
                    }
                }

                // Step 7: Save both changes together
                con.commit();

                System.out.println("\nPayment successful!");
                System.out.println("Transaction ID: "
                        + transactionReference);
                System.out.println("Amount: Rs. " + actualAmount);
                System.out.println("Booking status: CONFIRMED");

                return true;

            } catch (SQLException e) {

                con.rollback();

                System.out.println("\nPayment failed: " + e.getMessage());
                System.out.println("No payment changes were saved.");

                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public void viewPaymentHistory(int bookingId) {

        List<Payment> payments =
                paymentDAO.getPaymentsByBooking(bookingId);

        if (payments.isEmpty()) {
            System.out.println("No payment records found.");
            return;
        }

        System.out.println("\n----- PAYMENT HISTORY -----");

        for (Payment payment : payments) {
            System.out.println("Payment ID: " + payment.getPaymentId());
            System.out.println("Method: " + payment.getPaymentMethod());
            System.out.println("Amount: Rs. " + payment.getAmount());
            System.out.println("Status: " + payment.getPaymentStatus());
            System.out.println("Transaction ID: "
                    + payment.getTransactionReference());
            System.out.println("---------------------------");
        }
    }
}
