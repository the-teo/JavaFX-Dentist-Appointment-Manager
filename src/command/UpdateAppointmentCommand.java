package command;

import domain.Appoitment;
import domain.Patient;
import service.AppoitmentService;

public class UpdateAppointmentCommand implements Command {
    private final AppoitmentService service;
    private final Appoitment originalAppointment;
    private final Appoitment newAppointment;

    public UpdateAppointmentCommand(AppoitmentService service, Appoitment original, Appoitment updated) {
        this.service = service;
        this.originalAppointment = new Appoitment(original);
        this.newAppointment = updated;
    }

    @Override
    public void executeRedo() throws Exception {
        service.update(newAppointment);
    }

    @Override
    public void executeUndo() throws Exception {
        // Revert to the state saved before the update
        service.update(originalAppointment);
    }
}
