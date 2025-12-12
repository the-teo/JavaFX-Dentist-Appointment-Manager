import domain.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.PatientXmlRepo;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestPatientXml {

    private static final String FILE_NAME = "patients_test.xml";
    private PatientXmlRepo repo;

    @BeforeEach
    void setUp() {
        new File(FILE_NAME).delete();
        repo = new PatientXmlRepo(FILE_NAME);

        repo.save(new Patient(1, "Alice", "012345", "alice@example.com"));
        repo.save(new Patient(2, "Bob", "098765", "bob@example.com"));
    }

    @AfterEach
    void tearDown() {
        new File(FILE_NAME).delete();
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

    @Test
    void testPersistence() {
        // Save a patient and reload repo to check XML persistence
        Patient p4 = new Patient(4, "Daisy", "555666", "daisy@example.com");
        repo.save(p4);

        // Create a new instance of repo (simulate app restart)
        PatientXmlRepo newRepo = new PatientXmlRepo(FILE_NAME);
        assertEquals(3, newRepo.findAll().size()); // 3 patients in XML
        assertTrue(newRepo.existsById(4));
    }
}
