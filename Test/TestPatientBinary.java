import domain.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.PatientBinaryFileRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatientBinary {

    private static final String file = "test_patients.bin";
    private PatientBinaryFileRepo repo;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Path.of(file));
        repo = new PatientBinaryFileRepo(file);

        repo.save(new Patient(1, "John Doe", "073212125", "gmail"));
        repo.save(new Patient(2, "Jane Smith", "071232130", "gmail"));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(file));
    }

    @Test
    void testLoadDataFromExistingFile() {
        PatientBinaryFileRepo loadedRepo = new PatientBinaryFileRepo(file);
        assertEquals(2, loadedRepo.findAll().size());
        assertTrue(loadedRepo.existsById(1));
        assertTrue(loadedRepo.existsById(2));
    }

    @Test
    void testSaveAddsAndPersistsPatient() {
        Patient newPatient = new Patient(3, "Alice", "07231123", "gmail");
        repo.save(newPatient);

        assertTrue(repo.existsById(3));

        PatientBinaryFileRepo reloaded = new PatientBinaryFileRepo(file);
        assertTrue(reloaded.existsById(3));
        assertEquals("Alice", reloaded.findById(3).get().getName());
    }

    @Test
    void testDeletePatient() {
        boolean deleted = repo.deleteById(1);
        assertTrue(deleted);
        assertFalse(repo.existsById(1));

        PatientBinaryFileRepo reloaded = new PatientBinaryFileRepo(file);
        assertFalse(reloaded.existsById(1));
        assertEquals(1, reloaded.findAll().size());
    }

    @Test
    void testFindById() {
        Optional<Patient> patient = repo.findById(2);
        assertTrue(patient.isPresent());
        assertEquals("Jane Smith", patient.get().getName());
    }

    @Test
    void testFindAll() {
        List<Patient> patients = repo.findAll();
        assertEquals(2, patients.size());
    }

    @Test
    void testExistsById() {
        assertTrue(repo.existsById(1));
        assertFalse(repo.existsById(99));
    }

    @Test
    void testLoadDataFromNonexistentFile() throws IOException {
        Files.deleteIfExists(Path.of(file));
        PatientBinaryFileRepo emptyRepo = new PatientBinaryFileRepo(file);
        assertTrue(emptyRepo.findAll().isEmpty());
    }
}
