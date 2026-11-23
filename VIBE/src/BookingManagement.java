
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Statement;
import java.util.UUID;

public class BookingManagement {

    public static boolean isSeatAvailable(int eventId, int seatId) {

        String sql = "SELECT seat_status FROM seats "
                   + "WHERE event_id = ? AND seat_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, seatId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("seat_status")
                             .equals("AVAILABLE");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking seat: " + e.getMessage());
        }

        return false;
    }

public static void createBooking(int customerId, int eventId,
                                 int[] seatIds) {

    if (seatIds == null || seatIds.length == 0) {
        System.out.println("Please select at least one seat.");
        return;
    }

    String customerSql =
        "SELECT customer_id FROM customers " +
        "WHERE customer_id = ? AND is_active = TRUE";

    String eventSql =
        "SELECT ticket_price FROM events " +
        "WHERE event_id = ? AND event_status = 'ACTIVE'";

    String seatSql =
        "SELECT seat_status FROM seats " +
        "WHERE seat_id = ? AND event_id = ? FOR UPDATE";

    String bookingSql =
        "INSERT INTO bookings " +
        "(booking_reference, customer_id, event_id, total_amount, booking_status) " +
        "VALUES (?, ?, ?, ?, 'PENDING')";

    String bookingSeatSql =
        "INSERT INTO booking_seats " +
        "(booking_id, seat_id, ticket_price) VALUES (?, ?, ?)";

    String updateSeatSql =
        "UPDATE seats SET seat_status = 'BOOKED' " +
        "WHERE seat_id = ? AND event_id = ? " +
        "AND seat_status = 'AVAILABLE'";

    try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false);

        try {
            // Step 1: Check customer
            try (PreparedStatement ps = con.prepareStatement(customerSql)) {
                ps.setInt(1, customerId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Customer not found or inactive.");
                    }
                }
            }

            // Step 2: Check event and get ticket price
            BigDecimal ticketPrice;

            try (PreparedStatement ps = con.prepareStatement(eventSql)) {
                ps.setInt(1, eventId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Event not found or inactive.");
                    }

                    ticketPrice = rs.getBigDecimal("ticket_price");
                }
            }

            // Step 3: Check and lock selected seats
            // Sort IDs to keep a consistent locking order.
            int[] sortedSeatIds = seatIds.clone();
            java.util.Arrays.sort(sortedSeatIds);

            for (int i = 0; i < sortedSeatIds.length; i++) {

                if (i > 0 && sortedSeatIds[i] == sortedSeatIds[i - 1]) {
                    throw new SQLException("Duplicate seat selected.");
                }

                try (PreparedStatement ps = con.prepareStatement(seatSql)) {
                    ps.setInt(1, sortedSeatIds[i]);
                    ps.setInt(2, eventId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                "Seat " + sortedSeatIds[i] + " does not exist for this event."
                            );
                        }

                        if (!rs.getString("seat_status").equals("AVAILABLE")) {
                            throw new SQLException(
                                "Seat " + sortedSeatIds[i] + " is already booked."
                            );
                        }
                    }
                }
            }

            // Step 4: Calculate total amount
            BigDecimal totalAmount =
                ticketPrice.multiply(BigDecimal.valueOf(sortedSeatIds.length));

            String reference =
                "VIBE-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);

            // Step 5: Create booking
            int bookingId;

            try (PreparedStatement ps = con.prepareStatement(
                    bookingSql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, reference);
                ps.setInt(2, customerId);
                ps.setInt(3, eventId);
                ps.setBigDecimal(4, totalAmount);

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Could not generate booking ID.");
                    }

                    bookingId = keys.getInt(1);
                }
            }

            // Step 6: Add booking seats and update seat status
            for (int seatId : sortedSeatIds) {

                try (PreparedStatement ps =
                         con.prepareStatement(bookingSeatSql)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, seatId);
                    ps.setBigDecimal(3, ticketPrice);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                         con.prepareStatement(updateSeatSql)) {

                    ps.setInt(1, seatId);
                    ps.setInt(2, eventId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Seat " + seatId + " is no longer available."
                        );
                    }
                }
            }

            // Step 7: Save all changes
            con.commit();

            System.out.println("\nBooking created successfully!");
            System.out.println("Booking ID: " + bookingId);
            System.out.println("Reference: " + reference);
            System.out.println("Seats booked: " + sortedSeatIds.length);
            System.out.println("Total amount: Rs. " + totalAmount);
            System.out.println("Booking status: PENDING");

        } catch (SQLException e) {
            con.rollback();
            System.out.println("Booking failed: " + e.getMessage());
            System.out.println("No booking changes were saved.");
        }

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}

public static void viewBookings(int customerId) {

    String sql =
        "SELECT b.booking_id, b.booking_reference, " +
        "e.event_name, b.total_amount, b.booking_status, " +
        "s.row_label, s.seat_number " +
        "FROM bookings b " +
        "JOIN events e ON b.event_id = e.event_id " +
        "JOIN booking_seats bs ON b.booking_id = bs.booking_id " +
        "JOIN seats s ON bs.seat_id = s.seat_id " +
        "WHERE b.customer_id = ? " +
        "ORDER BY b.booking_id, s.row_label, s.seat_number";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, customerId);

        try (ResultSet rs = ps.executeQuery()) {

            boolean found = false;
            int previousBookingId = -1;

            System.out.println("\n===== BOOKING HISTORY =====");

            while (rs.next()) {

                found = true;

                int bookingId = rs.getInt("booking_id");

                if (bookingId != previousBookingId) {

                    if (previousBookingId != -1) {
                        System.out.println();
                    }

                    System.out.println("\nBooking ID: " + bookingId);
                    System.out.println(
                        "Reference: " + rs.getString("booking_reference")
                    );
                    System.out.println(
                        "Event: " + rs.getString("event_name")
                    );
                    System.out.println(
                        "Total Amount: Rs. " + rs.getBigDecimal("total_amount")
                    );
                    System.out.println(
                        "Status: " + rs.getString("booking_status")
                    );

                    System.out.print("Seats: ");

                    previousBookingId = bookingId;
                }

                System.out.print(
                    rs.getString("row_label") +
                    rs.getInt("seat_number") + " "
                );
            }

            if (!found) {
                System.out.println("No bookings found for this customer.");
            }

            System.out.println();
        }

    } catch (SQLException e) {
        System.out.println("Error loading bookings: " + e.getMessage());
    }
}

public static void cancelBooking(int customerId, int bookingId) {

    String checkSql =
        "SELECT booking_status FROM bookings " +
        "WHERE booking_id = ? AND customer_id = ? FOR UPDATE";

    String seatsSql =
        "SELECT s.seat_id FROM seats s " +
        "JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
        "WHERE bs.booking_id = ? FOR UPDATE";

    String updateBookingSql =
        "UPDATE bookings SET booking_status = 'CANCELLED' " +
        "WHERE booking_id = ?";

    String updateSeatSql =
        "UPDATE seats SET seat_status = 'AVAILABLE' " +
        "WHERE seat_id = ? AND seat_status = 'BOOKED'";

    try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false);

        try {
            // Check the booking and lock it
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, bookingId);
                ps.setInt(2, customerId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Booking not found for this customer.");
                    }

                    String status = rs.getString("booking_status");

                    if (!status.equals("PENDING")) {
                        throw new SQLException(
                            "Only PENDING bookings can be cancelled here."
                        );
                    }
                }
            }

            // Don't release seats if a successful payment exists
            String paymentSql =
                "SELECT payment_id FROM payments " +
                "WHERE booking_id = ? AND payment_status = 'SUCCESS'";

            try (PreparedStatement ps = con.prepareStatement(paymentSql)) {
                ps.setInt(1, bookingId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new SQLException(
                            "This booking has a successful payment. " +
                            "Payment/refund handling is required."
                        );
                    }
                }
            }

            // Find and lock the seats associated with the booking
            java.util.ArrayList<Integer> seatIds =
                new java.util.ArrayList<>();

            try (PreparedStatement ps = con.prepareStatement(seatsSql)) {
                ps.setInt(1, bookingId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        seatIds.add(rs.getInt("seat_id"));
                    }
                }
            }

            if (seatIds.isEmpty()) {
                throw new SQLException("No seats found for this booking.");
            }

            // Cancel the booking
            try (PreparedStatement ps =
                     con.prepareStatement(updateBookingSql)) {

                ps.setInt(1, bookingId);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("Could not cancel booking.");
                }
            }

            // Release each reserved seat
            for (int seatId : seatIds) {
                try (PreparedStatement ps =
                         con.prepareStatement(updateSeatSql)) {

                    ps.setInt(1, seatId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Could not release seat " + seatId
                        );
                    }
                }
            }

            con.commit();

            System.out.println("\nBooking cancelled successfully!");
            System.out.println("Booking ID: " + bookingId);
            System.out.println("Seats released: " + seatIds.size());

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackError) {
                System.out.println(
                    "Rollback failed: " + rollbackError.getMessage()
                );
            }

            System.out.println("Cancellation failed: " + e.getMessage());
        }

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}

}

