package services;

import models.Weather;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crud {
    private static Crud instance;
    private final Connection connection;

    private Crud(Connection connection) {
        this.connection = connection;
    }

    public static Crud getInstance(Connection connection) {
        if (instance == null) {
            instance = new Crud(connection);
        }
        return instance;
    }

    public void createTable() {
        try {
            connection.createStatement().executeUpdate("CREATE TABLE WEATHER (localidad VARCHAR(50), provincia VARCHAR(50)," +
                    " tempMax DOUBLE, horaTempMax TIMESTAMP, tempMin DOUBLE, horaTempMin TIMESTAMP, precipitacion VARCHAR(50))");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    /*CREATE*/
    public void create(Weather weather) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO WEATHER (localidad, provincia, " +
                    "tempMax, horaTempMax, tempMin, horaTempMin, precipitacion) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, weather.getLocalidad());
            statement.setString(2, weather.getProvincia());
            statement.setDouble(3, weather.getTempMax());
            statement.setTimestamp(4, Timestamp.valueOf(weather.getHoraTempMax()));
            statement.setDouble(5, weather.getTempMin());
            statement.setTimestamp(6, Timestamp.valueOf(weather.getHoraTempMin()));
            statement.setString(7, weather.getPrecipitacion());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    /*READ*/
    public List<Weather> findAll() {
        List<Weather> weathers = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM WEATHER");
            while (resultSet.next()) {
                weathers.add(new Weather(
                        resultSet.getString("localidad"),
                        resultSet.getString("provincia"),
                        resultSet.getDouble("tempMax"),
                        resultSet.getTimestamp("horaTempMax").toLocalDateTime(),
                        resultSet.getDouble("tempMin"),
                        resultSet.getTimestamp("horaTempMin").toLocalDateTime(),
                        resultSet.getString("precipitacion")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error en findAll: " + e.getMessage());
        }
        return weathers;
    }

    public Weather findByLocalidadAndProvincia(String localidad, String provincia) {
        Weather weather = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM WEATHER WHERE localidad = ? AND provincia = ?");
            statement.setString(1, localidad);
            statement.setString(2, provincia);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                weather = new Weather(
                        resultSet.getString("localidad"),
                        resultSet.getString("provincia"),
                        resultSet.getDouble("tempMax"),
                        resultSet.getTimestamp("horaTempMax").toLocalDateTime(),
                        resultSet.getDouble("tempMin"),
                        resultSet.getTimestamp("horaTempMin").toLocalDateTime(),
                        resultSet.getString("precipitacion")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error en findByLocalidadAndProvincia: " + e.getMessage());
        }
        return weather;
    }

    /*UPDATE*/
    public void update(Weather weather) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE WEATHER SET tempMax = ?, horaTempMax = ?," +
                    " tempMin = ?, horaTempMin = ?, precipitacion = ? WHERE localidad = ? AND provincia = ?");
            statement.setDouble(1, weather.getTempMax());
            statement.setTimestamp(2, Timestamp.valueOf(weather.getHoraTempMax()));
            statement.setDouble(3, weather.getTempMin());
            statement.setTimestamp(4, Timestamp.valueOf(weather.getHoraTempMin()));
            statement.setString(5, weather.getPrecipitacion());
            statement.setString(6, weather.getLocalidad());
            statement.setString(7, weather.getProvincia());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    /*DELETE*/
    public void delete(Weather weather) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM WEATHER WHERE localidad = ? AND provincia = ?");
            statement.setString(1, weather.getLocalidad());
            statement.setString(2, weather.getProvincia());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }




}
