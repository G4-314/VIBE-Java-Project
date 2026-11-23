
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/vibe";

    private static final String USER = "root";

    private static String password;

    public static void setPassword(String dbPassword) {
        password = dbPassword;
    }

    public static Connection getConnection()
            throws SQLException {

        if (password == null) {
            throw new SQLException(
                "Database password has not been set."
            );
        }

        return DriverManager.getConnection(
            URL, USER, password
        );
    }

    public static void main(String[] args) {

       try (Scanner sc = new Scanner(System.in)) {

        System.out.print("Enter MySQL password: ");
        String enteredPassword = sc.nextLine();

        setPassword(enteredPassword);

        try (Connection con = getConnection()) {
            if(con != null){
            System.out.println(
                "Connected to VIBE database successfully!"
            );
        }
        } catch (SQLException e) {
            System.out.println(
                "Connection failed: " + e.getMessage()
            );
        }
    }
}
}


