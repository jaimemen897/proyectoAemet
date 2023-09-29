package services;

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


/**
 * La clase `WeatherManager` implementa la interfaz `Crud` y se encarga de gestionar los datos meteorológicos
 * almacenados en una base de datos. Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * en la tabla de datos meteorológicos.
 */
public class WeatherManager implements Crud<Weather> {
    /**
     * Instancia única de la clase `WeatherManager`.
     */
    private static WeatherManager instance;

    /**
     * La conexión a la base de datos utilizada para gestionar los datos meteorológicos.
     */
    private final Connection connection;

    /**
     * Constructor privado de la clase `WeatherManager` utilizado para implementar el patrón Singleton.
     *
     * @param connection La conexión a la base de datos.
     */
    private WeatherManager(Connection connection) {
        this.connection = connection;
    }

    /**
     * Obtiene la instancia única de la clase `WeatherManager`. Si no existe, crea una nueva instancia.
     *
     * @param connection La conexión a la base de datos.
     * @return La instancia única de `WeatherManager`.
     */
    public static WeatherManager getInstance(Connection connection) {
        if (instance == null) {
            instance = new WeatherManager(connection);
        }
        return instance;
    }

    /**
     * Guarda los datos meteorológicos en la base de datos.
     *
     * @param weather Los datos meteorológicos a guardar.
     */
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

    /**
     * Obtiene una lista de todos los datos meteorológicos almacenados en la base de datos.
     *
     * @return Una lista de objetos `Weather` que representan datos meteorológicos.
     */
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

    /**
     * Busca datos meteorológicos por localidad y provincia en la base de datos.
     *
     * @param localidad La localidad de los datos meteorológicos a buscar.
     * @param provincia La provincia de los datos meteorológicos a buscar.
     * @return Un objeto `Optional` que contiene los datos meteorológicos si se encuentran, o un valor vacío si no se encuentran.
     */
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

    /**
     * Actualiza los datos meteorológicos en la base de datos.
     *
     * @param weather Los nuevos datos meteorológicos.
     */
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

    /**
     * Elimina los datos meteorológicos de la base de datos.
     *
     * @param weather Los datos meteorológicos a eliminar.
     */
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

    /**
     * Elimina todos los datos meteorológicos de la base de datos.
     */
    public void deleteAll() {
        try {
            String sqlQuery = "DELETE FROM WEATHER";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    /**
     * Obtiene la temperatura máxima registrada agrupada por fecha y provincia.
     *
     * @return Un mapa que contiene la temperatura máxima por fecha y provincia.
     */
    public Map<LocalDate, Optional<String>> maxTemp() {
        List<Weather> weathers = findAll();
        return weathers.stream().collect(groupingBy(Weather::getDay,
                collectingAndThen(maxBy(comparingDouble(Weather::getTempMax)), w -> w.map(Weather::getProvincia))));
    }

    /**
     * Obtiene la temperatura mínima registrada agrupada por fecha y provincia.
     *
     * @return Un mapa que contiene la temperatura mínima por fecha y provincia.
     */
    public Map<LocalDate, Optional<String>> minTemp() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(minBy(comparingDouble(Weather::getTempMin)), w -> w.map(Weather::getProvincia))));
    }

    /**
     * Obtiene la temperatura máxima registrada agrupada por provincia y fecha.
     *
     * @return Un mapa que contiene la temperatura máxima por provincia y fecha.
     */
    public Map<CombinedProTemp, Double> maxTempByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        collectingAndThen(maxBy(comparingDouble(Weather::getTempMax)), w -> w.map(Weather::getTempMax).orElse(0.0))));
    }

    /**
     * Obtiene la temperatura mínima registrada agrupada por provincia y fecha.
     *
     * @return Un mapa que contiene la temperatura mínima por provincia y fecha.
     */
    public Map<CombinedProTemp, Double> minTempByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        collectingAndThen(minBy(comparingDouble(Weather::getTempMin)), w -> w.map(Weather::getTempMin).orElse(0.0))));
    }

    /**
     * Obtiene la temperatura media agrupada por provincia y fecha.
     *
     * @return Un mapa que contiene la temperatura media por provincia y fecha.
     */
    public Map<CombinedProTemp, Double> getAverageTempByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        averagingDouble(Weather::getTempMax)));
    }

    /**
     * Obtiene la fecha y provincia con la precipitación máxima registrada.
     *
     * @return Un mapa que contiene la fecha y provincia con la precipitación máxima.
     */
    public Map<LocalDate, Optional<String>> getMaxPrecipitation() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getPrecipitacion)), w -> w.map(Weather::getProvincia))));
    }

    /**
     * Obtiene la precipitación media agrupada por provincia y fecha.
     *
     * @return Un mapa que contiene la precipitación media por provincia y fecha.
     */
    public Map<CombinedProTemp, Double> getAveragePrecipitationByGroup() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()),
                        averagingDouble(Weather::getPrecipitacion)));
    }

    /**
     * Obtiene los lugares donde ha llovido agrupados por provincia y fecha.
     *
     * @return Un mapa que contiene los lugares donde ha llovido por provincia y fecha.
     */
    public Map<CombinedProTemp, List<Weather>> getPlacesWhereRained() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getPrecipitacion() > 0)
                .collect(groupingBy(combined -> new CombinedProTemp(combined.getProvincia(), combined.getDay()), toList()));
    }

    /**
     * Obtiene el lugar donde ha llovido más.
     *
     * @return El lugar donde ha llovido más.
     */
    public String placedMaxRained() {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getPrecipitacion() > 0)
                .collect(collectingAndThen(maxBy(comparingDouble(Weather::getPrecipitacion)),
                        w -> w.map(weather -> weather.getLocalidad() + " - " + weather.getProvincia()).orElse(null)));
    }

    /**
     * Obtiene la temperatura máxima agrupada por provincia y fecha para una provincia específica.
     *
     * @param provincia La provincia para la cual se desea obtener la temperatura máxima.
     * @return Un mapa que contiene la temperatura máxima por fecha para la provincia especificada.
     */
    public Map<LocalDate, Optional<String>> maxTempByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getTempMax)), w -> w.map(Weather::getLocalidad))));
    }

    /**
     * Obtiene la temperatura mínima agrupada por provincia y fecha para una provincia específica.
     *
     * @param provincia La provincia para la cual se desea obtener la temperatura mínima.
     * @return Un mapa que contiene la temperatura mínima por fecha para la provincia especificada.
     */
    public Map<LocalDate, Optional<String>> minTempByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(minBy(comparingDouble(Weather::getTempMin)), w -> w.map(Weather::getLocalidad))));
    }

    /**
     * Obtiene la temperatura media máxima para una provincia específica.
     *
     * @param provincia La provincia para la cual se desea obtener la temperatura media máxima.
     * @return Un mapa que contiene la temperatura media máxima por fecha para la provincia especificada.
     */
    public Map<LocalDate, Double> getAvgTempMaxByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay, averagingDouble(Weather::getTempMax)));
    }

    /**
     * Obtiene la temperatura media mínima para una provincia específica.
     *
     * @param provincia La provincia para la cual se desea obtener la temperatura media mínima.
     * @return Un mapa que contiene la temperatura media mínima por fecha para la provincia especificada.
     */
    public Map<LocalDate, Double> getAvgTempMinByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay, averagingDouble(Weather::getTempMin)));
    }

    /**
     * Obtiene la precipitación máxima y su ubicación para una provincia específica.
     *
     * @param provincia La provincia para la cual se desea obtener la precipitación máxima y su ubicación.
     * @return Un mapa que contiene la precipitación máxima y su ubicación por fecha para la provincia especificada.
     */
    public Map<LocalDate, CombinedProTemp> maxPrecipitationByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay,
                        collectingAndThen(maxBy(comparingDouble(Weather::getPrecipitacion)),
                                w -> w.map(weather -> new CombinedProTemp(weather.getLocalidad(), weather.getPrecipitacion())).orElse(null))));
    }

    /**
     * Obtiene la precipitación media para una provincia específica.
     *
     * @param provincia La provincia para la cual se desea obtener la precipitación media.
     * @return Un mapa que contiene la precipitación media por fecha para la provincia especificada.
     */
    public Map<LocalDate, Double> getAvgPrecipitationByProvincia(String provincia) {
        List<Weather> weathers = findAll();
        return weathers.stream()
                .filter(weather -> weather.getProvincia().equals(provincia))
                .collect(groupingBy(Weather::getDay, averagingDouble(Weather::getPrecipitacion)));
    }


}















