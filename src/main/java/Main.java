import controllers.WeatherController;
import models.CombinedProTemp;
import models.Weather;
import services.DB;
import services.WeatherManager;

import java.time.LocalDate;
import java.util.List;
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


        System.out.println("Temperatura maxima en cada uno de los dias");
        Map<LocalDate, Optional<String>> mapa1 = weatherManager.maxTemp();
        for (Map.Entry<LocalDate, Optional<String>> entry : mapa1.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura minima en cada uno de los dias");
        Map<LocalDate, Optional<String>> mapa2 = weatherManager.minTemp();
        for (Map.Entry<LocalDate, Optional<String>> entry : mapa2.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura maxima agrupado por provincias y dia");
        Map<CombinedProTemp, Double> mapa3 = weatherManager.maxTempByGroup();
        for (Map.Entry<CombinedProTemp, Double> entry : mapa3.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura minima agrupado por provincias y dia");
        Map<CombinedProTemp, Double> mapa4 = weatherManager.minTempByGroup();
        for (Map.Entry<CombinedProTemp, Double> entry : mapa4.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura media agrupado por provincias y dia");
        Map<CombinedProTemp, Double> mapa5 = weatherManager.getAverageTempByGroup();
        for (Map.Entry<CombinedProTemp, Double> entry : mapa5.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Precipitacion maxima por dias y donde se dio");
        Map<LocalDate, Optional<String>> mapa6 = weatherManager.getMaxPrecipitation();
        for (Map.Entry<LocalDate, Optional<String>> entry : mapa6.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Precipitacion media por provincias y dias");
        Map<CombinedProTemp, Double> mapa7 = weatherManager.getAveragePrecipitationByGroup();
        for (Map.Entry<CombinedProTemp, Double> entry : mapa7.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Lugares donde ha llovido agrupado por provincias y dia");
        Map<CombinedProTemp, List<Weather>> mapa8 = weatherManager.getPlacesWhereRained();
        for (Map.Entry<CombinedProTemp, List<Weather>> entry : mapa8.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Lugares donde mas ha llovido");
        System.out.println(weatherManager.placedMaxRained());
        System.out.println("-----------------------------------------------");


        System.out.println("Temperatura maxima y donde ha sido");
        Map<LocalDate, Optional<String>> mapa9 = weatherManager.maxTempByProvincia("Madrid");
        for (Map.Entry<LocalDate, Optional<String>> entry : mapa9.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura minima y donde ha sido");
        Map<LocalDate, Optional<String>> mapa10 = weatherManager.minTempByProvincia("Madrid");
        for (Map.Entry<LocalDate, Optional<String>> entry : mapa10.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura media maxima");
        Map<LocalDate, Double> mapa11 = weatherManager.getAvgTempMaxByProvincia("Madrid");
        for (Map.Entry<LocalDate, Double> entry : mapa11.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Temperatura media minima");
        Map<LocalDate, Double> mapa12 = weatherManager.getAvgTempMinByProvincia("Madrid");
        for (Map.Entry<LocalDate, Double> entry : mapa12.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Precipitacion maxima y donde ha sido");
        Map<LocalDate, CombinedProTemp> mapa13 = weatherManager.maxPrecipitationByProvincia("Madrid");
        for (Map.Entry<LocalDate, CombinedProTemp> entry : mapa13.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Precipitacion media");
        Map<LocalDate, Double> mapa14 = weatherManager.getAvgPrecipitationByProvincia("Madrid");
        for (Map.Entry<LocalDate, Double> entry : mapa14.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}