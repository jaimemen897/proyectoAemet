
package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Getter;
import models.LocalDateAdapter;
import models.Weather;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * La clase `WeatherController` es responsable de controlar y gestionar los datos meteorológicos,
 * incluida la carga de datos desde archivos, búsqueda por provincia y exportación a formato JSON.
 * Esta clase implementa el patrón Singleton para garantizar una única instancia en toda la aplicación.
 *
 * @author Jaime Medina y Eva Gómez
 */
@Getter
@Data
public class WeatherController {
    /**
     * Instancia única de la clase `WeatherController`.
     */
    private static WeatherController instance;

    /**
     * Lista que almacena objetos `Weather` que representan datos meteorológicos.
     */
    private List<Weather> weatherList = new ArrayList<>();

    /**
     * Ruta al directorio de datos meteorológicos.
     */
    private final String dir = Paths.get("").toAbsolutePath() + File.separator + "data";

    /**
     * Obtiene la instancia única de la clase `WeatherController`.
     *
     * @return La instancia única de `WeatherController`.
     */
    public static WeatherController getInstance() {
        if (instance == null) {
            instance = new WeatherController();
        }
        return instance;
    }

    /**
     * Carga datos meteorológicos desde archivos y los almacena en la lista `weatherList`.
     */
    public void loadWeather() {
        try (Stream<Path> listPaths = Files.list(Paths.get(dir))) {
            listPaths.filter(a -> a.getFileName().toString().startsWith("Aemet"))
                    .forEach(a -> {
                        File newEncode = changeEncoding(a.toString(), dir + File.separator + "new" + a.getFileName());
                        int day = Integer.parseInt(newEncode.getName().substring(14, 16));
                        int month = Integer.parseInt(newEncode.getName().substring(12, 14));
                        int year = Integer.parseInt(newEncode.getName().substring(8, 12));

                        try (Stream<String> lines = Files.lines(Paths.get(newEncode.getPath()))) {
                            lines.forEach(line -> {
                                String[] tiempo = line.split(";");
                                LocalDate dia = LocalDate.of(year, month, day);

                                Weather weather = new Weather(tiempo[0], tiempo[1], Double.parseDouble(tiempo[2]), LocalTime.parse(verificateLenght(tiempo[3])),
                                        Double.parseDouble(tiempo[4]), LocalTime.parse(verificateLenght(tiempo[5])), Double.parseDouble(tiempo[6]), dia);
                                weatherList.add(weather);
                            });
                        } catch (IOException e) {
                            System.out.println("Error al leer el archivo: " + e.getMessage());
                        }
                    });

        } catch (IOException e) {
            System.out.println();
        }
    }

    /**
     * Verifica que la longitud de la fecha sea de 5 caracteres.
     *
     * @param tiempo Cadena a verificar.
     * @return La cadena con longitud 5.
     */
    private String verificateLenght(String tiempo) {
        if (tiempo.length() != 5) {
            tiempo = 0 + tiempo;
        }
        return tiempo;
    }

    /**
     * Cambia la codificación de un archivo y lo guarda en un nuevo archivo.
     *
     * @param oldArchivo Ruta del archivo original.
     * @param newArchivo Ruta del nuevo archivo con codificación diferente.
     * @return El nuevo archivo con la codificación cambiada.
     */
    public File changeEncoding(String oldArchivo, String newArchivo) {
        try {
            Files.deleteIfExists(Paths.get(newArchivo));
            String contenido = Files.readString(Paths.get(oldArchivo), Charset.forName("Windows-1252"));
            Files.writeString(Paths.get(newArchivo), contenido, StandardCharsets.UTF_8);
            return new File(newArchivo);
        } catch (IOException e) {
            System.err.println("Error al cambiar la codificación del archivo." + e.getMessage());
        }
        return null;
    }

    /**
     * Busca datos meteorológicos por nombre de provincia.
     *
     * @param name Nombre de la provincia a buscar.
     * @return Una lista de objetos `Weather` que coinciden con el nombre de la provincia.
     */
    public List<Weather> findByNombre(String name) {
        List<Weather> lista = new ArrayList<>();
        for (Weather weather : weatherList) {
            if (weather.getProvincia().equals(name)) {
                lista.add(weather);
            }
        }
        return lista;
    }

    /**
     * Exporta datos meteorológicos en formato JSON para una provincia específica.
     *
     * @param provincia Nombre de la provincia para la exportación.
     */
    public void exportJson(String provincia) {
        String rutaJson = dir + File.separator + "JsonAemet.json";
        File file = new File(rutaJson);

        try {
            Files.deleteIfExists(Path.of(file.getPath()));
        } catch (IOException e) {
            System.out.println("Error al crear el archivo JSON: " + e.getMessage());
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Weather.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        if (findByNombre(provincia) == null) {
            System.out.println("No se ha encontrado la provincia");
        } else {
            try (FileWriter writer = new FileWriter(rutaJson)) {
                gson.toJson(findByNombre(provincia), writer);
            } catch (IOException e) {
                System.out.println("Error al escribir el archivo JSON: " + e.getMessage());
            }
        }
    }
}