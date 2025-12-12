package repository;

import domain.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PatientBinaryFileRepo extends FilteredRepo<Integer, Patient> {
    private final Map<Integer, Patient> storage = new ConcurrentHashMap<>();
    private final String fileName;

    public PatientBinaryFileRepo(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        storage.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Map<Integer, Patient> loadedMap = (Map<Integer, Patient>) ois.readObject();
            storage.putAll(loadedMap);
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading from binary file: " + fileName, e);
        }
    }

    private void saveData() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to binary file: " + fileName, e);
        }
    }

    @Override
    public Patient save(Patient entity) {
        storage.put(entity.getId(), entity);
        saveData();
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean removed = storage.remove(id) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsById(Integer id) {
        return storage.containsKey(id);
    }
}