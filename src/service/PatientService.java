package service;

import domain.Patient;
import repository.Repo;

import java.util.List;
import java.util.stream.Collectors;

public class PatientService {
    private final Repo<Integer,Patient> repo;
    private final ValidatorPatient validatorPatient = new ValidatorPatient();

    public PatientService(Repo<Integer,Patient> repo) {
        this.repo = repo;
    }

    public Patient create(Patient p) {
        if(repo.existsById(p.getId())) {
            throw new IllegalArgumentException("Patient with id " + p.getId() + " already exists");
        }
        validatorPatient.validatePatient(p);
        return repo.save(p);
    }

    public Patient update(Patient p) throws PatientNotFound {
        if(!repo.existsById(p.getId()))
            throw new PatientNotFound(p.getId());
        validatorPatient.validatePatient(p);
        return repo.save(p);
    }

    public Patient getById(Integer id) {
        return repo.findById(id).orElseThrow(()-> new PatientNotFound(id));
    }

    public List<Patient> getAll() {
        return repo.findAll();
    }

    public void delete(Integer id) {
        if(!repo.deleteById(id)) {
            throw new PatientNotFound(id);
        }
    }

    public boolean patientExists(Integer id) {
        return repo.existsById(id);
    }

    public List<Patient> filterByName(String name) {
        List<Patient> patients = getAll();
        return patients.stream()
                .filter(patient -> patient.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }



    public List<Patient> filterByPhone(String phone) {
        List<Patient> patients = getAll();
        return patients.stream()
                .filter(patient -> patient.getPhone().startsWith(phone))
                .collect(Collectors.toList());
    }
}
