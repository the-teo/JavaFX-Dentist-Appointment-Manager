package repository;

import domain.Appoitment;

import java.util.List;
import java.util.stream.Collectors;


public record AppoitmentDateFilter(String date) implements AbstractFilter<Appoitment> {

    @Override
    public List<Appoitment> filter(List<Appoitment> appoitments) {
        return appoitments.stream()
                .filter(appoitment -> appoitment.getDate().equals(date))
                .collect(Collectors.toList());
    }
}
