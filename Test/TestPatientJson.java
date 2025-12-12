import domain.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.PatientJSONFileRepo;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestPatientJson {

    private static final String FILE_NAME = "patients_test.json";
    private PatientJSONFileRepo repo;

    @BeforeEach
    void setUp() {
        // Delete the test file to start fresh
        new File(FILE_NAME).delete();
        repo = new PatientJSONFileRepo(FILE_NAME);

        // Initialize with some patients
        repo.save(new Patient(1, "Alice", "012345", "alice@example.com"));
        repo.save(new Patient(2, "Bob", "098765", "bob@example.com"));
    }

    @AfterEach
    void tearDown() {
        new File(FILE_NAME).delete(); // Clean up after test
    }

    @Test
    void testSaveAndFind() {
        Patient p3 = new Patient(3, "Charlie", "111222", "charlie@example.com");
        repo.save(p3);

        assertEquals(p3, repo.findById(3).orElse(null));
        assertEquals(3, repo.findAll().size());
    }

    @Test
    void testDelete() {
        assertTrue(repo.deleteById(1));
        assertFalse(repo.existsById(1));
        assertEquals(1, repo.findAll().size());
    }
}
