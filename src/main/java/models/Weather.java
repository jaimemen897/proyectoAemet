package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Weather {
    private final String localidad;
    private final String provincia;
    private final Double tempMax;
    private final LocalDateTime horaTempMax;
    private final Double tempMin;
    private final LocalDateTime horaTempMin;
    private final String precipitacion;
    private final LocalDate day;

    public Weather(String localidad, String provincia, Double tempMax, LocalDateTime horaTempMax, Double tempMin, LocalDateTime horaTempMin, String precipitacion, LocalDate day) {
        this.localidad = localidad;
        this.provincia = provincia;
        this.tempMax = tempMax;
        this.horaTempMax = horaTempMax;
        this.tempMin = tempMin;
        this.horaTempMin = horaTempMin;
        this.precipitacion = precipitacion;
        this.day = day;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", horaTempMax=" + horaTempMax +
                ", tempMin='" + tempMin + '\'' +
                ", horaTempMin=" + horaTempMin +
                ", precipitacion='" + precipitacion + '\'' +
                '}';
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public LocalDateTime getHoraTempMax() {
        return horaTempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public LocalDateTime getHoraTempMin() {
        return horaTempMin;
    }

    public String getPrecipitacion() {
        return precipitacion;
    }

    public LocalDate getDay() {
        return day;
    }
}
