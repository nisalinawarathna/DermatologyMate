import java.time.LocalDate;

public class Patient extends Person{
    private String patientId;

    public Patient(String firstName, String surName, LocalDate dateOfBirth, String mobileNumber, String patientID) {
        super(firstName, surName, dateOfBirth, mobileNumber);
        this.patientId = patientID;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
