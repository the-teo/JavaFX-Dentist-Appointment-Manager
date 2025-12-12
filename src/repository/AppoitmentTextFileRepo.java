package repository;

import domain.Appoitment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AppoitmentTextFileRepo extends FilteredRepo<Integer, Appoitment> {

    protected final Map<Integer, Appoitment> storage = new ConcurrentHashMap<>();
    protected final String fileName;

    public AppoitmentTextFileRepo(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        storage.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Appoitment appoitment = Appoitment.fromString(line);
                storage.put(appoitment.getId(), appoitment);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException("Error reading from file: " + fileName, e);
        }
    }

        private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Appoitment appoitment : storage.values()) {
                writer.write(appoitment.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + fileName, e);
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