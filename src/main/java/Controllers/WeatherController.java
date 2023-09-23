package Controllers;

import lombok.Data;
import models.Weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class WeatherController {
    private static WeatherController instance;
    public List<Weather> weatherList = new ArrayList<>();

    public static WeatherController getInstance() {
        if (instance == null) {
            instance = new WeatherController();
        }
        return instance;
    }

    public void loadWeather() {
        for (int i = 29; i <= 31; i++) {
            /*Rutas con los nombres de archivos, el de origen y el nuevo al que vamos a cambiar la codificacion*/
            String ruta = "src" + File.separator + "data" + File.separator + "Aemet201710" + i + ".csv";
            String ruta2 = "src" + File.separator + "data" + File.separator + "DAemet201710" + i + ".csv";
            changeEncoding(ruta, ruta2);
            /*Leemos el fichero con BufferedReader línea a línea*/
            try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
                String line = br.readLine();
                while (line != null) {
                    /*Separamos en un array, creamos el Weather y lo metemos en la lista*/
                    String[] tiempo = line.split(";");
                    Weather weather = new Weather(tiempo[0], tiempo[1], Double.parseDouble(tiempo[2]), changeDate(tiempo[3], i),
                            Double.parseDouble(tiempo[4]), changeDate(tiempo[5], i), tiempo[6]);
                    weatherList.add(weather);
                    line = br.readLine();
                }
            } catch (Exception e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        }
    }

    public LocalDateTime changeDate(String fecha, int dia) {
        /*Por si nos dan una fecha como 1:20 en vez de 01:20*/
        if (fecha.length() != 5) {
            fecha = 0 + fecha;
        }
        fecha = "2017-10-" + dia + " " + fecha;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(fecha, formatter);
    }

    public void changeEncoding(String oldArchivo, String newArchivo) {
        try {
            /*Lee el contenido del archivo de origen para copiarlo en el nuevo con distinto codificador*/
            String contenido = Files.readString(Paths.get(oldArchivo), StandardCharsets.ISO_8859_1);
            Files.writeString(Paths.get(newArchivo), contenido, StandardCharsets.UTF_16);
        } catch (IOException e) {
            System.err.println("Error al cambiar la codificación del archivo." + e.getMessage());
        }
    }

    public void show() {
        for (Weather weather : weatherList) {
            System.out.println(weather);
        }
    }

    /*Donde se dio la temperatura máxima y mínima total en cada un ode los días*/
    public String maxTemp() {
        return weatherList.stream()
                .max(Comparator.comparing(Weather::getTempMax))
                .get().toString();
    }
}

































