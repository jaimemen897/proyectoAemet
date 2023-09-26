package models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class Weather {
    private final String localidad;
    private final String provincia;
    private final Double tempMax;
    private final LocalDateTime horaTempMax;
    private final Double tempMin;
    private final LocalDateTime horaTempMin;
    private final double precipitacion;
    private final LocalDate day;

    public Weather(String localidad, String provincia, Double tempMax, LocalDateTime horaTempMax, Double tempMin, LocalDateTime horaTempMin, double precipitacion, LocalDate day) {
        this.localidad = localidad;
        this.provincia = provincia;
        this.tempMax = tempMax;
        this.horaTempMax = horaTempMax;
        this.tempMin = tempMin;
        this.horaTempMin = horaTempMin;
        this.precipitacion = precipitacion;
        this.day = day;
    }
}