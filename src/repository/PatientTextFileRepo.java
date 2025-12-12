package repository;

import domain.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PatientTextFileRepo extends FilteredRepo<Integer, Patient> {
    private final Map<Integer, Patient> storage = new ConcurrentHashMap<>();
    public final String fileName;

    public PatientTextFileRepo(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        storage.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Patient patient = Patient.fromString(line);
                storage.put(patient.getId(), patient);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException("Error reading from file: " + fileName, e);
        }
    }

    private void saveData() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Patient patient : storage.values()) {
                writer.write(patient.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + fileName, e);
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
