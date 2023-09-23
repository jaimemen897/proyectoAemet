package Controllers;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Data
public class WeatherController {
    private static Controllers.WeatherController instance;
    public List<Weather> weatherList = new ArrayList<>();
    private final String ruta = "src" + File.separator + "data" + File.separator + "Aemet20171029.csv";

    public static Controllers.WeatherController getInstance() {
        if (instance == null) {
            instance = new Controllers.WeatherController();
        }
        return instance;
    }

    public void loadWeather() {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line = br.readLine();
            while (line != null) {
                String[] tiempo = line.split(";");
                Weather weather = new Weather(tiempo[0], tiempo[1], tiempo[2], tiempo[3], tiempo[4], tiempo[5], tiempo[6]);
                weatherList.add(weather);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("No se encontro archivo");
        }
    }

    public void changeEncoding() {
        String newArchivo = "src" + File.separator + "data" + File.separator + "Aemet20171029.csv";
        try {
            String contenido = Files.readString(Paths.get(ruta), StandardCharsets.ISO_8859_1);
            Files.writeString(Paths.get(newArchivo), contenido, StandardCharsets.UTF_16);
            System.out.println("Cambio de codificación exitoso.");
        } catch (IOException e) {
            System.err.println("Error al cambiar la codificación del archivo." + e.getMessage());
        }
    }

    public void mostrarDatos() {
        for (Weather weather : weatherList) {
            System.out.println(weather);
        }
    }
}

































