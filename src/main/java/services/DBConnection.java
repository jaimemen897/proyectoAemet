package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection connectDB(DBManager dbManager) throws SQLException {
        String url = dbManager.getProperty("db.url");
        String user = dbManager.getProperty("db.user");
        String password = dbManager.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}
