package gui;

import command.UndoRedoManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import service.AppoitmentService;
import service.PatientService;
import service.ReportingService;

import java.io.IOException;

public class MainController {

    @FXML private BorderPane mainContainer;

    private PatientService patientService;
    private AppoitmentService appoitmentService;
    private ReportingService reportingService;

    private UndoRedoManager undoRedoManager;

    public void setServices(
            PatientService ps,
            AppoitmentService as,
            ReportingService rs,
            UndoRedoManager urm) {
        this.patientService = ps;
        this.appoitmentService = as;
        this.reportingService = rs;
        this.undoRedoManager = urm;

        showPatientView();
    }

    @FXML
    private void showPatientView() {
        loadView("/gui/PatientsGUI.fxml");
    }

    @FXML
    private void showAppointmentView() {
        loadView("/gui/AppointmentGUI.fxml");
    }

    @FXML
    private void showReportsView() {
        loadView("/gui/ReportsGUI.fxml");
    }

    @FXML
    private void handleUndo() {
        try {
            if (undoRedoManager.canUndo()) {
                undoRedoManager.undo();
                System.out.println("Undo successful.");

                showPatientView();
            } else {
                System.out.println("Nothing to undo.");
            }
        } catch (Exception e) {
            System.err.println("Undo failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRedo() {
        try {
            if (undoRedoManager.canRedo()) {
                undoRedoManager.redo();
                System.out.println("Redo successful.");

                showPatientView();
            } else {
                System.out.println("Nothing to redo.");
            }
        } catch (Exception e) {
            System.err.println("Redo failed: " + e.getMessage());
        }
    }


    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof PatientController pc) {
                pc.setPatientService(patientService);
                pc.setUndoRedoManager(undoRedoManager);
            } else if (controller instanceof AppointmentController pc) {
                pc.setAppoitmentService(appoitmentService);
                pc.setUndoRedoManager(undoRedoManager);
            } else if (controller instanceof ReportsController) {
            ((ReportsController) controller).setReportingService(reportingService);
        }

            mainContainer.setCenter(view);

        } catch (IOException e) {
            System.err.println("Failed to load FXML view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}