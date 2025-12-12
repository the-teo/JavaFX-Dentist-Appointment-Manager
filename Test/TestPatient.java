import domain.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatient {

    @Test
    void testGettersAndSetters() {
        Patient p = new Patient(1, "Alice", "0711111111", "alice@mail.com");

        assertEquals(1, p.getId());
        assertEquals("Alice", p.getName());
        assertEquals("0711111111", p.getPhone());
        assertEquals("alice@mail.com", p.getEmail());

        p.setName("Alice Updated");
        p.setPhone("0722222222");
        p.setEmail("alice2@mail.com");

        assertEquals("Alice Updated", p.getName());
        assertEquals("0722222222", p.getPhone());
        assertEquals("alice2@mail.com", p.getEmail());
    }

    @Test
    void testToStringAndFromString() {
        Patient p = new Patient(1, "Alice", "0711111111", "alice@mail.com");
        String line = p.toString();

        assertEquals("1,Alice,0711111111,alice@mail.com", line);

        Patient parsed = Patient.fromString(line);
        assertEquals(p, parsed);
    }

    @Test
    void TestEqualsAndHashCode() {
        Patient p1 = new Patient(1,"Ana","07", "g");
        Patient p2 = new Patient(1,"Ana","07", "g");

        assertEquals(p1,p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

}
