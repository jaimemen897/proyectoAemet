package models;

import lombok.Data;

/**
 * La clase `CombinedProTemp` representa una estructura que combina dos objetos de tipos diferentes.
 * Esta clase es inmutable, lo que significa que una vez que se crean los objetos, no se pueden modificar.
 *
 * @param <T>  El tipo del primer objeto.
 * @param <T1> El tipo del segundo objeto.
 */
@Data
public class CombinedProTemp<T,T1> {
    private final T item;
    private final T1 item2;
}
