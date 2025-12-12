package gui;

import command.*;
import domain.Appoitment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import service.AppoitmentService;

import java.util.List;
import java.util.Optional;

public class AppointmentController {

    @FXML private TableView<Appoitment> appointmentTable;
    @FXML private TableColumn<Appoitment, Integer> idColumn;
    @FXML private TableColumn<Appoitment, Integer> patientIdColumn;
    @FXML private TableColumn<Appoitment, String> dateColumn;
    @FXML private TableColumn<Appoitment, String> timeColumn;
    @FXML private TableColumn<Appoitment, String> statusColumn;
    @FXML private TableColumn<Appoitment, String> descriptionColumn;

    @FXML private TextField idInput;
    @FXML private TextField patientIdInput;
    @FXML private TextField dateInput;
    @FXML private TextField timeInput;
    @FXML private TextField statusInput;
    @FXML private TextField descriptionInput;

    private AppoitmentService appoitmentService;
    private UndoRedoManager undoRedoManager;

    public void setAppoitmentService(AppoitmentService service) {
        this.appoitmentService = service;
        initializeTable();
        loadAppointmentData(appoitmentService.getAll());
    }

    public void setUndoRedoManager(UndoRedoManager manager) {
        this.undoRedoManager = manager;
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId()));

        patientIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPatientId()));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTime()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        appointmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        idInput.setText(String.valueOf(newSelection.getId()));
                        patientIdInput.setText(String.valueOf(newSelection.getPatientId()));
                        dateInput.setText(newSelection.getDate());
                        timeInput.setText(newSelection.getTime());
                        statusInput.setText(newSelection.getStatus());
                        descriptionInput.setText(newSelection.getDescription());
                    } else {
                        clearInputFields();
                    }
                });
    }

    private void loadAppointmentData(List<Appoitment> appointments) {
        appointmentTable.setItems(FXCollections.observableArrayList(appointments));
    }

    private void clearInputFields() {
        idInput.clear();
        patientIdInput.clear();
        dateInput.clear();
        timeInput.clear();
        statusInput.clear();
        descriptionInput.clear();
    }

    @FXML
    private void handleCreate() {
        try {
            Appoitment newAppt = createAppointmentFromInput();

            Command addCommand = new AddAppointmentCommand(appoitmentService, newAppt);
            undoRedoManager.execute(addCommand);

            loadAppointmentData(appoitmentService.getAll());
            clearInputFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment created successfully.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Creation Error", e.getMessage());
        }
    }


    @FXML
    private void handleUpdate() {
        try {
            Integer appointmentId = Integer.parseInt(idInput.getText());

            String patientIdText = patientIdInput.getText();
            if (patientIdText == null || patientIdText.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Patient ID cannot be empty.");
                return;
            }
            Integer patientId = Integer.parseInt(patientIdText);
            String date = dateInput.getText();
            String time = timeInput.getText();
            String status = statusInput.getText();
            String description = descriptionInput.getText();

            Appoitment originalAppointment = appoitmentService.getById(appointmentId);

            Appoitment newAppointment = new Appoitment(
                    appointmentId, patientId, date, time, status, description
            );

            Command updateCommand = new UpdateAppointmentCommand(
                    appoitmentService,
                    originalAppointment,
                    newAppointment
            );

            undoRedoManager.execute(updateCommand);

            loadAppointmentData(appoitmentService.getAll());
            clearInputFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment updated successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID and Patient ID must be valid integers. Check inputs.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            Integer id = Integer.parseInt(idInput.getText());

            Appoitment restore = appoitmentService.getById(id);

            Command deleteCommand = new DeleteAppointmentCommand(appoitmentService, restore);
            undoRedoManager.execute(deleteCommand);

            loadAppointmentData(appoitmentService.getAll());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment deleted successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID must be a valid integer.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Deletion Error", e.getMessage());
        }
    }


    @FXML
    private void handleFilterByDate() {
        Optional<String> result = showTextInputDialog("Filter Appointments", "Enter Date:", "Date (DD.MM.YYYY):");

        result.ifPresent(date -> {
            try {
                if (date.isBlank()) {
                    loadAppointmentData(appoitmentService.getAll());
                    return;
                }

                List<Appoitment> filteredList = appoitmentService.filterByDate(date);

                loadAppointmentData(filteredList);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Filter Error", "Could not filter by date: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleFilterByPatientId() {
        Optional<String> result = showTextInputDialog("Filter Appointments", "Enter Patient ID:", "Patient ID:");
        result.ifPresent(patientIdStr -> {
            try {
                Integer patientId = Integer.parseInt(patientIdStr);
                List<Appoitment> filteredList = appoitmentService.filterByPatient(patientId);
                loadAppointmentData(filteredList);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Patient ID must be an integer.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Filter Error", e.getMessage());
            }
        });
    }

    @FXML
    private void handleShowAll() {
        try {
            loadAppointmentData(appoitmentService.getAll());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private Appoitment createAppointmentFromInput() throws NumberFormatException {
        Integer id = Integer.parseInt(idInput.getText());
        Integer patientId = Integer.parseInt(patientIdInput.getText());
        String date = dateInput.getText();
        String time = timeInput.getText();
        String status = statusInput.getText();
        String description = descriptionInput.getText();

        return new Appoitment(id, patientId, date, time, status, description);
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
}