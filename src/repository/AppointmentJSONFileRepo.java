package repository;

import com.google.gson.reflect.TypeToken;
import domain.Appoitment;

import java.lang.reflect.Type;
import java.util.List;

public class AppointmentJSONFileRepo extends AbstractJsonRepo<Integer, Appoitment> {
    public AppointmentJSONFileRepo(String fileName) {
        super(fileName);
    }
    @Override
    protected Type getListType() {
        return new TypeToken<List<Appoitment>>() {
        }.getType();
    }
}

