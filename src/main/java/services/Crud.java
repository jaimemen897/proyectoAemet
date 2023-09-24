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
                    " tempMax DOUBLE, horaTempMax TIMESTAMP, tempMin DOUBLE, horaTempMin TIMESTAMP, precipitacion VARCHAR(50), dia TIMESTAMP)");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    /*CREATE*/
    public void create(Weather weather) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO WEATHER (localidad, provincia, " +
                    "tempMax, horaTempMax, tempMin, horaTempMin, precipitacion, dia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, weather.getLocalidad());
            statement.setString(2, weather.getProvincia());
            statement.setDouble(3, weather.getTempMax());
            statement.setTimestamp(4, Timestamp.valueOf(weather.getHoraTempMax()));
            statement.setDouble(5, weather.getTempMin());
            statement.setTimestamp(6, Timestamp.valueOf(weather.getHoraTempMin()));
            statement.setString(7, weather.getPrecipitacion());
            statement.setTimestamp(8, Timestamp.valueOf(weather.getDay().atStartOfDay()));
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
                        resultSet.getString("precipitacion"),
                        resultSet.getTimestamp("day").toLocalDateTime().toLocalDate()));
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
                        resultSet.getString("precipitacion"),
                        resultSet.getTimestamp("day").toLocalDateTime().toLocalDate());
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
                    " tempMin = ?, horaTempMin = ?, precipitacion = ?, dia = ? WHERE localidad = ? AND provincia = ?");
            statement.setDouble(1, weather.getTempMax());
            statement.setTimestamp(2, Timestamp.valueOf(weather.getHoraTempMax()));
            statement.setDouble(3, weather.getTempMin());
            statement.setTimestamp(4, Timestamp.valueOf(weather.getHoraTempMin()));
            statement.setString(5, weather.getPrecipitacion());
            statement.setString(6, weather.getLocalidad());
            statement.setString(7, weather.getProvincia());
            statement.setTimestamp(8, Timestamp.valueOf(weather.getDay().atStartOfDay()));
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

    /*Dónde se dio la temperatura máxima y mínima total en cada uno de los días*/
    public List<String> maxTemp() {
        List<String> localidades = new ArrayList<>();
        for (int i = 29; i <= 31; i++) {
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT localidad FROM WEATHER " +
                        "WHERE tempMax = (SELECT MAX(tempMax) FROM WEATHER WHERE EXTRACT(DAY FROM dia) = " + i + ") " +
                        "AND EXTRACT(DAY FROM dia) = " + i + " ");
                if (resultSet.next()) {
                    localidades.add(resultSet.getString("LOCALIDAD"));
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar la temperatura máxima: " + e.getMessage());
            }
        }
        return localidades;
    }

    public List<String> minTemp() {
        List<String> localidades = new ArrayList<>();
        for (int i = 29; i <= 31; i++) {
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT localidad FROM WEATHER " +
                        "WHERE tempMin = (SELECT min(tempMin) FROM WEATHER WHERE EXTRACT(DAY FROM dia) = " + i + ") " +
                        "AND EXTRACT(DAY FROM dia) = " + i + " ");
                if (resultSet.next()) {
                    localidades.add(resultSet.getString("LOCALIDAD"));
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar la temperatura mínima: " + e.getMessage());
            }
        }
        return localidades;
    }

    /*Máxima temperatura agrupado por provincias y día*/
    public List<String> maxTempByProvincia() {
        List<String> provincias = new ArrayList<>();
        for (int i = 29; i <= 31; i++) {
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT provincia, MAX(tempMax) AS tempMax FROM WEATHER " +
                        "WHERE EXTRACT(DAY FROM dia) = " + i + " GROUP BY provincia, dia");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("provincia") + " - " + resultSet.getDouble("tempMax") + "Cº  - " + i);
                    provincias.add(resultSet.getString("provincia") + " - " + resultSet.getDouble("tempMax"));
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar la temperatura máxima: " + e.getMessage());
            }
        }
        return provincias;
    }

    /*Mínima temperatura agrupado por provincias y día*/
    public List<String> minTempByProvincia() {
        List<String> provincias = new ArrayList<>();
        for (int i = 29; i <= 31; i++) {
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT provincia, MIN(tempMin) AS tempMin FROM WEATHER " +
                        "WHERE EXTRACT(DAY FROM dia) = " + i + " GROUP BY provincia, dia");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("provincia") + " - " + resultSet.getDouble("tempMin") + "Cº  - " + i);
                    provincias.add(resultSet.getString("provincia") + " - " + resultSet.getDouble("tempMin"));
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar la temperatura mínima: " + e.getMessage());
            }
        }
        return provincias;
    }

}