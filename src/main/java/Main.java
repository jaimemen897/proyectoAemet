import Controllers.WeatherController;
import models.CombinedProTemp;
import models.Weather;
import services.DB;
import services.WeatherManager;

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

        Map<CombinedProTemp, Double> mapa = weatherManager.getPlacesWhereRained();

        //Muestra el mapa
        for (Map.Entry<CombinedProTemp, Double> entry : mapa.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        /*System.out.println(weatherManager.getAvgTempMaxByProvincia("Madrid"));*/
    }
}
