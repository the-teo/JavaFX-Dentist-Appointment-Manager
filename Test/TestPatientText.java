import domain.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.PatientTextFileRepo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatientText {

    private static final String TEST_FILE = "test_patients.txt";
    private PatientTextFileRepo repo;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
        Files.createFile(Path.of(TEST_FILE));

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(TEST_FILE))) {
            writer.write("1,John Doe,25,M\n");
            writer.write("2,Jane Smith,30,F\n");
        }

        repo = new PatientTextFileRepo(TEST_FILE);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    void testLoadData() {
        List<Patient> patients = repo.findAll();
        assertEquals(2, patients.size());
        assertTrue(repo.existsById(1));
        assertTrue(repo.existsById(2));
    }

    @Test
    void testSaveNewPatient() throws IOException {
        Patient newPatient = new Patient(3, "Alice", "072132328", "F");
        repo.save(newPatient);

        assertTrue(repo.existsById(3));

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(3, lines.size());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Alice")));
    }

    @Test
    void testDeletePatient() throws IOException {
        boolean deleted = repo.deleteById(1);
        assertTrue(deleted);
        assertFalse(repo.existsById(1));

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(1, lines.size());
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
    void testLoadDataFromEmptyFile() throws IOException {
        Files.writeString(Path.of(TEST_FILE), "");
        PatientTextFileRepo emptyRepo = new PatientTextFileRepo(TEST_FILE);
        assertTrue(emptyRepo.findAll().isEmpty());
    }
}
