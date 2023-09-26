package models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CombinedProTemp {
    private final String provincia;
    private final LocalDate date;

    public CombinedProTemp(String provincia, LocalDate date) {
        this.provincia = provincia;
        this.date = date;
    }
}
