package Controllers;

import lombok.Getter;

@Getter
public class Weather {
    private final String localidad;
    private final String provincia;
    private final String tempMax;
    private final String horaTempMax;
    private final String tempMin;
    private final String horaTempMin;
    private final String precipitacion;

    public Weather(String localidad, String provincia, String tempMax, String horaTempMax, String tempMin, String horaTempMin, String precipitacion) {
        this.localidad = localidad;
        this.provincia = provincia;
        this.tempMax = tempMax;
        this.horaTempMax = horaTempMax;
        this.tempMin = tempMin;
        this.horaTempMin = horaTempMin;
        this.precipitacion = precipitacion;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", horaTempMax='" + horaTempMax + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", horaTempMin='" + horaTempMin + '\'' +
                ", precipitacion='" + precipitacion + '\'' +
                '}';
    }
}
