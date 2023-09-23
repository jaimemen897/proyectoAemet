import Controllers.WeatherController;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        WeatherController controller = Controllers.WeatherController.getInstance();
        controller.loadWeather();
        controller.mostrarDatos();
    }
}
