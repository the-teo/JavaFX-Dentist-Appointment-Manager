package service;

import domain.Appoitment;
import domain.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingService {
    private final PatientService patientService;
    private final AppoitmentService appoitmentService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ReportingService(PatientService patientService, AppoitmentService appoitmentService) {
        this.patientService = patientService;
        this.appoitmentService = appoitmentService;
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Warning: Invalid date format found: " + dateStr);
            return LocalDate.MIN;
        }
    }

    /**
     * REPORT 1: All appointments for a certain patient (and their status).
     * Usage: Display ID, Date, and Status from the resulting list.
     */
    public List<Appoitment> getAppointmentsForPatient(int patientId) {
        return appoitmentService.getAll().stream()
                .filter(app -> app.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    /**
     * REPORT 2: The phone number of a certain patient (given by id).
     * Usage: Simple lookup mapping Patient ID -> Phone String.
     */
    public String getPatientPhoneNumber(int patientId) {
        // We use the stream to find the patient, map to phone, and return string
        return patientService.getAll().stream()
                .filter(p -> p.getId().equals(patientId))
                .map(Patient::getPhone)
                .findFirst()
                .orElse("Patient not found");
    }

    /**
     * REPORT 3: All sessions of a given client between 2 given dates.
     * Usage: Filters appointments by patient ID and date range.
     */
    public List<Appoitment> getAppointmentsBetweenDates(int patientId, String startDateStr, String endDateStr) {
        final LocalDate startDate = parseDate(startDateStr);
        final LocalDate endDate = parseDate(endDateStr);

        return appoitmentService.getAll().stream()
                // 1. Filter by Patient
                .filter(app -> app.getPatientId().equals(patientId))
                // 2. Filter by Date Range (inclusive)
                .filter(app -> {
                    LocalDate appDate = parseDate(app.getDate());
                    return !appDate.isBefore(startDate) && !appDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }

    /**
     * REPORt 4: All sessions for a client involving certain given exercises.
     * Usage: Filters based on keywords found in the 'description' field.
     */
    public List<Appoitment> getAppointmentsByPatientAndKeyword(int patientId, String keyword) {
        final String lowerKeyword = keyword.toLowerCase();

        return appoitmentService.getAll().stream()
                // 1. Filter by Patient
                .filter(app -> app.getPatientId().equals(patientId))
                // 2. Filter by Keyword in Description (Safe null check included)
                .filter(app -> app.getDescription() != null &&
                        app.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Report 5: Count of appointments per status.
     * Usage: Returns a map like {"Confirmed"=5, "Pending"=2}
     */
    public Map<String, Long> countAppointmentsByStatus() {
        return appoitmentService.getAll().stream()
                .filter(app -> app.getStatus() != null)
                .collect(Collectors.groupingBy(
                        Appoitment::getStatus,
                        Collectors.counting()
                ));
    }
}