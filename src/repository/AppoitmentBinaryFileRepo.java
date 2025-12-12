package repository;

import domain.Appoitment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AppoitmentBinaryFileRepo extends FilteredRepo<Integer, Appoitment> {

    protected final Map<Integer, Appoitment> storage = new ConcurrentHashMap<>();
    protected final String fileName;

    public AppoitmentBinaryFileRepo(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        storage.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Map<Integer, Appoitment> loadedMap = (Map<Integer, Appoitment>) ois.readObject();
            storage.putAll(loadedMap);
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading from binary file: " + fileName, e);
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to binary file: " + fileName, e);
        }
    }

    @Override
    public Appoitment save(Appoitment entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        storage.put(entity.getId(), entity);
        saveData();
        return entity;
    }

    @Override
    public Optional<Appoitment> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Appoitment> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsById(Integer id) {
        return storage.containsKey(id);
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean removed = storage.remove(id) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }
}