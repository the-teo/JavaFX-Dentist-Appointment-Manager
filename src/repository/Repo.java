package repository;

import java.util.List;
import java.util.Optional;

public interface Repo<Id, T> {
    T save(T entity);
    Optional<T> findById(Id id);
    List<T> findAll();
    boolean deleteById(Id id);
    boolean existsById(Id id);
}
