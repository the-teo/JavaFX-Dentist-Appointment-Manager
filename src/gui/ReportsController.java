package gui;

import domain.Appoitment;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import service.ReportingService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML private TextArea resultArea;
    private ReportingService reportingService;

    public void setReportingService(ReportingService service) {
        this.reportingService = service;
        resultArea.setText("Welcome to the Reporting Dashboard. Select a report above to run it.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<String> showTextInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    private void displayResults(String title, List<?> results) {
        if (results == null || results.isEmpty()) {
            resultArea.setText(title + "\n\n--- NO RESULTS FOUND ---");
        } else {
            String output = title + "\n\n" + results.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
            resultArea.setText(output);
        }
    }


    @FXML
    private void reportAppointmentsForPatient() {
        Optional<String> result = showTextInputDialog("Report 1", "Appts by Patient ID", "Enter Patient ID:");
        result.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                List<Appoitment> appts = reportingService.getAppointmentsForPatient(id);
                displayResults("Report 1: Appointments for Patient ID " + id, appts);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Patient ID.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Report Error", e.getMessage());
            }
        });
    }

    @FXML
    private void reportAppointmentsByKeyword() {
        Optional<String> idResult = showTextInputDialog("Report 2", "Appts by Keyword", "Enter Patient ID:");
        idResult.ifPresent(idStr -> {
            Optional<String> keywordResult = showTextInputDialog("Report 2", "Appts by Keyword", "Enter Keyword in Description:");
            keywordResult.ifPresent(keyword -> {
                try {
                    int id = Integer.parseInt(idStr);
                    List<Appoitment> appts = reportingService.getAppointmentsByPatientAndKeyword(id, keyword);
                    displayResults("Report 2: Appts for ID " + id + " containing '" + keyword + "'", appts);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Patient ID.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Report Error", e.getMessage());
                }
            });
        });
    }

    @FXML
    private void reportPatientPhoneNumber() {
        Optional<String> result = showTextInputDialog("Report 3", "Patient Phone Number", "Enter Patient ID:");
        result.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                String phone = reportingService.getPatientPhoneNumber(id);
                resultArea.setText("Report 3: Phone Number\n\nPatient ID " + id + " Phone: " + phone);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Patient ID.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Report Error", e.getMessage());
            }
        });
    }

    @FXML
    private void reportAppointmentsBetweenDates() {
        Optional<String> idResult = showTextInputDialog("Report 4", "Appts Between Dates", "Enter Patient ID:");
        idResult.ifPresent(idStr -> {
            Optional<String> startResult = showTextInputDialog("Report 4", "Start Date", "Enter Start Date (DD.MM.YYYY):");
            startResult.ifPresent(startStr -> {
                Optional<String> endResult = showTextInputDialog("Report 4", "End Date", "Enter End Date (DD.MM.YYYY):");
                endResult.ifPresent(endStr -> {
                    try {
                        int id = Integer.parseInt(idStr);
                        List<Appoitment> appts = reportingService.getAppointmentsBetweenDates(id, startStr, endStr);
                        displayResults("Report 4: Appts for ID " + id + " between " + startStr + " and " + endStr, appts);
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Patient ID.");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Report Error", e.getMessage());
                    }
                });
            });
        });
    }

    @FXML
    private void reportCountByStatus() {
        Map<String, Long> counts = reportingService.countAppointmentsByStatus();

        String output = "Report 5: Appointment Count by Status\n\n" + counts.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        resultArea.setText(output);
    }
}