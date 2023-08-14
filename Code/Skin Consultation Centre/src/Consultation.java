import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Consultation {
    private Doctor consultantDoctor;
    private Patient patient;
    private LocalDate date;
    private LocalTime time;
    private int cost;
    private String notes;

    public Consultation(Doctor consultantDoctor, Patient patient, LocalDate date, LocalTime time, int cost, String notes) {
       setConsultantDoctor(consultantDoctor);
       setPatient(patient);
       setDate(date);
       setTime(time);
       setCost(cost);
       setNotes(notes);
    }

    public Doctor getConsultantDoctor() {
        return consultantDoctor;
    }

    public void setConsultantDoctor(Doctor consultantDoctor) {
        this.consultantDoctor = consultantDoctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
