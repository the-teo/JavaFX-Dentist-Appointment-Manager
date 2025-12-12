package service;

public class PatientNotFound extends RuntimeException {
    public PatientNotFound(Integer id) {
        super("Patient with id " + id + " doesn't exist.");
    }
}
