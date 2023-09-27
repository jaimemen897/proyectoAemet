package models;

import lombok.Data;

@Data
public class CombinedProTemp<T,T1> {
    private final T item;
    private final T1 item2;


}
