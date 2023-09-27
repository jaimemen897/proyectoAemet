package services;

import models.CombinedPreciProv;
import models.CombinedProTemp;
import models.Crud;
import models.Weather;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.*;

public class WeatherManager implements Crud<Weather> {
    private static WeatherManager instance;
    private final Connection connection;

    private WeatherManager(Connection connection) {
        this.connection = connection;
    }

    public static WeatherManager getInstance(Connection connection) {
        if (instance == null) {
            instance = new WeatherManager(connection);
        }
        return instance;
    }

    /*CREATE*/
    public void save(Weather weather) {
        try {
            String sqlQuery = "INSERT INTO WEATHER (localidad, provincia, tempMax, horaTempMax, tempMin, horaTempMin, precipitacion, dia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, weather.getLocalidad());
            statement.setString(2, weather.getProvincia());
            statement.setDouble(3, weather.getTempMax());
            statement.setTimestamp(4, Timestamp.valueOf(weather.getHoraTempMax()));
            statement.setDouble(5, weather.getTempMin());
            statement.setTimestamp(6, Timestamp.valueOf(weather.getHoraTempMin()));
            statement.setDouble(7, weather.getPrecipitacion());
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
                        resultSet.getDouble("precipitacion"),
                        resultSet.getTimestamp("dia").toLocalDateTime().toLocalDate()));
            }
        } catch (SQLException e) {
            System.out.println("Error en findAll: " + e.getMessage());
        }
        return weathers;
    }

    public Optional<Weather> findByLocalidadAndProvincia(String localidad, String provincia) {
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
                        resultSet.getDouble("precipitacion"),
                        resultSet.getTimestamp("day").toLocalDateTime().toLocalDate());
            }
        } catch (SQLException e) {
            System.out.println("Error en findByLocalidadAndProvincia: " + e.getMessage());
        }
        return Optional.ofNullable(weather);
    }

    /*UPDATE*/
    public void update(Weather weather) {
        try {
            String sqlQuery = "UPDATE WEATHER SET tempMax = ?, horaTempMax = ?, tempMin = ?, horaTempMin = ?, precipitacion = ?, dia = ? WHERE localidad = ? AND provincia = ?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            statement.setDouble(1, weather.getTempMax());
            statement.setTimestamp(2, Timestamp.valueOf(weather.getHoraTempMax()));
            statement.setDouble(3, weather.getTempMin());
            statement.setTimestamp(4, Timestamp.valueOf(weather.getHoraTempMin()));
            statement.setDouble(5, weather.getPrecipitacion());
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
            String sqlQuery = "DELETE FROM WEATHER WHERE localidad = ? AND provincia = ? and dia = ?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, weather.getLocalidad());
            statement.setString(2, weather.getProvincia());
            statement.setTimestamp(3, Timestamp.valueOf(weather.getDay().atStartOfDay()));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public void deleteAll() {
        try {
            String sqlQuery = "DELETE FROM WEATHER";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public Map<LocalDate, Optional<String>> maxTemp() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getTempMax)), w -> w.map(Weather::getProvincia))));
    }


    public Map<LocalDate, Optional<String>> minTemp() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(minBy(comparingDouble(Weather::getTempMin)), w -> w.map(Weather::getProvincia))));
    }

    /*Máxima temperatura agrupada por provincias y día*/
    public Map<CombinedProTemp, Double> maxTempByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        collectingAndThen(maxBy(comparingDouble(Weather::getTempMax)), w -> w.map(Weather::getTempMax).orElse(0.0))));
    }

    /*Mínima temperatura agrupada por provincias y día*/
    public Map<CombinedProTemp, Double> minTempByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        collectingAndThen(minBy(comparingDouble(Weather::getTempMin)), w -> w.map(Weather::getTempMin).orElse(0.0))));
    }

    /*Medía de temperatura agrupada por provincias y día.*/
    public Map<CombinedProTemp, Double> getAverageTempByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        averagingDouble(Weather::getTempMax)));
    }

    /*Precipitación máxima por días y donde se dio*/
    public Map<LocalDate, Optional<String>> getMaxPrecipitation() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getPrecipitacion)), w -> w.map(Weather::getProvincia))));
    }

    /*Precipitación media por provincias y días*/
    public Map<CombinedProTemp, Double> getAveragePrecipitationByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        averagingDouble(Weather::getPrecipitacion)));
    }

    /*Lugares donde ha llovido agrupado por provincias y dia*/
    public Map<CombinedProTemp, List<Weather>> getPlacesWhereRained() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getPrecipitacion() > 0)
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()), toList()));
    }

    /*Temperatura máxima, mínima y dónde ha sido.*/
    public Map<LocalDate, Optional<String>> maxTempByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getTempMax)), w -> w.map(Weather::getLocalidad))));
    }

    public Map<LocalDate, Optional<String>> minTempByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(minBy(comparingDouble(Weather::getTempMin)), w -> w.map(Weather::getLocalidad))));
    }

    /*Temperatura media máxima*/
    public Map<LocalDate, Double> getAvgTempMaxByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay, averagingDouble(Weather::getTempMax)));
    }

    /*Temperatura media mínima*/
    public Map<LocalDate, Double> getAvgTempMinByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay, averagingDouble(Weather::getTempMin)));
    }

    /*Precipitación máxima y dónde ha sido*/
    public Map<LocalDate, CombinedPreciProv> maxPrecipitationByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getPrecipitacion)),
                                w -> w.map(weather -> new CombinedPreciProv(weather.getLocalidad(), weather.getPrecipitacion())).orElse(null))));
    }

    /*Precipitación media*/
    public Map<LocalDate, Double> getAvgPrecipitationByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay, averagingDouble(Weather::getPrecipitacion)));
    }

}















