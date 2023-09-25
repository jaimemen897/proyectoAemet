package models;

import java.util.List;
import java.util.Optional;

public interface Crud<T> {
    void save(T t);

    void update(T t);

    Optional<T> findByLocalidadAndProvincia(String a, String b);

    List<T> findAll();

    void delete(T t);

    void deleteAll();
}
