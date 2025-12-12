import domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.PatientRepository;
import repository.Repo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatientRepo {

    private Repo<Integer, Patient> repo;

    @BeforeEach
    void setUp() {
        repo = new PatientRepository();
    }

    @Test
    void testSaveAndFindById() {
        Patient p = new Patient(1, "Alice", "0711111111", "alice@mail.com");
        repo.save(p);

        Optional<Patient> found = repo.findById(1);
        assertTrue(found.isPresent());
        assertEquals(p, found.get());
    }

    @Test
    void testSaveOverwriteExisting() {
        Patient p1 = new Patient(1, "Alice", "0711111111", "alice@mail.com");
        Patient p2 = new Patient(1, "Alice Updated", "0722222222", "alice2@mail.com");

        repo.save(p1);
        Patient result = repo.save(p2);

        assertEquals(p2, result);
        Optional<Patient> found = repo.findById(1);
        assertEquals("Alice Updated", found.get().getName());
    }

    @Test
    void testFindAll() {
        Patient p1 = new Patient(1, "Alice", "0711111111", "alice@mail.com");
        Patient p2 = new Patient(2, "Bob", "0722222222", "bob@mail.com");

        repo.save(p1);
        repo.save(p2);

        List<Patient> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(p1));
        assertTrue(all.contains(p2));
    }

    @Test
    void testDeleteById() {
        Patient p = new Patient(1, "Alice", "0711111111", "alice@mail.com");
        repo.save(p);

        boolean deleted = repo.deleteById(1);
        assertTrue(deleted);
        assertFalse(repo.existsById(1));
    }

    @Test
    void testDeleteNonExisting() {
        boolean deleted = repo.deleteById(99);
        assertFalse(deleted);
    }

    @Test
    void testExistsById() {
        Patient p = new Patient(1, "Alice", "0711111111", "alice@mail.com");
        repo.save(p);

        assertTrue(repo.existsById(1));
        assertFalse(repo.existsById(2));
    }
}
