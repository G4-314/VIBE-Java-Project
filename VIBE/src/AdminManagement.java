import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminManagement {

    public static void viewAdmins() {

        String sql = "SELECT admin_id, username, full_name, "
                   + "is_active FROM admins";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- ADMIN LIST ---");

            boolean found = false;

            while (rs.next()) {
                found = true;

                int id = rs.getInt("admin_id");
                String username = rs.getString("username");
                String name = rs.getString("full_name");
                boolean active = rs.getBoolean("is_active");

                System.out.println(
                    "ID: " + id +
                    " | Username: " + username +
                    " | Name: " + name +
                    " | Status: " + (active ? "Active" : "Inactive")
                );
            }

            if (!found) {
                System.out.println("No administrators found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

public static void addAdmin(Scanner sc) {

    System.out.println("\n--- ADD ADMIN ---");

    System.out.print("Enter username: ");
    String username = sc.nextLine().trim();

    System.out.print("Enter full name: ");
    String fullName = sc.nextLine().trim();

    System.out.print("Enter password: ");
    String password = sc.nextLine();

    if (username.isEmpty() || fullName.isEmpty()
            || password.isEmpty()) {
        System.out.println("All fields are required.");
        return;
    }

    String salt = PasswordUtil.generateSalt();
    String hash = PasswordUtil.hashPassword(password, salt);

    String sql = "INSERT INTO admins "
               + "(username, full_name, password_hash, password_salt) "
               + "VALUES (?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);
        ps.setString(2, fullName);
        ps.setString(3, hash);
        ps.setString(4, salt);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Administrator added successfully!");
        }

    } catch (SQLException e) {
        System.out.println("Unable to add admin: " + e.getMessage());
    }
}

public static void changeAdminStatus(Scanner sc, int currentAdminId) {

    System.out.println("\n--- CHANGE ADMIN STATUS ---");

    System.out.print("Enter Admin ID: ");

    int adminId;

    try {
        adminId = Integer.parseInt(sc.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Please enter a valid Admin ID.");
        return;
    }

    if (adminId == currentAdminId) {
        System.out.println("You cannot deactivate or change your own account status.");
        return;
    }

    System.out.println("1. Activate Admin");
    System.out.println("2. Deactivate Admin");
    System.out.print("Enter choice: ");

    String choice = sc.nextLine();

    if (!choice.equals("1") && !choice.equals("2")) {
        System.out.println("Invalid choice.");
        return;
    }

    boolean active = choice.equals("1");

    String sql = "UPDATE admins SET is_active = ? WHERE admin_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setBoolean(1, active);
        ps.setInt(2, adminId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Admin status updated successfully!");
        } else {
            System.out.println("Admin ID not found.");
        }

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}

public static void showMenu(Scanner sc, Admin currentAdmin) {

    int choice;

    do {
        System.out.println("\n========== ADMIN MANAGEMENT ==========");
        System.out.println("1. View All Administrators");
        System.out.println("2. Add New Administrator");
        System.out.println("3. Activate / Deactivate Administrator");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");

        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            choice = 0;
        }

        switch (choice) {
            case 1:
                viewAdmins();
                break;

            case 2:
                addAdmin(sc);
                break;

            case 3:
                changeAdminStatus(sc, currentAdmin.getAdminId());
                break;

            case 4:
                System.out.println("Returning to Main Menu...");
                break;

            default:
                System.out.println("Invalid choice. Try again.");
        }

    } while (choice != 4);
}

}

