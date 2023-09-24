import Controllers.WeatherController;
import models.Weather;
import services.DB;
import services.Crud;

public class Main {
    public static void main(String[] args) {
        DB db = DB.getInstance();
        db.openConnection();
        Crud crud = Crud.getInstance(db.getConnection());
        WeatherController weatherController = WeatherController.getInstance();
        weatherController.loadWeather();
        crud.createTable();

        for (Weather weather : weatherController.getWeatherList()) {
            crud.create(weather);
        }
    }
}
