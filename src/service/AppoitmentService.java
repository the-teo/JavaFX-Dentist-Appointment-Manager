package service;

import domain.Appoitment;
import repository.Repo;

import java.util.List;
import java.util.stream.Collectors;

public class AppoitmentService {
    private final Repo<Integer, Appoitment> repo;
    private final PatientService patientService;
    private final ValidatorAppointment validatorAppointment = new ValidatorAppointment();

    public AppoitmentService(Repo<Integer, Appoitment> repo, PatientService patientService) {
        this.repo = repo;
        this.patientService = patientService;
    }

    public Appoitment create(Appoitment appoitment) {
        if(!patientService.patientExists(appoitment.getPatientId())) {
            throw new IllegalArgumentException("Patient does not exist");
        }

        if (repo.existsById(appoitment.getId())) {
            throw new IllegalArgumentException("Appoitment already exists");
        }
        validatorAppointment.validateAppointment(appoitment);
        return repo.save(appoitment);
    }

    public Appoitment update(Appoitment appoitment) {
        if(!repo.existsById(appoitment.getId())) {
            throw new AppoitmentNotFound(appoitment.getId());
        }

        if(!patientService.patientExists(appoitment.getPatientId()))
            throw new IllegalArgumentException("Patient does not exist");
        validatorAppointment.validateAppointment(appoitment);
        return repo.save(appoitment);
    }

    public Appoitment getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public List<Appoitment> getAll() {
        return repo.findAll();
    }

    public void delete(Integer id) {
        if(!repo.existsById(id)) {
            throw new IllegalArgumentException("Appointment does not exist");
        }
        repo.deleteById(id);
    }

    public List<Appoitment> filterByDate(String date) {
        List<Appoitment> appoitments = getAll();
        return appoitments.stream()
                .filter(appoitment -> appoitment.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Appoitment> filterByPatient(Integer patientId) {
        List<Appoitment> appoitments = getAll();
        return appoitments.stream()
                .filter(appoitment -> appoitment.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
}
