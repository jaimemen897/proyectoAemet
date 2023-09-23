import Controllers.WeatherController;

public class Main {
    public static void main(String[] args) {
        WeatherController controller = WeatherController.getInstance();
        /*controller.changeEncoding();*/
        controller.loadWeather();
        controller.show();

    }
}
