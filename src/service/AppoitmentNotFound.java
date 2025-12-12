package service;

public class AppoitmentNotFound extends RuntimeException{
    public AppoitmentNotFound(Integer id) {
        super("Appoitment with id " + id + " not found");
    }
}
