package repository;

import domain.Appoitment;

import java.util.List;
import java.util.stream.Collectors;

public class AppoitmentPatientFilter implements AbstractFilter<Appoitment> {
    private final Integer patientId;

    public AppoitmentPatientFilter(Integer patientId) {
        this.patientId = patientId;
    }

    @Override
    public List<Appoitment> filter(List<Appoitment> appoitments) {
        return appoitments.stream()
                .filter(appoitment -> appoitment.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
}
