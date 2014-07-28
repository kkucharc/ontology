package wedt.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * Date: 31.05.14
 * Time: 16:33
 */
public class ConnectionUtils {

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:./data", "sa", "");
        return connection;
    }
}
