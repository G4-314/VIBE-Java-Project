
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public boolean addPayment(Payment payment) {

        String sql = "INSERT INTO payments " +
                "(booking_id, payment_method, amount, payment_status, " +
                "transaction_reference, payment_datetime) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getBookingId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setBigDecimal(3, payment.getAmount());
            ps.setString(4, payment.getPaymentStatus());
            ps.setString(5, payment.getTransactionReference());
            ps.setTimestamp(6,
                    Timestamp.valueOf(payment.getPaymentDateTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Payment error: " + e.getMessage());
            return false;
        }
    }

    public List<Payment> getPaymentsByBooking(int bookingId) {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payments WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Payment payment = new Payment(
                            rs.getInt("payment_id"),
                            rs.getInt("booking_id"),
                            rs.getString("payment_method"),
                            rs.getBigDecimal("amount"),
                            rs.getString("payment_status"),
                            rs.getString("transaction_reference"),
                            rs.getTimestamp("payment_datetime")
                                    .toLocalDateTime()
                    );

                    payments.add(payment);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve payments: "
                    + e.getMessage());
        }

        return payments;
    }
}
