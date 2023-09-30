package models;

import com.google.gson.annotations.JsonAdapter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * La clase `Weather` representa datos meteorológicos para una localidad y provincia específicas.
 * Los datos incluyen la temperatura máxima y mínima, la hora de temperatura máxima y mínima,
 * la precipitación y la fecha del día.
 * @author Jaime Medina y Eva Gómez
 */
@Data
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
    private final LocalTime horaTempMax;

    /**
     * La temperatura mínima registrada.
     */
    private final Double tempMin;

    /**
     * La hora en la que se registró la temperatura mínima.
     */
    private final LocalTime horaTempMin;

    /**
     * La cantidad de precipitación registrada.
     */
    private final double precipitacion;

    /**
     * La fecha del día al que corresponden los datos meteorológicos.
     */
    private final LocalDate day;
}