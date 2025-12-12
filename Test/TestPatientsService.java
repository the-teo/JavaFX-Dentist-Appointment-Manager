import domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.PatientRepository;
import repository.Repo;
import service.PatientNotFound;
import service.PatientService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatientsService{

    private PatientService service;
    private Repo<Integer, Patient> repo;

    @BeforeEach
    public void setUp() {
        repo = new PatientRepository(); // in-memory repo
        service = new PatientService(repo);
    }

    @Test
    public void testCreatePatient() {
        Patient p = new Patient(1, "Ana", "0711111111", "ana@example.com");
        service.create(p);

        List<Patient> patients = service.getAll();
        assertEquals(1, patients.size());
        assertEquals("Ana", patients.get(0).getName());
    }

    @Test
    public void testCreateDuplicateThrows() {
        Patient p = new Patient(1, "Ana", "0711111111", "ana@example.com");
        service.create(p);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.create(new Patient(1, "Ion", "0722222222", "ion@example.com"))
        );
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testUpdatePatient() {
        service.create(new Patient(1, "Ana", "0711111111", "ana@example.com"));

        Patient updated = new Patient(1, "Ana Maria", "0799999999", "anamaria@example.com");
        service.update(updated);

        Patient result = service.getById(1);
        assertEquals("Ana Maria", result.getName());
        assertEquals("0799999999", result.getPhone());
        assertEquals("anamaria@example.com", result.getEmail());
    }

    @Test
    public void testUpdateNonExistingThrows() {
        Patient p = new Patient(1, "Ana", "0711111111", "ana@example.com");
        assertThrows(PatientNotFound.class, () -> service.update(p));
    }

    @Test
    public void testGetById() {
        service.create(new Patient(1, "Ana", "0711111111", "ana@example.com"));
        Patient result = service.getById(1);
        assertEquals("Ana", result.getName());
    }

    @Test
    public void testGetByIdNotFoundThrows() {
        assertThrows(PatientNotFound.class, () -> service.getById(100));
    }

    @Test
    public void testDeletePatient() {
        service.create(new Patient(1, "Ana", "0711111111", "ana@example.com"));
        service.delete(1);

        assertEquals(0, service.getAll().size());
        assertFalse(service.patientExists(1));
    }

    @Test
    public void testDeleteNonExistingThrows() {
        assertThrows(PatientNotFound.class, () -> service.delete(99));
    }

    @Test
    public void testGetAll() {
        service.create(new Patient(1, "Ana", "0711111111", "ana@example.com"));
        service.create(new Patient(2, "Ion", "0722222222", "ion@example.com"));

        List<Patient> all = service.getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(p -> p.getName().equals("Ana")));
        assertTrue(all.stream().anyMatch(p -> p.getName().equals("Ion")));
    }

    @Test
    void testFilterByName_foundMatches() {
        repo.save(new Patient(3, "Maria", "0712345678", "maria@mail.com"));
        List<Patient> result = service.filterByName("Maria");
        assertEquals(1, result.size());
        assertEquals("Maria", result.get(0).getName());
    }

    @Test
    void testFilterByPhone() {
        List<Patient> result = service.filterByPhone("07");
        List<Patient> all =service.getAll();
        assertEquals(all.size(), result.size());
    }
}
