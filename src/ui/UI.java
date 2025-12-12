package ui;

import domain.Appoitment;
import domain.Patient;
import service.AppoitmentService;
import service.PatientNotFound;
import service.PatientService;
import service.ReportingService;

import java.util.List;
import java.util.Scanner;


public class UI {
    private final PatientService patientService;
    private final AppoitmentService appoitmentService;
    private final ReportingService reportingService;
    private final Scanner scanner = new Scanner(System.in);

    public UI(PatientService patientService, AppoitmentService appoitmentService, ReportingService reportingService) {
        this.patientService = patientService;
        this.appoitmentService = appoitmentService;
        this.reportingService = reportingService;
    }

    public void start() {
        System.out.println("-----------Dentist Management------------");
        boolean running = true;

        while (running) {
            System.out.println("Main Menu: ");
            System.out.println("1. Patients ");
            System.out.println("2. Appoitments");
            System.out.println("3. Reports");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> patientMenu();
                case "2" -> appointmentMenu();
                case "3" -> reportMenu();
                case "0" -> running = false;
                default -> System.out.println("Invalid option, retry! ");
            }
        }
    }

    private void patientMenu(){
        boolean pMenu = true;
        while(pMenu) {
            System.out.println("\n Patients Menu:");
            System.out.println("1. List All Patients");
            System.out.println("2. Create Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Filter Patient by Name");
            System.out.println("6. Filter Patient by Phone");
            System.out.println("0. Back to Main Menu");

            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> listAll();
                case "2" -> create();
                case "3" -> update();
                case "4" -> delete();
                case "5" -> filterPatientsByName();
                case "6" -> filterPatientsByPhone();
                case "0" -> pMenu = false;
                default -> System.out.println("Invalid option, retry! ");
            }
        }
    }

    private void appointmentMenu(){
        boolean aMenu = true;
        while(aMenu) {
            System.out.println("\nAppointments Menu:");
            System.out.println("1. List All Appointments");
            System.out.println("2. Create Appointment");
            System.out.println("3. Update Appointment");
            System.out.println("4. Delete Appointment");
            System.out.println("5. Filter Appointment by Date");
            System.out.println("6. Filter Appointment by Patient");
            System.out.println("0. Back to Main Menu");

            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> listAllAppointments();
                case "2" -> createAppointment();
                case "3" -> updateApppointment();
                case "4" -> deleteAppointment();
                case "5" -> filterAppointmentByDate();
                case "6" -> filterAppointmentByPatient();
                case "0" -> aMenu = false;
                default -> System.out.println("Invalid option, retry! ");
            }
        }
    }

    private void listAllAppointments() {
        List<Appoitment> appoitments = appoitmentService.getAll();
        if(appoitments.isEmpty()) {
            System.out.println("No appointments found");
        } else  {
            for(Appoitment appoitment : appoitments) {
                System.out.println(appoitment);
            }
        }
    }

    private void createAppointment() {
        try {
            System.out.println("Enter Appointment ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Patient ID: ");
            Integer patientId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Date (DD.MM.YYYY): ");
            String date = scanner.nextLine();
            System.out.println("Enter Time (HH.MM): ");
            String time= scanner.nextLine();
            System.out.println("Enter status: ");
            String status= scanner.nextLine();
            System.out.println("Enter description: ");
            String description= scanner.nextLine();

            Appoitment a = new Appoitment(id,patientId,date,time,status, description);
            appoitmentService.create(a);
            System.out.println("Appoitment Created!");
            System.out.println(a);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void updateApppointment() {
        try {
            listAllAppointments();
            System.out.print("Enter Appointment Id: ");
            Integer id = Integer.parseInt(scanner.nextLine());

            Appoitment existingAppointment = appoitmentService.getById(id);

            System.out.print("Enter Patient ID (press enter not to): ");
            String patientIdStr = scanner.nextLine();
            Integer patientId = patientIdStr.trim().isEmpty() ? existingAppointment.getPatientId() : Integer.parseInt(patientIdStr);

            System.out.print("Enter Date (press enter not to): ");
            String date = scanner.nextLine();
            if(date.trim().isEmpty()) {
                date = existingAppointment.getDate();
            }

            System.out.print("Enter Time (press enter not to): ");
            String time = scanner.nextLine();
            if(time.trim().isEmpty()) {
                time = existingAppointment.getTime();
            }

            System.out.print("Enter Status (press enter not to): ");
            String status = scanner.nextLine();
            if(status.trim().isEmpty()) {
                status = existingAppointment.getTime();
            }

            System.out.print("Enter Description (press enter not to): ");
            String description = scanner.nextLine();
            if(description.trim().isEmpty()) {
                description = existingAppointment.getTime();
            }

            Appoitment appointment = new Appoitment(id, patientId, date, time, status,description);
            appoitmentService.update(appointment);
            System.out.println("Appointment updated successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAppointment() {
        try {
            listAllAppointments();
            System.out.print("Enter Appointment Id: ");
            Integer id = Integer.parseInt(scanner.nextLine());

            appoitmentService.delete(id);
            System.out.println("Appointment deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void filterAppointmentByDate() {
        try {
            System.out.println("Enter date DD.MM.YYYY: ");
            String date = scanner.nextLine();
            List<Appoitment> filtered = appoitmentService.filterByDate(date);

            if(filtered.isEmpty()) {
                System.out.println("No appointments found.");
            } else {
                System.out.println("Appointment on " + date + " found!");
                for(Appoitment appoitment : filtered) {
                    System.out.println(appoitment);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void filterAppointmentByPatient() {
        try {
            System.out.println("Enter patient ID: ");
            Integer patientId = Integer.parseInt(scanner.nextLine());
            List<Appoitment> filtered = appoitmentService.filterByPatient(patientId);
            if(filtered.isEmpty()) {
                System.out.println("No appointments found");
            } else {
                System.out.println("Appointment for patient ID " + patientId + " found!");
                for(Appoitment appoitment : filtered) {
                    System.out.println(appoitment);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    private void listAll() {
        List<Patient> patients = patientService.getAll();
        if(patients.isEmpty()) {
            System.out.println("No patients found");
        }else  {
            for(Patient patient : patients)
                System.out.println(patient);
        }
    }

    private void create() {
        try {
            System.out.println("Enter ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Name: ");
            String name = scanner.nextLine();
            System.out.println("Enter Phone: ");
            String phone = scanner.nextLine();
            System.out.println("Enter Email: ");
            String email = scanner.nextLine();

            Patient p = new Patient(id,name,phone,email);

            patientService.create(p);
            System.out.println("Patient created successfully");
        }
        catch (Exception e)
            {
            System.out.println(e.getMessage());
            }
    }

    private void update() {
        try {
            listAll();
            System.out.println("Enter ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());

            Patient existingPatient = patientService.getById(id);
            if(existingPatient == null) {
                System.out.println("Patient doesn't exist.");
                return;
            }

            System.out.println("Enter New Name (enter not to): ");
            String name = scanner.nextLine();
            if(name.trim().isEmpty()) {
                name = existingPatient.getName();
            }

            System.out.println("Enter Phone (enter not to): ");
            String phone = scanner.nextLine();
            if(phone.trim().isEmpty()) {
                phone = existingPatient.getPhone();
            }

            System.out.println("Enter Email (enter not to): ");
            String email = scanner.nextLine();
            if(email.trim().isEmpty()) {
                email = existingPatient.getEmail();
            }

            Patient p = new Patient(id,name,phone, email);
            patientService.update(p);
            System.out.println("Patient updated successfully");
        } catch (Exception e)
            {
            System.out.println(e.getMessage());
            }
        }

    private void delete() {
        try {
            listAll();
            System.out.println("Enter ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            patientService.delete(id);
            System.out.println("Patient deleted successfully");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
        catch(PatientNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    private void filterPatientsByName() {
        try {
            System.out.println("Enter name: ");
            String name = scanner.nextLine();
            List<Patient> filtered = patientService.filterByName(name);
            System.out.println("Patients with the name " + name + ": " + "\n");
            for(Patient patient : filtered)
                System.out.println(patient);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void filterPatientsByPhone() {
        try {
            System.out.println("Enter phone number: ");
            String phone = scanner.nextLine();
            List<Patient> filtered = patientService.filterByPhone(phone);
            System.out.println("Patients with the phone " + phone + ": " + "\n");
            for (Patient patient : filtered)
                System.out.println(patient);
        }
        catch (Exception e)
            {
            System.out.println(e.getMessage());
            }
    }

    // In ui/UI.java (add this new method)

    private void reportMenu() {
        boolean rMenu = true;
        while (rMenu) {
            System.out.println("\n--- Reporting Menu (Java 8 Streams) ---");
            System.out.println("1. Appointments for a Patient (by ID)");
            System.out.println("2. Phone Number of a Patient (by ID)");
            System.out.println("3. Appointments for Patient Between Dates");
            System.out.println("4. Appointments for Patient by Keyword in Description");
            System.out.println("5. Appointment Count by Status");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> reportAppointmentsForPatient();
                case "2" -> reportPatientPhoneNumber();
                case "3" -> reportAppointmentsBetweenDates();
                case "4" -> reportAppointmentsByKeyword();
                case "5" -> reportCountByStatus();
                case "0" -> rMenu = false;
                default -> System.out.println("Invalid option, retry!");
            }
        }
    }

    private void reportAppointmentsForPatient() {
        try {
            System.out.print("Enter Patient ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            List<Appoitment> appointments = reportingService.getAppointmentsForPatient(id);
            if (appointments.isEmpty()) {
                System.out.println("No appointments found for Patient ID: " + id);
            } else {
                System.out.println("Appointments for Patient ID " + id + ":");
                appointments.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }


    private void reportPatientPhoneNumber() {
        try {
            System.out.print("Enter Patient ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            String phone = reportingService.getPatientPhoneNumber(id);
            System.out.println("Phone Number for Patient ID " + id + ": " + phone);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void reportAppointmentsBetweenDates() {
        try {
            System.out.print("Enter Patient ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            // NOTE: The ReportingService expects "yyyy-MM-dd"
            System.out.print("Enter Start Date (yyyy-MM-dd): ");
            String start = scanner.nextLine();
            System.out.print("Enter End Date (yyyy-MM-dd): ");
            String end = scanner.nextLine();

            List<Appoitment> appointments = reportingService.getAppointmentsBetweenDates(id, start, end);
            if (appointments.isEmpty()) {
                System.out.println("No appointments found in that range for Patient ID: " + id);
            } else {
                System.out.println("Appointments for Patient ID " + id + " between " + start + " and " + end + ":");
                appointments.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void reportAppointmentsByKeyword() {
        try {
            System.out.print("Enter Patient ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter keyword in description: ");
            String keyword = scanner.nextLine();

            List<Appoitment> appointments = reportingService.getAppointmentsByPatientAndKeyword(id, keyword);
            if (appointments.isEmpty()) {
                System.out.println("No appointments found with that keyword for Patient ID: " + id);
            } else {
                System.out.println("Appointments for Patient ID " + id + " matching keyword '" + keyword + "':");
                appointments.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void reportCountByStatus() {
        System.out.println("Appointment Counts by Status:");
        reportingService.countAppointmentsByStatus()
                .forEach((status, count) -> System.out.println(status + ": " + count));
    }
}
