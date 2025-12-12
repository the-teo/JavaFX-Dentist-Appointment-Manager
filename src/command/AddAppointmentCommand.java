package command;

import domain.Appoitment;
import service.AppoitmentService;

public class AddAppointmentCommand implements Command {
    private final AppoitmentService service;
    private final Appoitment appointmentToAdd;

    public AddAppointmentCommand(AppoitmentService service, Appoitment appointmentToAdd) {
        this.service = service;
        this.appointmentToAdd = appointmentToAdd;
    }

    @Override
    public void executeRedo() throws Exception {
        service.create(appointmentToAdd);
    }

    @Override
    public void executeUndo() throws Exception {
        service.delete(appointmentToAdd.getId());
    }
}