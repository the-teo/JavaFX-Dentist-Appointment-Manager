package domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement(name = "Appointment")
@XmlAccessorType(XmlAccessType.FIELD)

public class Appoitment implements Id<Integer>, Serializable {
    private Integer id;
    private Integer patientId;
    private String date;
    private String time;

    private String status;
    private String description;

    public Appoitment() {}

    public Appoitment(Appoitment other) {
        this.id = other.id;
        this.patientId = other.patientId;
        this.date = other.date;
        this.time = other.time;
        this.status = other.status;
        this.description = other.description;
    }

    public Appoitment(Integer id, Integer patientId, String date, String time) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
    }

    public Appoitment(Integer id, Integer patientId, String date, String time, String status, String description) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.description = description;
    }

    public Integer getId() {return id;}
    public void setId(int id) {this.id = id;}
    public Integer getPatientId() {return patientId;}
    public void setPatientId(int id) {this.patientId = id;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getTime() { return time;}
    public void setTime(String time) { this.time = time;}
    public String getStatus() { return status;}
    public String getDescription() {return description;}
    public void setStatus(String status) {this.status = status;}
    public void setDescription(String description) {this.description = description;}

    @Override
    public String toString() {
        return id + "," + patientId + "," + date + "," + time + "," + status + "," + description;
    }

    public static Appoitment fromString(String line) {
        String[] parts = line.split(",");
        if(parts.length != 6) {
            throw new IllegalArgumentException("Invalid line format: expected 6 parts, got " + parts.length);
        }
        return new Appoitment(
                Integer.parseInt(parts[0]), // id
                Integer.parseInt(parts[1]), // patientId
                parts[2],                   // date
                parts[3],                   // time
                parts[4],                   // status
                parts[5]                    // description
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Appoitment that = (Appoitment) o;
        return Objects.equals(id, that.id) && Objects.equals(patientId, that.patientId) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(status, that.status) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, date, time, status, description);
    }
}


