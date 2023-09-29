package models;

import java.util.List;
import java.util.Optional;

/**
 * La interfaz `Crud` define operaciones CRUD (Crear, Leer, Actualizar y Eliminar) básicas para entidades de un tipo genérico T.
 *
 * @param <T> El tipo genérico que representa la entidad con la que se realizarán las operaciones CRUD.
 */
public interface Crud<T> {

    /**
     * Guarda una entidad de tipo T.
     *
     * @param t La entidad de tipo T que se va a guardar.
     */
    void save(T t);

    /**
     * Actualiza una entidad de tipo T.
     *
     * @param t La entidad de tipo T que se va a actualizar.
     */
    void update(T t);

    /**
     * Busca una entidad de tipo T por localidad y provincia.
     *
     * @param localidad  La localidad de la entidad a buscar.
     * @param provincia  La provincia de la entidad a buscar.
     * @return Un objeto Optional que contiene la entidad de tipo T si se encuentra, o un valor vacío si no se encuentra.
     */
    Optional<T> findByLocalidadAndProvincia(String localidad, String provincia);

    /**
     * Obtiene una lista de todas las entidades de tipo T.
     *
     * @return Una lista que contiene todas las entidades de tipo T.
     */
    List<T> findAll();

    /**
     * Elimina una entidad de tipo T.
     *
     * @param t La entidad de tipo T que se va a eliminar.
     */
    void delete(T t);

    /**
     * Elimina todas las entidades de tipo T.
     */
    void deleteAll();
}