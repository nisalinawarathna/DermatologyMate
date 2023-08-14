import java.time.LocalDate;
import java.util.regex.Pattern;

public class Person {
    private String firstName;
    private String surName;
    private LocalDate dateOfBirth;
    private String mobileNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName.equals("") || !Pattern.matches("^[a-zA-Z]*$", firstName)) {
            throw new IllegalArgumentException("First name can only contain values from a-Z");
        }
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        if (surName.equals("") || !Pattern.matches("^[a-zA-Z]*$", surName)) {
            throw new IllegalArgumentException("Last name can only contain values from a-Z");
        }
        this.surName = surName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        if (mobileNumber.equals("") || !Pattern.matches("^[0-9]*$", mobileNumber)) {
            throw new IllegalArgumentException("Mobile Number can only contain values from 0-9");
        }
        this.mobileNumber = mobileNumber;
    }

    public Person(String firstName, String surName, LocalDate dateOfBirth, String mobileNumber) {
        setFirstName(firstName);
        setSurName(surName);
        setDateOfBirth(dateOfBirth);
        setMobileNumber(mobileNumber);
    }
}