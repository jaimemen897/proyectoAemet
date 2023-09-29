import controllers.WeatherController;
import models.CombinedProTemp;
import models.Weather;
import services.DB;
import services.WeatherManager;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();

        WeatherManager weatherManager = WeatherManager.getInstance(db.getConnection());
        WeatherController weatherController = WeatherController.getInstance();

        weatherController.loadWeather();

        for (Weather weather : weatherController.getWeatherList()) {
            weatherManager.save(weather);
        }

        weatherController.exportJson("Madrid");

        /*Optional<Map.Entry<LocalDate, Double>> mapa = weatherManager.placedMaxRained();

        //Muestra el mapa
        for (Map.Entry<LocalDate, CombinedProTemp> entry : mapa.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/

        System.out.println(weatherManager.placedMaxRained());


    }
}
