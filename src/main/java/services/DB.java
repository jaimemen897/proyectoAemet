package services;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DB {
    private static DB instance;
    private static final String propertiesPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "database.properties";
    private Connection connection;

    public static DB getInstance() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        }
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    public void openConnection() {
        try {
            DBManager dbManager = new DBManager(propertiesPath);
            connection = DBConnection.connectDB(dbManager);
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la base de datos: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
