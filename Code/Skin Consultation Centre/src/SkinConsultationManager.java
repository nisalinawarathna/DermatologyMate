import java.util.ArrayList;

public interface SkinConsultationManager {
    void addDoctor(ArrayList<Doctor> doctorArrayList);
    void deleteDoctor(ArrayList<Doctor> doctorArrayList);
    void showAllDoctors(ArrayList<Doctor> doctorArrayList);
    void saveInformation(ArrayList<Doctor> doctorArrayList);
}
