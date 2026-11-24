import java.sql.*;

public class Reports {

    // Total Events Report
    public void totalEvents() {

        try (Connection con = DBConnection.getConnection()) {

            String query = "SELECT COUNT(*) FROM events";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                System.out.println("Total Events: " + rs.getInt(1));
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Event Seat Report
    public void eventSeatReport() {

        String query = "SELECT e.event_id, e.event_name, " +
                "COUNT(s.seat_id) AS total_seats, " +
                "SUM(CASE WHEN s.seat_status = 'AVAILABLE' " +
                "THEN 1 ELSE 0 END) AS available_seats, " +
                "SUM(CASE WHEN s.seat_status = 'BOOKED' " +
                "THEN 1 ELSE 0 END) AS booked_seats " +
                "FROM events e LEFT JOIN seats s " +
                "ON e.event_id = s.event_id " +
                "GROUP BY e.event_id, e.event_name";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n========== EVENT SEAT REPORT ==========");

            while (rs.next()) {
                System.out.println("Event ID: " + rs.getInt("event_id"));
                System.out.println("Event Name: " + rs.getString("event_name"));
                System.out.println("Total Seats: " + rs.getInt("total_seats"));
                System.out.println("Available Seats: " + rs.getInt("available_seats"));
                System.out.println("Booked Seats: " + rs.getInt("booked_seats"));
                System.out.println("---------------------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Category Report
    public void categoryReport() {

        try (Connection con = DBConnection.getConnection()) {

            String query = "SELECT category, COUNT(*) AS total " +
                    "FROM events GROUP BY category";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n========== CATEGORY REPORT ==========");

            while (rs.next()) {
                System.out.println("Category: " + rs.getString("category"));
                System.out.println("Total Events: " + rs.getInt("total"));
                System.out.println("-------------------------------------");
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}