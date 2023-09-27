import controllers.WeatherController;
import models.CombinedPreciProv;
import models.Weather;
import services.DB;
import services.WeatherManager;

import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();

        WeatherManager weatherManager = WeatherManager.getInstance(db.getConnection());
        WeatherController weatherController = WeatherController.getInstance();

        weatherController.loadWeather();

        for (Weather weather : weatherController.getWeatherList()) {
            weatherManager.save(weather);
        }

        Map<LocalDate, CombinedPreciProv> mapa = weatherManager.maxPrecipitationByProvincia("Asturias");

        //Muestra el mapa
        for (Map.Entry<LocalDate, CombinedPreciProv> entry : mapa.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
