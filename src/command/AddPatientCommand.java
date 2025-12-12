package command;

import domain.Patient;
import service.PatientService;

public class AddPatientCommand implements Command {
    private final PatientService service;
    private final Patient patientToAdd;

    public AddPatientCommand(PatientService service, Patient patientToAdd) {
        this.service = service;
        this.patientToAdd = patientToAdd;
    }

    @Override
    public void executeRedo() throws Exception {
        service.create(patientToAdd);
    }

    @Override
    public void executeUndo() throws Exception {
        service.delete(patientToAdd.getId());
    }
}
