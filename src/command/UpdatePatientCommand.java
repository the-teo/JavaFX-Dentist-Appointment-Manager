package command;

import domain.Patient;
import service.PatientService;

public class UpdatePatientCommand implements Command {
    private final PatientService service;
    private final Patient originalPatient;
    private final Patient newPatient;

    public UpdatePatientCommand(PatientService service, Patient original, Patient updated) {
        this.service = service;
        this.originalPatient = new Patient(original);
        this.newPatient = updated;
    }

    @Override
    public void executeRedo() throws Exception {
        service.update(newPatient);
    }

    @Override
    public void executeUndo() throws Exception {
        service.update(originalPatient);
    }
}
