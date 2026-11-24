
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerLogin {

    public static Customer login(Scanner sc) {

        System.out.println("\n--- CUSTOMER LOGIN ---");

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM customers WHERE email = ? AND is_active = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String storedHash = rs.getString("password_hash");
                    String storedSalt = rs.getString("password_salt");

                    if (PasswordUtil.verifyPassword(
                            password, storedHash, storedSalt)) {

                        System.out.println("\nLogin successful!");
                        
                        return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone")
                        );
                    }
                }

                System.out.println("\nInvalid email or password.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return null;
        }
    }
}

