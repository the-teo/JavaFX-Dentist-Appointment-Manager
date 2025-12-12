package repository;

import domain.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class PatientPhoneFilter implements AbstractFilter<Patient> {
    private final String phonePrefix;

    public PatientPhoneFilter(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    @Override
    public List<Patient> filter(List<Patient> patients) {
        return patients.stream()
                .filter(patient -> patient.getPhone().startsWith(phonePrefix))
                .collect(Collectors.toList());
    }
}
