import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class EventManagement {
    private Scanner sc;

public EventManagement(Scanner sc) {
    this.sc = sc;
}

// Add Event
public void addEvent() {

    try {
        Connection con = DBConnection.getConnection();

        System.out.print("Enter Event Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Event Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Event Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.print("Enter Event Location: ");
        String location = sc.nextLine();

        System.out.print("Enter Ticket Price: ");
        double price = Double.parseDouble(sc.nextLine());

        System.out.print("Enter Available Seats: ");
        int seats = Integer.parseInt(sc.nextLine());

        String query = "INSERT INTO events (event_name, category, event_date, location, price, available_seats) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);
        ps.setString(2, category);
        ps.setString(3, date);
        ps.setString(4, location);
        ps.setDouble(5, price);
        ps.setInt(6, seats);

        int result = ps.executeUpdate();

        if (result > 0) {
            System.out.println("Event added successfully!");
        }

        ps.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error adding event: " + e.getMessage());
    }
}

// View All Events
public void viewEvents() {

    try {
        Connection con = DBConnection.getConnection();

        String query = "SELECT * FROM events";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("\n========== AVAILABLE EVENTS ==========");

        while (rs.next()) {

            System.out.println("Event ID: " + rs.getInt("event_id"));
            System.out.println("Event Name: " + rs.getString("event_name"));
            System.out.println("Category: " + rs.getString("category"));
            System.out.println("Date: " + rs.getString("event_date"));
            System.out.println("Location: " + rs.getString("location"));
            System.out.println("Price: " + rs.getDouble("price"));
            System.out.println("Available Seats: " + rs.getInt("available_seats"));

            System.out.println("--------------------------------------");
        }

        rs.close();
        st.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error viewing events: " + e.getMessage());
    }
}

// Search Event
public void searchEvent() {

    try {
        Connection con = DBConnection.getConnection();

        System.out.print("Enter Event ID: ");
        int id = Integer.parseInt(sc.nextLine());

        String query = "SELECT * FROM events WHERE event_id = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            System.out.println("\nEvent Found!");
            System.out.println("Event ID: " + rs.getInt("event_id"));
            System.out.println("Name: " + rs.getString("event_name"));
            System.out.println("Category: " + rs.getString("category"));
            System.out.println("Date: " + rs.getString("event_date"));
            System.out.println("Location: " + rs.getString("location"));
            System.out.println("Price: " + rs.getDouble("price"));
            System.out.println("Available Seats: " + rs.getInt("available_seats"));

        } else {
            System.out.println("Event not found!");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error searching event: " + e.getMessage());
    }
}

// Delete Event
public void deleteEvent() {

    try {
        Connection con = DBConnection.getConnection();

        System.out.print("Enter Event ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String query = "DELETE FROM events WHERE event_id = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        int result = ps.executeUpdate();

        if (result > 0) {
            System.out.println("Event deleted successfully!");
        } else {
            System.out.println("Event not found!");
        }

        ps.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error deleting event: " + e.getMessage());
    }
}
}