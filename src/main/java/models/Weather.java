package models;

import com.google.gson.annotations.JsonAdapter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * La clase `Weather` representa datos meteorológicos para una localidad y provincia específicas.
 * Los datos incluyen la temperatura máxima y mínima, la hora de temperatura máxima y mínima,
 * la precipitación y la fecha del día.
 */
@Data
@Builder
@JsonAdapter(LocalDateAdapter.class)
public class Weather {
    /**
     * La localidad asociada a los datos meteorológicos.
     */
    private final String localidad;

    /**
     * La provincia asociada a los datos meteorológicos.
     */
    private final String provincia;

    /**
     * La temperatura máxima registrada.
     */
    private final Double tempMax;

    /**
     * La hora en la que se registró la temperatura máxima.
     */
    private final LocalDateTime horaTempMax;

    /**
     * La temperatura mínima registrada.
     */
    private final Double tempMin;

    /**
     * La hora en la que se registró la temperatura mínima.
     */
    private final LocalDateTime horaTempMin;

    /**
     * La cantidad de precipitación registrada.
     */
    private final double precipitacion;

    /**
     * La fecha del día al que corresponden los datos meteorológicos.
     */
    private final LocalDate day;

    /**
     * Crea una nueva instancia de la clase `Weather` con los datos meteorológicos especificados.
     *
     * @param localidad     La localidad asociada a los datos meteorológicos.
     * @param provincia     La provincia asociada a los datos meteorológicos.
     * @param tempMax       La temperatura máxima registrada.
     * @param horaTempMax   La hora en la que se registró la temperatura máxima.
     * @param tempMin       La temperatura mínima registrada.
     * @param horaTempMin   La hora en la que se registró la temperatura mínima.
     * @param precipitacion La cantidad de precipitación registrada.
     * @param day           La fecha del día al que corresponden los datos meteorológicos.
     */
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