
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminLogin {

    public static Admin login(String username, String password) {

        String sql = "SELECT * FROM admins "
                   + "WHERE username = ? AND is_active = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String storedHash =
                        rs.getString("password_hash");

                    String storedSalt =
                        rs.getString("password_salt");

                    if (PasswordUtil.verifyPassword(
                            password, storedHash, storedSalt)) {

                        return new Admin(
                            rs.getInt("admin_id"),
                            rs.getString("username"),
                            rs.getString("full_name")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return null;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter MySQL password: ");
        DBConnection.setPassword(sc.nextLine());

        System.out.print("Enter admin username: ");
        String username = sc.nextLine();

        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        Admin admin = login(username, password);

        
if (admin != null) {
    System.out.println("Login successful!");
    System.out.println("Welcome, " + admin.getFullName());

    AdminManagement.showMenu(sc, admin);

} else {
    System.out.println("Login failed or database unavailable.");
}

    }
}

