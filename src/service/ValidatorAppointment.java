package service;

import domain.Appoitment;

public class ValidatorAppointment {
    public void validateAppointment(Appoitment a) {
        Integer id = a.getId(), patientId = a.getPatientId();
        String time = a.getTime(), date = a.getDate();

        if(id == null || id<=0)
        {
            throw new ValidationException("Invalid ID");
        }
        if(patientId == null || id<=0)
        {
            throw new ValidationException("Invalid Patient ID");
        }
        if(date == null) {
            throw new ValidationException("Invalid date");
        }
        if(time == null) {
            throw new ValidationException("Invalid time");
        }
    }
}
