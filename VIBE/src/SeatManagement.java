
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatManagement {

    public static void viewSeats(int eventId) {

        String sql = "SELECT * FROM seats WHERE event_id = ? "
                   + "ORDER BY row_label, seat_number";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\n--- SEATS FOR EVENT " + eventId + " ---");
                System.out.println("Seat ID\tSeat\tStatus");

                while (rs.next()) {
                    found = true;

                    Seat seat = new Seat(
                        rs.getInt("seat_id"),
                        rs.getInt("event_id"),
                        rs.getString("row_label"),
                        rs.getInt("seat_number"),
                        rs.getString("seat_status")
                    );

                    System.out.println(
                        seat.getSeatId() + "\t"
                        + seat.getRowLabel() + seat.getSeatNumber() + "\t"
                        + seat.getSeatStatus()
                    );
                }

                if (!found) {
                    System.out.println("No seats found for this event.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading seats: " + e.getMessage());
        }
    }

public static void showSeatLayout(int eventId) {

    String sql = "SELECT row_label, seat_number, seat_status "
               + "FROM seats WHERE event_id = ? "
               + "ORDER BY row_label, seat_number";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, eventId);

        try (ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== SCREEN ==========");
            System.out.println("       EVENT " + eventId);
            System.out.println("----------------------------");

            String previousRow = "";
            boolean found = false;

            while (rs.next()) {
                found = true;

                String row = rs.getString("row_label");
                int number = rs.getInt("seat_number");
                String status = rs.getString("seat_status");

                if (!row.equals(previousRow)) {
                    System.out.println();
                    System.out.print("Row " + row + ": ");
                    previousRow = row;
                }

                if (status.equals("AVAILABLE")) {
                    System.out.print("[ " + row + number + " ] ");
                } else {
                    System.out.print("[ XX ] ");
                }
            }

            if (!found) {
                System.out.println("No seats found for this event.");
            }

            System.out.println("\n\n[Seat] = Available");
            System.out.println("[XX]   = Booked");
        }

    } catch (SQLException e) {
        System.out.println("Error displaying seat layout: " + e.getMessage());
    }
}

}
