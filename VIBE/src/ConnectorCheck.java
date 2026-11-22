public class ConnectorCheck {

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println(
                "SUCCESS: MySQL Connector/J is loaded!"
            );

        } catch (ClassNotFoundException e) {

            System.out.println(
                "FAILED: MySQL Connector/J was not found."
            );

            e.printStackTrace();
        }
    }
}