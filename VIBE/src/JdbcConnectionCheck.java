
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class JdbcConnectionCheck {

    public static void main(String[] args) {

        String password;

        Console console = System.console();

        if (console != null) {
            char[] chars = console.readPassword(
                "Enter MySQL root password: "
            );
            password = new String(chars);
            Arrays.fill(chars, '\0');
        } else {
            System.out.println(
                "Password input will be visible in this terminal."
            );
            try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter MySQL root password: ");
            password = scanner.nextLine();
        }

        String url = "jdbc:mysql://localhost:3306/";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection =
                     DriverManager.getConnection(
                         url, "root", password)) {

                DatabaseMetaData metadata =
                    connection.getMetaData();

                System.out.println();
                System.out.println(
                    "SUCCESS: Connected to MySQL Server!"
                );

                System.out.println(
                    "MySQL Version: " +
                    metadata.getDatabaseProductVersion()
                );

                System.out.println(
                    "Connected User: " +
                    metadata.getUserName()
                );
            }

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println();
            System.out.println("CONNECTION FAILED!");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
}