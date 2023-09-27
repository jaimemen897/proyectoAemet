package models;

import lombok.Data;
import lombok.Getter;

@Data
public class CombinedPreciProv {
    private final String localidad;
    private final double precipitacion;

    public CombinedPreciProv(String localidad, double precipitacion) {
        this.localidad = localidad;
        this.precipitacion = precipitacion;
    }
}
