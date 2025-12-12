package repository;

import com.google.gson.reflect.TypeToken;
import domain.Patient;

import java.lang.reflect.Type;
import java.util.List;

public class PatientJSONFileRepo extends AbstractJsonRepo<Integer, Patient> {

    public PatientJSONFileRepo(String fileName) {
        super(fileName);
    }

    @Override
    protected Type getListType() {
        return new TypeToken<List<Patient>>() {}.getType();
    }
}