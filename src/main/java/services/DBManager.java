package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBManager {
    private final Properties properties;

    public DBManager(String archivo){
        properties = new Properties();
        try {
            properties.load(new FileInputStream(archivo));
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de propiedades: " + e.getMessage());
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
