package main;

import command.UndoRedoManager;
import domain.Appoitment;
import domain.Patient;
import gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import service.AppoitmentService;
import service.PatientService;
import service.ReportingService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainApplication extends Application {

    private PatientService patientService;
    private AppoitmentService appoitmentService;
    private ReportingService reportingService;
    private UndoRedoManager undoRedoManager;

    @Override
    public void init() throws Exception {
        super.init();
        initializeServices();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/gui/MainApp.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();

        mainController.setServices(
                this.patientService,
                this.appoitmentService,
                this.reportingService,
                this.undoRedoManager
        );
        primaryStage.setTitle("Management System - JavaFX");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void initializeServices() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("settings.properties"));

            String repoType = properties.getProperty("Repository").trim();
            String location = properties.getProperty("Location", "").trim().replace("\"", "");
            String patientsFile = properties.getProperty("Patients").replace("\"", "");
            String appointmentsFile = properties.getProperty("Appointments").replace("\"", "");

            Repo<Integer, Patient> patientRepo = null;
            Repo<Integer, Appoitment> appoitmentRepo = null;
            System.out.println("-----------------------------------------");
            if ("text".equalsIgnoreCase(repoType)) {
                patientRepo = new PatientTextFileRepo(patientsFile);
                appoitmentRepo = new AppoitmentTextFileRepo(appointmentsFile);


                System.out.println("----- Using TEXT file repositories ------");
            } else if ("binary".equalsIgnoreCase(repoType)) {
                patientRepo = new PatientBinaryFileRepo(patientsFile);
                appoitmentRepo = new AppoitmentBinaryFileRepo(appointmentsFile);

                System.out.println("----- Using BINARY file repositories -----");
            } else if ("database".equalsIgnoreCase(repoType)) {
                patientRepo = new PatientDBRepo(location);
                appoitmentRepo = new AppointmentDBRepo(location);
                System.out.println("----- Using DATABASE repositories -----");
            } else if ("json".equalsIgnoreCase(repoType)) {
                patientRepo = new PatientJSONFileRepo(patientsFile);
                appoitmentRepo = new AppointmentJSONFileRepo(appointmentsFile);
                System.out.println("----- Using JSON file repositories  ------");
            } else if ("xml".equalsIgnoreCase(repoType)) {
                patientRepo = new PatientXmlRepo(patientsFile);
                appoitmentRepo = new AppointmentXmlRepo(appointmentsFile);
                System.out.println("----- Using XML file repositories ------");
            }
            else {
                patientRepo = new FilteredRepo<>();
                initializePatient((FilteredRepo<Integer, Patient>) patientRepo);

                appoitmentRepo = new FilteredRepo<>();
                initializeAppointment((FilteredRepo<Integer, Appoitment>) appoitmentRepo);

                System.out.println("----- Using IN-MEMORY repository -----");
            }
            System.out.println("-----------------------------------------");

            this.patientService = new PatientService(patientRepo);
            this.appoitmentService = new AppoitmentService(appoitmentRepo, this.patientService);
            this.reportingService = new ReportingService(this.patientService, this.appoitmentService);
            this.undoRedoManager = new UndoRedoManager();
        } catch (Exception e) {
            System.err.println("FATAL: Could not initialize services: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void initializePatient(FilteredRepo<Integer, Patient> repo) {
        repo.save(new Patient(12, "Maria", "076556155", "maria@example.com"));
        repo.save(new Patient(232, "Ana", "076213325", "ana@example.com"));
        repo.save(new Patient(321, "George", "0761261522", "george@example.com"));
        repo.save(new Patient(541, "Alin", "0752223115", "alin@example.com"));
        repo.save(new Patient(569, "Vlad", "072531351", "vlad@example.com"));

    }

    private static void initializeAppointment(FilteredRepo<Integer, Appoitment>  repo) {
        repo.save(new Appoitment(1, 12, "20.10.2025", "10.50", "Confirmed", "Routine dental cleaning and checkup."));
        repo.save(new Appoitment(2, 232, "23.08.2022", "11.10", "Completed", "Composite filling for molar."));
        repo.save(new Appoitment(3, 321, "09.10.2025", "08.15", "Pending", "Consultation for braces."));
        repo.save(new Appoitment(4, 541, "31.12.2025", "13.35", "Cancelled", "Wisdom tooth extraction procedure."));
        repo.save(new Appoitment(5, 569, "09.10.2025", "09.40", "Confirmed", "Follow-up checkup after surgery."));
        repo.save(new Appoitment(6, 569, "23.10.2025", "14.20", "Confirmed", "Deep scaling and root planing."));
    }

    static void main(String[] args) {
        launch(args);
    }
}