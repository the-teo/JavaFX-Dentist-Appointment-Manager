package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FilteredRepo<Id, T extends domain.Id<Id>> implements Repo<Id, T> {
    protected final Map<Id, T> storage = new ConcurrentHashMap<>();

    public List<T> filter(AbstractFilter<T> filter) {
        return filter.filter(findAll());
    }

    @Override
    public T save(T entity){
        if(entity == null){
            throw new IllegalArgumentException("entity is null");
        }
        storage.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Id id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean  existsById(Id id) {
        return storage.containsKey(id);
    }

    @Override
    public boolean  deleteById(Id id) {
        return storage.remove(id) != null;
    }
}
