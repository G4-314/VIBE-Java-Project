import java.sql.*;
import java.util.Scanner;
import java.math.BigDecimal;

public class EventManagement {

    private Scanner sc;

    public EventManagement(Scanner sc) {
        this.sc = sc;
    }

    // Add Event
    public void addEvent() {

        try {
            System.out.print("Enter Event Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Category (MOVIE/CONCERT/COMEDY): ");
            String category = sc.nextLine().trim().toUpperCase();

            if (!category.equals("MOVIE")
                    && !category.equals("CONCERT")
                    && !category.equals("COMEDY")) {
                System.out.println("Invalid category!");
                return;
            }

            System.out.print("Enter Description: ");
            String description = sc.nextLine();

            System.out.print("Enter Event Date (YYYY-MM-DD): ");
            Date date = Date.valueOf(sc.nextLine());

            System.out.print("Enter Event Time (HH:MM:SS): ");
            Time time = Time.valueOf(sc.nextLine());

            System.out.print("Enter Event Venue: ");
            String venue = sc.nextLine();

            System.out.print("Enter Ticket Price: ");
            BigDecimal price = new BigDecimal(sc.nextLine());

            System.out.print("Enter Number of Rows (1-26): ");
            int rows = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Seats Per Row (1-100): ");
            int seatsPerRow = Integer.parseInt(sc.nextLine());

            if (name.trim().isEmpty()
                    || venue.trim().isEmpty()
                    || price.signum() <= 0
                    || rows < 1 || rows > 26
                    || seatsPerRow < 1 || seatsPerRow > 100) {

                System.out.println("Invalid event details!");
                return;
            }

            try (Connection con = DBConnection.getConnection()) {

                con.setAutoCommit(false);

                try {
                    String query =
                        "INSERT INTO events "
                        + "(event_name, category, description, venue, "
                        + "event_date, event_time, ticket_price) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                    int eventId;

                    try (PreparedStatement ps = con.prepareStatement(
                            query, Statement.RETURN_GENERATED_KEYS)) {

                        ps.setString(1, name);
                        ps.setString(2, category);
                        ps.setString(3, description);
                        ps.setString(4, venue);
                        ps.setDate(5, date);
                        ps.setTime(6, time);
                        ps.setBigDecimal(7, price);

                        ps.executeUpdate();

                        try (ResultSet rs = ps.getGeneratedKeys()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                    "Event ID not generated."
                                );
                            }

                            eventId = rs.getInt(1);
                        }
                    }

                    String seatQuery =
                        "INSERT INTO seats "
                        + "(event_id, row_label, seat_number, seat_status) "
                        + "VALUES (?, ?, ?, 'AVAILABLE')";

                    try (PreparedStatement ps =
                            con.prepareStatement(seatQuery)) {

                        for (int i = 0; i < rows; i++) {

                            String rowLabel =
                                String.valueOf((char) ('A' + i));

                            for (int j = 1; j <= seatsPerRow; j++) {

                                ps.setInt(1, eventId);
                                ps.setString(2, rowLabel);
                                ps.setInt(3, j);

                                ps.executeUpdate();
                            }
                        }
                    }

                    con.commit();

                    System.out.println("Event added successfully!");
                    System.out.println("Event ID: " + eventId);
                    System.out.println(
                        "Total Seats: " + (rows * seatsPerRow)
                    );

                } catch (SQLException e) {
                    con.rollback();
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println(
                "Error adding event: " + e.getMessage()
            );
        }
    }

    // View All Events
    public void viewEvents() {

        String query =
            "SELECT e.*, "
            + "(SELECT COUNT(*) FROM seats s "
            + "WHERE s.event_id = e.event_id "
            + "AND s.seat_status = 'AVAILABLE') "
            + "AS available_seats "
            + "FROM events e ORDER BY e.event_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n========== ALL EVENTS ==========");

            while (rs.next()) {
                displayEvent(rs);
            }

        } catch (SQLException e) {
            System.out.println(
                "Error viewing events: " + e.getMessage()
            );
        }
    }

    // Search Event
    public void searchEvent() {

        try {
            System.out.print("Enter Event ID: ");
            int id = Integer.parseInt(sc.nextLine());

            String query =
                "SELECT e.*, "
                + "(SELECT COUNT(*) FROM seats s "
                + "WHERE s.event_id = e.event_id "
                + "AND s.seat_status = 'AVAILABLE') "
                + "AS available_seats "
                + "FROM events e WHERE e.event_id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        System.out.println("\nEvent Found!");
                        displayEvent(rs);
                    } else {
                        System.out.println("Event not found!");
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric Event ID.");

        } catch (SQLException e) {
            System.out.println(
                "Error searching event: " + e.getMessage()
            );
        }
    }

    // Display Event Details
    private void displayEvent(ResultSet rs) throws SQLException {

        System.out.println("Event ID: " + rs.getInt("event_id"));
        System.out.println("Event Name: " + rs.getString("event_name"));
        System.out.println("Category: " + rs.getString("category"));
        System.out.println("Description: " + rs.getString("description"));
        System.out.println("Date: " + rs.getDate("event_date"));
        System.out.println("Time: " + rs.getTime("event_time"));
        System.out.println("Venue: " + rs.getString("venue"));
        System.out.println("Price: Rs. " + rs.getBigDecimal("ticket_price"));
        System.out.println("Available Seats: " + rs.getInt("available_seats"));
        System.out.println("Status: " + rs.getString("event_status"));

        System.out.println("--------------------------------------");
    }

    // Safely Deactivate Event
    public void deleteEvent() {

        try {
            System.out.print("Enter Event ID to deactivate: ");
            int id = Integer.parseInt(sc.nextLine());

            String query =
                "UPDATE events SET event_status = 'INACTIVE' "
                + "WHERE event_id = ? AND event_status = 'ACTIVE'";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, id);

                int result = ps.executeUpdate();

                if (result > 0) {
                    System.out.println("Event deactivated successfully!");
                } else {
                    System.out.println(
                        "Event not found or already inactive!"
                    );
                }
            }

        } catch (Exception e) {
            System.out.println(
                "Error deactivating event: " + e.getMessage()
            );
        }
    }
}