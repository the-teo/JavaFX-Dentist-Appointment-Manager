package repository;

import com.google.gson.Gson;
import domain.Id;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractJsonRepo<ID, T extends Id<ID>> extends FilteredRepo<ID, T> {

    private final String fileName;
    private final Gson gson = new Gson();

    public AbstractJsonRepo(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    protected abstract Type getListType();

    private void loadData() {
        try (FileReader reader = new FileReader(fileName)) {
            List<T> entities = gson.fromJson(reader, getListType());
            if (entities != null) {
                entities.forEach(this::saveToMap);
            }
        } catch (IOException e) {
            System.out.println("Could not read JSON file. Starting with empty repository: " + e.getMessage());
        }
    }

    private void saveData() {
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(super.findAll(), writer);
        } catch (IOException e) {
            System.err.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    @Override
    public T save(T entity) {
        T result = super.save(entity);
        saveData();
        return result;
    }

    @Override
    public boolean deleteById(ID id) {
        boolean deleted = super.deleteById(id);
        if (deleted) {
            saveData();
        }
        return deleted;
    }

    private T saveToMap(T entity) {
        return super.save(entity);
    }
}