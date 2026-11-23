
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminSetup {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

        System.out.print("Enter MySQL password: ");
        String dbPassword = sc.nextLine();
        DBConnection.setPassword(dbPassword);

        System.out.print("Enter admin username: ");
        String username = sc.nextLine().trim();

        System.out.print("Enter admin full name: ");
        String fullName = sc.nextLine().trim();

        System.out.print("Enter admin password: ");
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
                System.out.println("Admin account created successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Unable to create admin: " + e.getMessage());
        }
    }
    }
}
