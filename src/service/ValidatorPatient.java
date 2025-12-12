package service;

import domain.Patient;

public class ValidatorPatient {
    public void validatePatient(Patient p) {
        Integer id = p.getId();
        String name = p.getName(), phone = p.getPhone(), email = p.getEmail();

        if(id == null || id<=0)
        {
            throw new ValidationException("Invalid ID");
        }
        if(name == null || name.isEmpty())
        {
            throw new ValidationException("Invalid Name");
        }
        if(phone == null || phone.isEmpty())
        {
            throw new ValidationException("Invalid Phone");
        }
        if(email == null || email.isEmpty())
        {
            throw new ValidationException("Invalid Email");
        }
    }
}
