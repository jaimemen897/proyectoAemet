import Controllers.WeatherController;
import models.Weather;
import services.DB;
import services.WeatherManager;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();

        WeatherManager weatherManager = WeatherManager.getInstance(db.getConnection());
        WeatherController weatherController = WeatherController.getInstance();

        weatherController.loadWeather();

        for (Weather weather : weatherController.getWeatherList()) {
            weatherManager.save(weather);
        }

        weatherManager.findAll().forEach(System.out::println);
    }
}
