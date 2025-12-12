package repository;

import domain.Patient;

import java.util.List;
import java.util.stream.Collectors;


public class PatientNameFilter implements AbstractFilter<Patient> {
    private final String namePattern;


    // filter by name
    public PatientNameFilter(String namePattern) {
        this.namePattern = namePattern.toLowerCase();
    }

    @Override
    public List<Patient> filter(List<Patient> patients) {
        return patients.stream()
                .filter(patient -> patient.getName().toLowerCase().contains(namePattern))
                .collect(Collectors.toList());
    }
}


