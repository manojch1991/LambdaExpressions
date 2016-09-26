package DatabaseConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {

    private static final String driver = "com.mysql.jdbc.Driver";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
        } catch (Exception e) {
            System.out.println("driver is not loaded: " + e);
        }
        String user = "root";
        String password = "root";
        String connStr = "jdbc:mysql://localhost:3306/individual";
        try {
            connection = DriverManager.getConnection(connStr, user, password);

        } catch (SQLException e) {
            System.out.println("Problem in database connectivity.\n");
            e.printStackTrace();
        }
        return connection;
    }

}
