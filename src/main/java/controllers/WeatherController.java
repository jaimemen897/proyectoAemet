package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Getter;
import models.LocalDateAdapter;
import models.Weather;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Data
public class WeatherController {
    private static WeatherController instance;
    private List<Weather> weatherList = new ArrayList<>();

    public static WeatherController getInstance() {
        if (instance == null) {
            instance = new WeatherController();
        }
        return instance;
    }

    public void loadWeather() {
        try (Stream<Path> listPaths = Files.list(Paths.get("src" + File.separator + "data"))) {
            listPaths.filter(a -> a.getFileName().toString().startsWith("Aemet"))
                    .forEach(a -> {
                        File newEncode = changeEncoding(a.toString(), "src" + File.separator + "data" + File.separator + "new" + a.getFileName());

                        try (BufferedReader br = new BufferedReader(new FileReader(newEncode))) {
                            String line = br.readLine();

                            while (line != null) {
                                int day = Integer.parseInt(newEncode.toString().substring(23, 25));
                                int month = Integer.parseInt(newEncode.toString().substring(21, 23));
                                int year = Integer.parseInt(newEncode.toString().substring(17, 21));
                                String[] tiempo = line.split(";");
                                LocalDate dia = LocalDate.of(year, month, day);

                                Weather weather = new Weather(tiempo[0], tiempo[1], Double.parseDouble(tiempo[2]), changeDate(tiempo[3], day),
                                        Double.parseDouble(tiempo[4]), changeDate(tiempo[5], day), Double.parseDouble(tiempo[6]), dia);
                                weatherList.add(weather);
                                line = br.readLine();
                            }
                        } catch (Exception e) {
                            System.err.println("Error al leer el archivo: " + e.getMessage());
                        }
                    });

        } catch (IOException e) {
            System.out.println();
        }
    }

    public File changeEncoding(String oldArchivo, String newArchivo) {
        try {
            Files.deleteIfExists(Paths.get(newArchivo));
            /*Lee el contenido del archivo de origen para copiarlo en el nuevo con distinto codificador*/
            String contenido = Files.readString(Paths.get(oldArchivo), Charset.forName("Windows-1252"));
            Files.writeString(Paths.get(newArchivo), contenido, StandardCharsets.UTF_8);
            return new File(newArchivo);
        } catch (IOException e) {
            System.err.println("Error al cambiar la codificación del archivo." + e.getMessage());
        }
        return null;
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

    public List<Weather> findByNombre(String nombre) {
        List<Weather> lista = new ArrayList<>();
        for (Weather weather : weatherList) {
            if (weather.getProvincia().equals(nombre)) {
                lista.add(weather);
            }
        }
        return lista;
    }

    public void exportJson(String provincia) {
        String rutaDelArchivo = "src" + File.separator + "data" + File.separator + "Aemet.json";
        /*si el fichero "rutaDelArchivo" eliminarlo y volverlo a crear*/
        try {
            Files.deleteIfExists(Paths.get(rutaDelArchivo));
        } catch (IOException e) {
            System.out.println("Error al crear el archivo JSON: " + e.getMessage());
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Weather.class, new LocalDateAdapter());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (findByNombre(provincia) == null) {
            System.out.println("No se ha encontrado la provincia");
        } else {
            try (FileWriter writer = new FileWriter(rutaDelArchivo)) {
                gson.toJson(findByNombre(provincia), writer);
            } catch (IOException e) {
                System.out.println("Error al escribir el archivo JSON: " + e.getMessage());
            }
        }
    }
}

































