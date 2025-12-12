package repository;

import domain.Id;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDBRepo<K, T extends Id<K>> implements Repo<K, T> {
    private final String URL;

    public AbstractDBRepo(String URL) {
        this.URL = URL;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.URL);
    }

    @Override
    public abstract T save(T entity);
    @Override
    public abstract Optional<T> findById(K id);
    @Override
    public abstract List<T> findAll();
    @Override
    public abstract boolean deleteById(K id);
    @Override
    public abstract boolean existsById(K id);
}