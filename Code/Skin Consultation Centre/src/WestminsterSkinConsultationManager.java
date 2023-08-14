import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

public class WestminsterSkinConsultationManager implements SkinConsultationManager {

    static WestminsterSkinConsultationManager westminsterSkinConsultationManager = new WestminsterSkinConsultationManager();

    // a static scanner instance to use for userInput
    static Scanner scanner = new Scanner(System.in);
    static int maximumNumberOfDoctorsAllowed = 10;
    //ArrayList to store all doctors in the system
    ArrayList<Doctor> doctorArrayList = new ArrayList<>();
    ArrayList<Consultation> consultationArrayList = new ArrayList<>();

    static int nextPatientID = 0;

    @Override
    public void addDoctor(ArrayList<Doctor> doctorArrayList) {
        boolean isValidInput = false;

        String doctorFirstName;
        LocalDate date;
        String doctorLastName;
        String mobileNumber;
        String medicalLicenseNumber;
        String specialisation;

        while (!isValidInput) {
            //get all parameters necessary for the Doctor constructor
            System.out.print("Enter First Name: ");
            doctorFirstName = scanner.nextLine();

            System.out.print("Enter Last Name: ");
            doctorLastName = scanner.nextLine();

            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            String dob = scanner.nextLine();
            try {
                date = LocalDate.parse(dob);
                isValidInput = true;
            } catch (Exception e) {
                System.out.println("Input is not a valid date.");
                continue;
            }

            System.out.print("Enter mobile number: ");
            mobileNumber = scanner.nextLine();

            System.out.print("Enter medical license number: ");
            medicalLicenseNumber = scanner.nextLine();

            //check if the given licenseNumber already exists in the list
            for (Doctor doctor : doctorArrayList) {
                if (doctor.getMedicalLicenseNumber().equals(medicalLicenseNumber)) {
                    System.out.println("\nThis medical license number already exists in the system. Try again\n");
                    isValidInput = false;
                    break;
                }
            }

            if (!isValidInput) {
                continue;
            }

            System.out.print("Enter area of specialisation: ");
            specialisation = scanner.nextLine();

            //wrapped in try catch in case there are any invalid inputs
            try {
                Doctor addThisDoctor = new Doctor(doctorFirstName, doctorLastName, date, mobileNumber, medicalLicenseNumber, specialisation);
                doctorArrayList.add(addThisDoctor);
            } catch (IllegalArgumentException e) {
                System.out.println("\n------------- Error while adding doctor --------- ");
                System.out.println(e.getMessage());
                isValidInput = false;
            }
        }

        System.out.println("\n------------- Doctor Added Successfully ----------------");
    }

    @Override
    public void deleteDoctor(ArrayList<Doctor> doctorArrayList) {
        //get medicalLicense number
        System.out.print("Enter medical license number of doctor that needs to be removed: ");
        String licenseNumber = scanner.nextLine();

        boolean foundElement = false;

        for (Doctor doctor : doctorArrayList) {
            if (doctor.getMedicalLicenseNumber().equals(licenseNumber)){
                doctorArrayList.remove(doctor);
                foundElement = true;
                System.out.println("\n------------- Doctor Removed Successfully ----------------");
                System.out.println("\t" + doctor.getFirstName() +" " + doctor.getSurName());
                System.out.println("\t" + "Date of Birth: " + doctor.getDateOfBirth());
                System.out.println("\t" + "Medical License Number: " + doctor.getMedicalLicenseNumber());
                System.out.println("\t" + "Specialisation: " + doctor.getSpecialisation());
                System.out.println("\t" + "Mobile Number: " + doctor.getMobileNumber());

                System.out.println("\n" + "Number of doctors remaining in the system: " + doctorArrayList.size());
                break;
            }
        }

        if (!foundElement) System.out.println("The medical license number " + licenseNumber + " does not exist");
    }

    @Override
    public void showAllDoctors(ArrayList<Doctor> doctorArrayList) {
        doctorArrayList.sort(new LastNameComparator());

        int i = 1;
        for (Doctor doctor: doctorArrayList) {
            System.out.println("\n" + i + ". " + doctor.getFirstName() +" " + doctor.getSurName());
            System.out.println("\t" + "Date of Birth: " + doctor.getDateOfBirth());
            System.out.println("\t" + "Medical License Number: " + doctor.getMedicalLicenseNumber());
            System.out.println("\t" + "Specialisation: " + doctor.getSpecialisation());
            System.out.println("\t" + "Mobile Number: " + doctor.getMobileNumber());
            i++;
        }
    }

    @Override
    public void saveInformation(ArrayList<Doctor> doctorArrayList) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            for (Doctor doctor : doctorArrayList) {
                writer.write(doctor.getFirstName() + "," + doctor.getSurName() + "," + doctor.getDateOfBirth() + "," + doctor.getMobileNumber()  + "," + doctor.getMedicalLicenseNumber()  + "," + doctor.getSpecialisation() + "\n");
            }
            writer.close();
            saveConsultationInfo();
            System.out.println("Data saved to file successfully");
        } catch (IOException e) {
            System.out.println("\nError while saving data: " + e);
        }
    }

    private static void saveConsultationInfo(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output2.txt"));
            for (Consultation consultation : westminsterSkinConsultationManager.consultationArrayList) {
                writer.write(consultation.getConsultantDoctor().getFirstName() + "," +
                        consultation.getConsultantDoctor().getSurName() + "," +
                        consultation.getConsultantDoctor().getDateOfBirth() + ","
                        + consultation.getConsultantDoctor().getMobileNumber()  + ","
                        + consultation.getConsultantDoctor().getMedicalLicenseNumber()  + ","
                        + consultation.getConsultantDoctor().getSpecialisation() + ","
                        + consultation.getPatient().getFirstName() + ","
                        + consultation.getPatient().getSurName() + ","
                        + consultation.getPatient().getDateOfBirth() + ","
                        + consultation.getPatient().getMobileNumber() + ","
                        + consultation.getPatient().getPatientId() + ","
                        + consultation.getDate() + ","
                        + consultation.getTime() + ","
                        + consultation.getCost() + ","
                        + consultation.getNotes() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("\nError while saving consultation data: " + e);
        }

    }

    private static int displayMainMenu() {
        int userChoice = 0;

        while (userChoice == 0) {
            System.out.println();
            System.out.println("----------------------Welcome to the Skin Consultation center----------------------");
            System.out.println("\t1. Add new doctor");
            System.out.println("\t2. Delete a doctor");
            System.out.println("\t3. Show All doctors");
            System.out.println("\t4. Save information to file");
            System.out.println("\t5. Launch GUI");
            System.out.println("\t6. Exit");

            System.out.println();
            System.out.print("Enter required function: ");
            String userInput = scanner.nextLine();

            try {
                userChoice = Integer.parseInt(userInput);
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter an integer from 1-4");
            } finally {
                if (userChoice == 0 || userChoice > 6) {
                    System.out.println("Please enter a value between 1-4");
                    userChoice = 0;
                }
            }
        }

        return userChoice;
    }

    private static ArrayList<Doctor> loadDoctorInformationFromFile() {
        ArrayList<Doctor> doctorsFromTextFile = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Doctor doctor = new Doctor(parts[0], parts[1], LocalDate.parse(parts[2]), parts[3], parts[4], parts[5]);
                doctorsFromTextFile.add(doctor);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorsFromTextFile;
    }

    private static ArrayList<Consultation> loadConsultationInfo() {
        ArrayList<Consultation> consultationsFromTextFile = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("output2.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Consultation consultation = new Consultation(new Doctor(parts[0], parts[1],LocalDate.parse(parts[2]), parts[3], parts[4], parts[5]),
                        new Patient(parts[6], parts[7], LocalDate.parse(parts[8]), parts[9], parts[10]),
                        LocalDate.parse(parts[11]),
                        LocalTime.parse(parts[12]),
                        Integer.parseInt(parts[13]),
                        parts[14]);
                consultationsFromTextFile.add(consultation);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultationsFromTextFile;
    }

    private void launchGUI(ArrayList<Doctor> doctorArrayList, ArrayList<Consultation> consultationArrayList) {
        // create the doctorTable dModel
        DefaultTableModel dModel = new DefaultTableModel();
        dModel.addColumn("First Name");
        dModel.addColumn("Last Name");
        dModel.addColumn("Date of birth");
        dModel.addColumn("Mobile Number");
        dModel.addColumn("License Number");
        dModel.addColumn("Specialisation");
        for (Doctor doctor : doctorArrayList) {
            dModel.addRow(new Object[]{doctor.getFirstName(), doctor.getSurName(), doctor.getDateOfBirth(), doctor.getMobileNumber(), doctor.getMedicalLicenseNumber(), doctor.getSpecialisation()});
        }

        DefaultTableModel cModel = new DefaultTableModel();
        cModel.addColumn("Consultant Name");
        cModel.addColumn("Patient Name");
        cModel.addColumn("Consultation Date");
        cModel.addColumn("Consultation Time");
        cModel.addColumn("Patient ID");
        cModel.addColumn("Patient Notes");
        cModel.addColumn("Cost Per Hour");
        for (Consultation consultation : consultationArrayList) {
            cModel.addRow(new Object[]{(consultation.getConsultantDoctor().getFirstName() + " " + consultation.getConsultantDoctor().getSurName()), (consultation.getPatient().getFirstName() + " " + consultation.getPatient().getSurName()) , consultation.getDate(), consultation.getTime(), consultation.getPatient().getPatientId(), consultation.getNotes(), consultation.getCost()});
        }

        DefaultTableModel pModel = new DefaultTableModel();
        pModel.addColumn("Patient ID");
        pModel.addColumn("Patient First Name");
        pModel.addColumn("Patient Last Name");
        pModel.addColumn("Patient Date of Birth");
        pModel.addColumn("Patient Mobile Number");
        for (Consultation consultation : consultationArrayList) {
            pModel.addRow(new Object[]{consultation.getPatient().getPatientId(), consultation.getPatient().getFirstName(), consultation.getPatient().getSurName(), consultation.getPatient().getDateOfBirth(), consultation.getPatient().getMobileNumber()});
        }

        // create the doctorTable & consultation table
        JTable doctorTable = new JTable(dModel);
        JTable consultationTable = new JTable(cModel);
        JTable patientTable = new JTable(pModel);

        // create the refresh button and add an action listener
        JButton button = new JButton("Sort doctors alphabetically");
        button.setPreferredSize(new Dimension(150,50));

        JButton button2 = new JButton("Add Consultation");
        button.setPreferredSize(new Dimension(100,50));

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel comboFieldLabel = new JLabel("Select Doctor");
                //dropdown list to show all doctors
                JComboBox<String> comboBox = new JComboBox<>();
                for (Doctor doctor: doctorArrayList) {
                    comboBox.addItem(doctor.getFirstName() + " " + doctor.getSurName());
                }
                comboBox.setMaximumSize(new Dimension(200, 50));

                JLabel dateFieldLabel = new JLabel("Enter consultation date (yyyy-MM-dd)");
                JTextField dateTextField = new JTextField("Date Field");
                dateTextField.setMaximumSize(new Dimension(150,50));

                JLabel timeFieldLabel = new JLabel("Enter consultation time (HH:MM)");
                JTextField timeTextField = new JTextField("");
                timeTextField.setMaximumSize(new Dimension(150,50));

                JLabel pNameFieldLabel = new JLabel("Enter patient name");
                JTextField pNameTextField = new JTextField("");
                pNameTextField.setMaximumSize(new Dimension(150,50));

                JLabel pSNameFieldLabel = new JLabel("Enter patient surname");
                JTextField pSNameTextField = new JTextField("");
                pSNameTextField.setMaximumSize(new Dimension(150,50));

                JLabel dobFieldLabel = new JLabel("Enter patient date of birth (yyyy-MM-dd)");
                JTextField dobTextField = new JTextField("");
                dobTextField.setMaximumSize(new Dimension(150,50));

                JLabel contactFieldLabel = new JLabel("Enter patient mobile number");
                JTextField contactTextField = new JTextField("");
                contactTextField.setMaximumSize(new Dimension(150,50));

                JLabel pIDFieldLabel = new JLabel("Enter patient ID (leave empty if inapplicable)");
                JTextField pIDTextField = new JTextField("Time TextField");
                pIDFieldLabel.setMaximumSize(new Dimension(150,50));

                JLabel notesFieldLabel = new JLabel("Enter notes");
                JTextField notesTextField = new JTextField("Time TextField");
                notesFieldLabel.setMaximumSize(new Dimension(150,50));

                JLabel createConsultationLabel = new JLabel("Create Consultation");
                JButton createConsultationButton = new JButton("Submit");
                createConsultationButton.setMaximumSize(new Dimension(150,50));

                JFrame consultationFrame = new JFrame("Add Consultation");
                consultationFrame.setSize(550,400);
                consultationFrame.setVisible(true);

                JPanel consultationPanel = new JPanel();
                consultationPanel.setLayout(new GridLayout(10,2, 0, 10));

                consultationPanel.add(comboFieldLabel);
                consultationPanel.add(comboBox);

                consultationPanel.add(dateFieldLabel);
                consultationPanel.add(dateTextField);

                consultationPanel.add(timeFieldLabel);
                consultationPanel.add(timeTextField);

                consultationPanel.add(pNameFieldLabel);
                consultationPanel.add(pNameTextField);

                consultationPanel.add(pSNameFieldLabel);
                consultationPanel.add(pSNameTextField);

                consultationPanel.add(dobFieldLabel);
                consultationPanel.add(dobTextField);

                consultationPanel.add(contactFieldLabel);
                consultationPanel.add(contactTextField);

                consultationPanel.add(pIDFieldLabel);
                consultationPanel.add(pIDTextField);

                consultationPanel.add(notesFieldLabel);
                consultationPanel.add(notesTextField);

                consultationPanel.add(createConsultationLabel);
                consultationPanel.add(createConsultationButton);

                consultationFrame.add(consultationPanel);

                createConsultationButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //get values from all the input fields and validate
                        String[] selectedDoctor = ((String) comboBox.getSelectedItem()).split(" ");
                        String doctorFName = selectedDoctor[0];
                        String doctorSName = selectedDoctor[1];

                        String inputDate = dateTextField.getText();
                        LocalDate consultationdate = null;
                        try {
                            consultationdate = LocalDate.parse(inputDate);
                        } catch (Exception er) {
                            JOptionPane.showMessageDialog(null, "Invalid consultation Date input", "Invalid user input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String inputTime = timeTextField.getText();
                        LocalTime consultationTime = null;

                        try {
                            consultationTime = LocalTime.parse(inputTime);
                        } catch (Exception er) {
                            JOptionPane.showMessageDialog(null, "Invalid consultation Time input", "Invalid user input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String inputDob = dobTextField.getText();
                        LocalDate patientDob = null;
                        try {
                            patientDob = LocalDate.parse(inputDob);
                        } catch (Exception er) {
                            JOptionPane.showMessageDialog(null, "Invalid patient Date of birth input", "Invalid user input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String inputPatientId = pIDTextField.getText();
                        int cost = 15;
                        Patient addThisPatient = null;
                        for (Consultation consultation: consultationArrayList) {
                            if (consultation.getPatient().getPatientId().equals(inputPatientId)){
                                JOptionPane.showMessageDialog(null, "This Patient Already Exists in the system. Cost will be £25 per hour", "Patient Already Exists", JOptionPane.INFORMATION_MESSAGE);

                                //get patient object if ID exists
                                addThisPatient = Objects.requireNonNull(consultationArrayList.stream()
                                        .filter(p -> p.getPatient().getPatientId().equals(inputPatientId))
                                        .findFirst()
                                        .orElse(null)).getPatient();

                                cost = 25;
                                break;
                            } else {
                                JOptionPane.showMessageDialog(null, "This Patient does not exist in the system. Cost will be £15 per hour", "Patient Does Not Exist", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }

                        String notes = notesTextField.getText();

                        if (notes.equals("")){
                            notes = "-";
                        }

                        //Run this block only if the inputs are valid
                        try {
                            //get doctor object from first and last name
                            Doctor consultantDoctor = doctorArrayList.stream()
                                    .filter(p -> p.getFirstName().equals(doctorFName))
                                    .filter(p -> p.getSurName().equals(doctorSName))
                                    .findFirst()
                                    .orElse(null);

                            Consultation addThisConsultation;
                            if(addThisPatient == null){
                                addThisConsultation = new Consultation(consultantDoctor, new Patient(pNameTextField.getText(), pSNameTextField.getText(), patientDob, contactTextField.getText(), Integer.toString(++nextPatientID)), consultationdate, consultationTime, cost, notes);
                            } else {
                                addThisConsultation = new Consultation(consultantDoctor, addThisPatient, consultationdate, consultationTime, cost, notes);
                            }
                            consultationArrayList.add(addThisConsultation);

                            cModel.setRowCount(0);
                            for (Consultation consultation : consultationArrayList) {
                                cModel.addRow(new Object[]{(consultation.getConsultantDoctor().getFirstName() + " " + consultation.getConsultantDoctor().getSurName()), (consultation.getPatient().getFirstName() + " " + consultation.getPatient().getSurName()) , consultation.getDate(), consultation.getTime(), consultation.getPatient().getPatientId(), consultation.getNotes(), consultation.getCost()});
                            }
                            // refresh the doctorTable
                            consultationTable.revalidate();
                            consultationTable.repaint();

                            pModel.setRowCount(0);
                            for (Consultation consultation : consultationArrayList) {
                                pModel.addRow(new Object[]{consultation.getPatient().getPatientId(), consultation.getPatient().getFirstName(), consultation.getPatient().getSurName(), consultation.getPatient().getDateOfBirth(), consultation.getPatient().getMobileNumber()});
                            }
                            // refresh the doctorTable
                            patientTable.revalidate();
                            patientTable.repaint();
                        } catch (IllegalArgumentException er) {
                            JOptionPane.showMessageDialog(null, er.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        });

        JLabel label = new JLabel("Doctors available in the system");

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call the sorting method
                westminsterSkinConsultationManager.showAllDoctors(doctorArrayList);
                // update the doctorTable dModel
                dModel.setRowCount(0);
                for (Doctor doctor : doctorArrayList) {
                    dModel.addRow(new Object[]{doctor.getFirstName(), doctor.getSurName(), doctor.getDateOfBirth(), doctor.getMobileNumber(), doctor.getMedicalLicenseNumber(), doctor.getSpecialisation()});
                }
                // refresh the doctorTable
                doctorTable.revalidate();
                doctorTable.repaint();
            }
        });

        // create the frame and add the doctorTable and button to it
        JFrame frame = new JFrame("Skin Consultation Center");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 0));
        panel.add(label);
        panel.add(button);
        panel.add(button2);

        frame.add(panel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

        tablePanel.add(new JScrollPane(doctorTable));
        tablePanel.add(new JScrollPane(consultationTable));
        tablePanel.add(new JScrollPane(patientTable));

        frame.add(tablePanel, BorderLayout.CENTER);

        frame.setSize(700, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        File file = new File("output.txt");
        if (file.exists()) {
            System.out.println("Load file found");
            westminsterSkinConsultationManager.doctorArrayList = loadDoctorInformationFromFile();
            westminsterSkinConsultationManager.consultationArrayList = loadConsultationInfo();
        } else {
            System.out.println("Load file not found");
            //throw an error if the user tries to add more than 10 elements to the arrayList
            westminsterSkinConsultationManager.doctorArrayList.ensureCapacity(maximumNumberOfDoctorsAllowed);
        }


        while (true) {
            int userInput = displayMainMenu();

            switch (userInput) {
                case 1 -> westminsterSkinConsultationManager.addDoctor(westminsterSkinConsultationManager.doctorArrayList);
                case 2 -> westminsterSkinConsultationManager.deleteDoctor(westminsterSkinConsultationManager.doctorArrayList);
                case 3 -> westminsterSkinConsultationManager.showAllDoctors(westminsterSkinConsultationManager.doctorArrayList);
                case 4 -> westminsterSkinConsultationManager.saveInformation(westminsterSkinConsultationManager.doctorArrayList);
                case 5 -> westminsterSkinConsultationManager.launchGUI(westminsterSkinConsultationManager.doctorArrayList, westminsterSkinConsultationManager.consultationArrayList);
                case 6 -> System.exit(0);
            }
        }
    }
}

//A comparator class which will compare the last names
class LastNameComparator implements Comparator<Doctor> {

    @Override
    public int compare(Doctor o1, Doctor o2) {
        return o1.getSurName().compareTo(o2.getSurName());
    }
}
