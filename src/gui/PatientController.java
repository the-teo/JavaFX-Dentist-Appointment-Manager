package gui;

import command.*;
import domain.Patient;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.PatientService;

import java.util.List;
import java.util.Optional;

public class PatientController {

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> idColumn;
    @FXML private TableColumn<Patient, String> nameColumn;
    @FXML private TableColumn<Patient, String> phoneColumn;
    @FXML private TableColumn<Patient, String> emailColumn;

    @FXML private TextField idInput;
    @FXML private TextField nameInput;
    @FXML private TextField phoneInput;
    @FXML private TextField emailInput;

    private PatientService patientService;
    private UndoRedoManager undoRedoManager;

    public void setUndoRedoManager(UndoRedoManager manager) {
        this.undoRedoManager = manager;
    }

    public void setPatientService(PatientService service) {
        this.patientService = service;
        initializeTable();
        loadPatientData(patientService.getAll());
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId()));

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        phoneColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhone()));

        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));

        // Listener to populate input fields when a row is selected
        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        idInput.setText(String.valueOf(newSelection.getId()));
                        nameInput.setText(newSelection.getName());
                        phoneInput.setText(newSelection.getPhone());
                        emailInput.setText(newSelection.getEmail());
                    } else {
                        clearInputFields();
                    }
                });
    }

    private void loadPatientData(List<Patient> patients) {
        patientTable.setItems(FXCollections.observableArrayList(patients));
    }

    private void clearInputFields() {
        idInput.clear();
        nameInput.clear();
        phoneInput.clear();
        emailInput.clear();
    }

    @FXML
    private void handleCreate() {
        try {
            Integer id = Integer.parseInt(idInput.getText());
            String name = nameInput.getText();
            String phone = phoneInput.getText();
            String email = emailInput.getText();

            Patient newPatient = new Patient(id, name, phone, email);

            Command addCommand = new AddPatientCommand(patientService, newPatient);
            undoRedoManager.execute(addCommand);

            loadPatientData(patientService.getAll());
            clearInputFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient created successfully.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID must be a valid integer.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            Integer id = Integer.parseInt(idInput.getText());
            Patient originalPatient = patientService.getById(id);
            Patient updatedPatient = new Patient(id, nameInput.getText(), phoneInput.getText(), emailInput.getText());

            Command updateCommand = new UpdatePatientCommand(patientService, originalPatient, updatedPatient);
            undoRedoManager.execute(updateCommand);

            loadPatientData(patientService.getAll());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient updated successfully.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID must be a valid integer.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            Integer id = Integer.parseInt(idInput.getText());

            Patient patientToRestore = patientService.getById(id);

            Command deleteCommand = new DeletePatientCommand(patientService, patientToRestore);
            undoRedoManager.execute(deleteCommand);

            loadPatientData(patientService.getAll());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient deleted successfully.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a valid patient ID.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Deletion Error", e.getMessage());
        }
    }

    @FXML
    private void handleFilterByName() {
        Optional<String> result = showTextInputDialog("Filter Patients", "Enter Name:", "Name:");

        result.ifPresent(name -> {
            try {
                List<Patient> filteredList = patientService.filterByName(name);
                loadPatientData(filteredList);
            } catch (UnsupportedOperationException e) {
                showAlert(Alert.AlertType.WARNING, "Filtering Not Supported", "Filtering is not supported in the current repository mode.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Filter Error", e.getMessage());
            }
        });
    }

    @FXML
    private void handleFilterByPhone() {
        Optional<String> result = showTextInputDialog("Filter Patients", "Enter Phone:", "Phone:");

        result.ifPresent(phone -> {
            try {
                List<Patient> filteredList = patientService.filterByPhone(phone);
                loadPatientData(filteredList);
            } catch (UnsupportedOperationException e) {
                showAlert(Alert.AlertType.WARNING, "Filtering Not Supported", "Filtering is not supported in the current repository mode.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Filter Error", e.getMessage());
            }
        });
    }
    @FXML
    private void handleShowAll() {
        try {
            loadPatientData(patientService.getAll());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }


    public void refreshTable() {
        loadPatientData(patientService.getAll());
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