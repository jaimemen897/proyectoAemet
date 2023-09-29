package services;

import lombok.Getter;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * La clase `DB` representa una conexión a una base de datos y proporciona métodos para abrir y cerrar la conexión.
 * Utiliza un patrón Singleton para garantizar una única instancia de la conexión a la base de datos.
 */
@Getter
public class DB implements AutoCloseable {
    /**
     * Instancia única de la clase `DB`.
     */
    private static DB instance;

    /**
     * Ruta al archivo de propiedades de configuración de la base de datos.
     */
    private static final String propertiesPath = Paths.get("").toAbsolutePath() + File.separator + "resources" + File.separator + "database.properties";
    private static final String initPath = Paths.get("").toAbsolutePath() + File.separator + "resources" + File.separator + "init.sql";

    /**
     * La conexión a la base de datos.
     */
    private Connection connection;

    /**
     * Constructor privado de la clase `DB` utilizado para implementar el patrón Singleton.
     * Abre la conexión a la base de datos al crear una instancia de la clase.
     */
    private DB() {
        openConnection();
    }

    /**
     * Obtiene la instancia única de la clase `DB`.
     *
     * @return La instancia única de `DB`.
     */
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

    /**
     * Abre una conexión a la base de datos utilizando la configuración proporcionada en el archivo de propiedades.
     * También ejecuta un script de inicialización en la base de datos.
     */
    public void openConnection() {
        try {
            InputStream dbProps = ClassLoader.getSystemResourceAsStream("database.properties");
            Properties properties = new Properties();
            properties.load(dbProps);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String init = properties.getProperty("db.init");
            connection = DriverManager.getConnection(url, user, password);

            Reader reader = new BufferedReader(new FileReader(initPath));
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(reader);

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de propiedades: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión a la base de datos.
     *
     * @throws SQLException Si ocurre un error al cerrar la conexión.
     */
    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
