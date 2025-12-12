package command;

import domain.Patient;
import service.PatientService;

public class DeletePatientCommand implements Command {
    private final PatientService service;
    private final Patient patientToDelete;

    public DeletePatientCommand(PatientService service, Patient patientToDelete) {
        this.service = service;
        this.patientToDelete = patientToDelete;
    }

    @Override
    public void executeRedo() throws Exception {
        service.delete(patientToDelete.getId());
    }

    @Override
    public void executeUndo() throws Exception {
        service.create(patientToDelete);
    }
}
