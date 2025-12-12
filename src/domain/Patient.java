package domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement(name = "Patient")
@XmlAccessorType(XmlAccessType.FIELD)

public class Patient implements Id<Integer>, Serializable {
    private Integer id;
    private String name;
    private String phone;
    private String email;

    public Patient() {}

    public Patient(Patient other) {
        this.id = other.id;
        this.name = other.name;
        this.phone = other.phone;
        this.email = other.email;
     }


    public Patient(Integer id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return  id + "," + name + "," + phone + "," + email;
    }

    public static Patient fromString(String line) {
        String[] fields = line.split(",");
        if(fields.length != 4) {
            throw new IllegalArgumentException("Invalid line format");
        }
        return new Patient(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3]);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id) &&
                Objects.equals(name, patient.name) &&
                Objects.equals(phone, patient.phone) &&
                Objects.equals(email, patient.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, email);
    }
}








