package com.example.hospitalmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;


public class AppointmentDAO implements Initializable {

    //Constructor is created which is non-parameterized and empty
    AppointmentDAO(){

    }
    @FXML private TextField appointmentId;
    @FXML private AnchorPane appointmentLoginForm;
    @FXML private AnchorPane appointmentCreateForm;
    @FXML private TextField patientId;
    @FXML private TextField doctorId;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> appointmentTimeComboBox;
    @FXML private ComboBox<String> appointmentStatusComboBox;
    @FXML private Label messageLabel;

    //Database connection tools
    private final Connection conn = DBConnection.getConnection();
    private PreparedStatement stmt;
    private ResultSet result;

    //Alert message object creation to apply in this class
    private final AlertMessage alertMessage = new AlertMessage();

    //Constructor is created which is parameterized but unassigned
    AppointmentDAO(int appointmentID, int doctorID, int patientID, Date appointmentDate, String appointmentTime, String status) {

    }

    //Method to show on the initialization of the frame or either program
    @FXML void initialize() {
        appointmentStatusComboBox.getItems().addAll("Scheduled", "Completed", "Cancelled", "In-Progress", "No-Show");
        appointmentTimeComboBox.getItems().addAll("09:00", "10:00", "11:00", "12:00", "14:00", "15:00");
    }

    //Method to create the appointment
    @FXML void createAppointment() {
        if (appointmentId.getText().isEmpty() || doctorId.getText().isEmpty() ||
                patientId.getText().isEmpty() || appointmentDatePicker.getValue() == null ||
                appointmentTimeComboBox.getValue() == null || appointmentStatusComboBox.getValue() == null) {

            alertMessage.errorMessage("Please fill all the fields.");
        }
        //Query to check the duplication of data
        String checkDuplicate = "SELECT * FROM appointment WHERE doctorID = ? AND appointmentDate = ? AND appointmentTime = ?";
        //Query to insert the data if no duplication occurs
        String insertData = "INSERT INTO appointment (doctorID, patientID, appointmentDate, appointmentTime, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement checkStmt = conn.prepareStatement(checkDuplicate);
             PreparedStatement insertStmt = conn.prepareStatement(insertData)) {

            // Check for duplicate appointment
            checkStmt.setString(1, doctorId.getText());
            checkStmt.setDate(2, java.sql.Date.valueOf(appointmentDatePicker.getValue()));
            checkStmt.setString(3, appointmentTimeComboBox.getValue());

            ResultSet result = checkStmt.executeQuery();
            if (result.next()) {
                alertMessage.errorMessage("This doctor already has an appointment at the selected time.");
            }

            // Insert new appointment
            insertStmt.setString(1, doctorId.getText());
            insertStmt.setString(2, patientId.getText());
            insertStmt.setDate(3, java.sql.Date.valueOf(appointmentDatePicker.getValue()));
            insertStmt.setString(4, appointmentTimeComboBox.getValue());
            insertStmt.setString(5, appointmentStatusComboBox.getValue());

            int rows = insertStmt.executeUpdate();

            if (rows > 0) {
                alertMessage.successMessage("Appointment created successfully!");
                if (appointmentLoginForm != null && appointmentCreateForm != null) {
                    appointmentLoginForm.setVisible(true);
                    appointmentCreateForm.setVisible(false);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Database error occurred while creating appointment.");
        }
    }

    //Method to update the appointment
    @FXML void updateAppointment() {
        //Checking the empty fields
        if (appointmentId.getText().isEmpty() || doctorId.getText().isEmpty() || patientId.getText().isEmpty() ||
                appointmentDatePicker.getValue() == null || appointmentTimeComboBox.getValue() == null ||
                appointmentStatusComboBox.getValue() == null) {
            alertMessage.errorMessage("Please fill all the fields.");
        }

        String updateQuery = "UPDATE appointment SET doctorID = ?, patientID = ?, appointmentDate = ?, appointmentTime = ?, status = ? WHERE appointmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, doctorId.getText());
            stmt.setString(2, patientId.getText());
            stmt.setDate(3, java.sql.Date.valueOf(appointmentDatePicker.getValue()));
            stmt.setString(4, appointmentTimeComboBox.getValue());
            stmt.setString(5, appointmentStatusComboBox.getValue());
            stmt.setString(6, appointmentId.getText());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                alertMessage.successMessage("Appointment updated successfully!");
            } else {
                alertMessage.errorMessage("Appointment ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Database error occurred while updating appointment.");
        }
    }


    @FXML private TableView<AppointmentDAO> appointmentTable;
    @FXML private TableColumn<AppointmentDAO, String> colId, colDoctor, colPatient, colDate, colTime, colStatus;
    private ObservableList<AppointmentDAO> appointmentList = FXCollections.observableArrayList();

    //Method to view all the appointments
    @FXML void viewAllAppointments() {
        appointmentList.clear();
        String query = "SELECT * FROM appointment";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                appointmentList.add(new AppointmentDAO(
                        rs.getInt("appointmentID"),
                        rs.getInt("doctorID"),
                        rs.getInt("patientID"),
                        rs.getDate("appointmentDate"),
                        rs.getString("appointmentTime"),
                        rs.getString("status")
                ));
            }

            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Error loading appointments.");
        }
    }

    //Method to delete the appointment
    @FXML void deleteAppointment() {
        if (appointmentId.getText().isEmpty()) {
            alertMessage.errorMessage("Please enter Appointment ID to delete.");
        }

        String deleteQuery = "DELETE FROM appointment WHERE appointmentID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setString(1, appointmentId.getText());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                alertMessage.successMessage("Appointment deleted successfully!");
            } else {
                alertMessage.errorMessage("Appointment ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Error deleting appointment.");
        }
    }

    //Method to update the appointment by doctor
    @FXML void doctorUpdateAppointment(){
        if (appointmentStatusComboBox.getValue() == null) {
            alertMessage.errorMessage("Please select the status.");
        }

        String updateQuery = "UPDATE appointment SET status = ? WHERE appointmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(5, appointmentStatusComboBox.getValue());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                alertMessage.successMessage("Appointment updated successfully!");
            } else {
                alertMessage.errorMessage("Appointment ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Database error occurred while updating appointment.");
        }
    }

    //Method to view the appointments of the doctor
    @FXML void viewDoctorAppointments() {
        appointmentList.clear();

        String query = "SELECT * FROM appointment WHERE doctorID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doctorId.getText()); // Assuming doctorId is a TextField

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointmentList.add(new AppointmentDAO(
                        rs.getInt("appointmentID"),
                        rs.getInt("doctorID"),
                        rs.getInt("patientID"),
                        rs.getDate("appointmentDate"),
                        rs.getString("appointmentTime"),
                        rs.getString("status")
                ));
            }

            appointmentTable.setItems(appointmentList);

        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Error loading appointments.");
        }
    }

    //Method to delete the appointment by the doctor
    @FXML void deleteDoctorAppointment() {
        String doctorIdText = doctorId.getText();
        String appointmentIdText = appointmentId.getText();

        if (doctorIdText.isEmpty() || appointmentIdText.isEmpty()) {
            alertMessage.errorMessage("Please enter both Appointment ID and Doctor ID.");
        }

        String query = "DELETE FROM appointment WHERE appointmentID = ? AND doctorID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(appointmentIdText));
            stmt.setInt(2, Integer.parseInt(doctorIdText));

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                alertMessage.successMessage("Appointment deleted successfully.");
                viewDoctorAppointments(); // Refresh table
            } else {
                alertMessage.errorMessage("No matching appointment found or you are not authorized to delete it.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alertMessage.errorMessage("Database error occurred while deleting appointment.");
        } catch (NumberFormatException e) {
            alertMessage.errorMessage("Invalid appointment or doctor ID.");
        }
    }


    //Method to initialize the objects on the initialization of program
    public void initialize(URL url, ResourceBundle resourceBundle){
        initialize();
    }
}
