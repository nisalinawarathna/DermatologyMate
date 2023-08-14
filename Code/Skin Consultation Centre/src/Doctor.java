import java.time.LocalDate;

public class Doctor extends Person{
    private String medicalLicenseNumber;
    private String specialisation;

    public Doctor(String firstName, String surName, LocalDate dateOfBirth, String mobileNumber, String medicalLicenseNumber, String specialisation) {
        super(firstName, surName, dateOfBirth, mobileNumber);
        setMedicalLicenseNumber(medicalLicenseNumber);
        setSpecialisation(specialisation);
    }

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }


}
