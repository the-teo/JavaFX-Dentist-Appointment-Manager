package command;

import domain.Appoitment;
import service.AppoitmentService;

public class DeleteAppointmentCommand implements Command {
    private final AppoitmentService service;
    private final Appoitment appointmentToDelete;

    public DeleteAppointmentCommand(AppoitmentService service, Appoitment appointmentToDelete) {
        this.service = service;
        this.appointmentToDelete = appointmentToDelete;
    }

    @Override
    public void executeRedo() throws Exception {
        service.delete(appointmentToDelete.getId());
    }

    @Override
    public void executeUndo() throws Exception {
        service.create(appointmentToDelete);
    }
}
