package com.example.hospitalmanagementsystem;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class DoctorDAO implements Initializable {

    //All components required
    @FXML private AnchorPane main_form;
    @FXML private Circle top_profile;
    @FXML private Label top_username;
    @FXML private Button logout_btn;
    @FXML private Label date_time;
    @FXML private Label current_form;
    @FXML private Label nav_adminID;
    @FXML private Label nav_username;
    @FXML private Button dashboard_btn;
    @FXML private Button patients_btn;
    @FXML private Button appointments_btn;
    @FXML private Button profile_btn;
    @FXML private AnchorPane dashboard_form;
    @FXML private Label dashboard_IP;
    @FXML private Label dashboard_TP;
    @FXML private Label dashboard_AP;
    @FXML private Label dashboard_tA;
    @FXML private AreaChart<?, ?> dashboad_chart_PD;
    @FXML private BarChart<?, ?> dashboad_chart_DD;
    @FXML private TableView<AppointmentData> dashboad_tableView;
    @FXML private TableColumn<AppointmentData, String> dashboad_col_appointmentID;
    @FXML private TableColumn<AppointmentData, String> dashboad_col_name;
    @FXML private TableColumn<AppointmentData, String> dashboad_col_description;
    @FXML private TableColumn<AppointmentData, String> dashboad_col_appointmentDate;
    @FXML private TableColumn<AppointmentData, String> dashboad_col_status;
    @FXML private AnchorPane patients_form;
    @FXML private TextField patients_patientID;
    @FXML private TextField patients_patientName;
    @FXML private TextField patients_mobileNumber;
    @FXML private TextField patients_password;
    @FXML private TextArea patients_address;
    @FXML private Button patients_confirmBtn;
    @FXML private Label patients_PA_patientID;
    @FXML private Label patients_PA_password;
    @FXML private Label patients_PA_dateCreated;
    @FXML private Label patients_PI_patientName;
    @FXML private Label patients_PI_gender;
    @FXML private Label patients_PI_mobileNumber;
    @FXML private Label patients_PI_address;
    @FXML private Button patients_PI_addBtn;
    @FXML private Button patients_PI_recordBtn;
    @FXML private AnchorPane appointments_form;
    @FXML private TableView<AppointmentData> appointments_tableView;
    @FXML private TableColumn<AppointmentData, String> appointments_col_appointmentID;
    @FXML private TableColumn<AppointmentData, String> appointments_col_name;
    @FXML private TableColumn<AppointmentData, String> appointments_col_gender;
    @FXML private TableColumn<AppointmentData, String> appointments_col_contactNumber;
    @FXML private TableColumn<AppointmentData, String> appointments_col_description;
    @FXML private TableColumn<AppointmentData, String> appointments_col_date;
    @FXML private TableColumn<AppointmentData, String> appointments_col_dateModify;
    @FXML private TableColumn<AppointmentData, String> appointments_col_dateDelete;
    @FXML private TableColumn<AppointmentData, String> appointments_col_status;
    @FXML private TableColumn<AppointmentData, String> appointments_col_action;
    @FXML private TextField appointment_appointmentID;
    @FXML private TextField appointment_name;
    @FXML private ComboBox<String> appointment_gender;
    @FXML private TextField appointment_description;
    @FXML private TextField appointment_diagnosis;
    @FXML private TextField appointment_treatment;
    @FXML private TextField appointment_mobileNumber;
    @FXML private TextArea appointment_address;
    @FXML private ComboBox<String> appointment_status;
    @FXML private DatePicker appointment_schedule;
    @FXML private Button appointment_insertBtn;
    @FXML private Button appointment_updateBtn;
    @FXML private Button appointment_clearBtn;
    @FXML private Button appointment_deleteBtn;
    @FXML private ComboBox<String> patients_gender;
    @FXML private AnchorPane profile_form;
    @FXML private Circle profile_circleImage;
    @FXML private Button profile_importBtn;
    @FXML private Label profile_label_doctorID;
    @FXML private Label profile_label_name;
    @FXML private Label profile_label_email;
    @FXML private Label profile_label_dateCreated;
    @FXML private TextField profile_doctorID;
    @FXML private TextField profile_name;
    @FXML private TextField profile_email;
    @FXML private ComboBox<String> profile_gender;
    @FXML private TextField profile_mobileNumber;
    @FXML private TextArea profile_address;
    @FXML private ComboBox<String> profile_specialized;
    @FXML private ComboBox<String> profile_status;
    @FXML private Button profile_updateBtn;

    //    Database connection tools
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    //Image required for profile
    private Image image;

    //Alert message object created
    private final AlertMessage alert = new AlertMessage();

    //Method to display the inactive patient
    @FXML public void dashbboardDisplayIP() {
        String sql = "SELECT COUNT(id) FROM patient WHERE status = 'Inactive' AND doctor = '"
                + Data.doctor_id + "'";
        connect = DBConnection.getConnection();
        int getIP = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                getIP = result.getInt("COUNT(id)");
            }
            dashboard_IP.setText(String.valueOf(getIP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to display the total patients
    @FXML public void dashbboardDisplayTP() {
        String sql = "SELECT COUNT(id) FROM patient WHERE doctor = '" + Data.doctor_id + "'";
        connect = DBConnection.getConnection();
        int getTP = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                getTP = result.getInt("COUNT(id)");
            }
            dashboard_TP.setText(String.valueOf(getTP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to display the active patient
    public void dashbboardDisplayAP() {
        String sql = "SELECT COUNT(id) FROM patient WHERE status = 'Active' AND doctor = '" + Data.doctor_id + "'";
        connect = DBConnection.getConnection();
        int getAP = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                getAP = result.getInt("COUNT(id)");
            }
            dashboard_TP.setText(String.valueOf(getAP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to display the total appointments
    public void dashbboardDisplayTA() {
        String sql = "SELECT COUNT(id) FROM appointment WHERE status = 'Active' AND doctor = '" + Data.doctor_id + "'";
        connect = DBConnection.getConnection();
        int getTA = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                getTA = result.getInt("COUNT(id)");
            }
            dashboard_tA.setText(String.valueOf(getTA));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to display the appointment table on the dashboard
    public ObservableList<AppointmentData> dashboardAppointmentTableView() {

        ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointment WHERE doctor = '" + Data.doctor_id + "' ORDER BY date ASC";

        connect = DBConnection.getConnection();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            AppointmentData aData;
            while (result.next()) {
                aData = new AppointmentData(result.getInt("appointment_id"),
                        result.getString("name"), result.getString("description"),
                        result.getDate("date"), result.getString("status"));

                listData.add(aData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<AppointmentData> dashboardGetData;

    //Method to display the data on the dashboard
    public void dashboardDisplayData() {
        dashboardGetData = dashboardAppointmentTableView();

        dashboad_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        dashboad_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        dashboad_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        dashboad_col_appointmentDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        dashboad_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        dashboad_tableView.setItems(dashboardGetData);
    }

    //Method to display the number of patients on the dashboard
    public void dashboardNOP() {
        dashboad_chart_PD.getData().clear();

        // Corrected SQL query: WHERE clause before GROUP BY
        String sql = "SELECT date, COUNT(id) FROM patient WHERE doctor = '" + Data.doctor_id + "' GROUP BY date";
        connect = DBConnection.getConnection();

        try {
            XYChart.Series chart = new XYChart.Series<>();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getDate(1), result.getInt(2)));
            }

            dashboad_chart_PD.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to display the number of appointments on the dashboard
    public void dashboardNOA() {
        dashboad_chart_DD.getData().clear();

        String sql = "SELECT date, COUNT(id) FROM appointment WHERE doctor = '" + Data.doctor_id + "' GROUP BY date ORDER BY date ASC LIMIT 7";
        connect = DBConnection.getConnection();

        try {
            XYChart.Series chart = new XYChart.Series<>();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Date sqlDate = result.getDate("date");
                String dateLabel = sqlDate.toString();
                int count = result.getInt("appointment_id");
                chart.getData().add(new XYChart.Data<>(dateLabel, count));
            }

            dashboad_chart_DD.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to perform the action of confirm button on the patient
    public void patientConfirmBtn() {
        if (patients_patientID.getText().isEmpty()
                || patients_patientName.getText().isEmpty()
                || patients_gender.getSelectionModel().getSelectedItem() == null
                || patients_mobileNumber.getText().isEmpty()
                || patients_password.getText().isEmpty()
                || patients_address.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            // To display the patient data
            patients_PA_patientID.setText(patients_patientID.getText());
            patients_PA_password.setText(patients_password.getText());
            patients_PA_dateCreated.setText(String.valueOf(sqlDate));

            // To display the personal information
            patients_PI_patientName.setText(patients_patientName.getText());
            patients_PI_gender.setText(patients_gender.getSelectionModel().getSelectedItem());
            patients_PI_mobileNumber.setText(patients_mobileNumber.getText());
            patients_PI_address.setText(patients_address.getText());
        }

    }

    //To perform the action of patient add button
    public void patientAddBtn() {

        if (patients_PA_patientID.getText().isEmpty()
                || patients_PA_password.getText().isEmpty()
                || patients_PA_dateCreated.getText().isEmpty()
                || patients_PI_patientName.getText().isEmpty()
                || patients_PI_gender.getText().isEmpty()
                || patients_PI_mobileNumber.getText().isEmpty()
                || patients_PI_address.getText().isEmpty()) {
            alert.errorMessage("Something went wrong");
        } else {

            DBConnection.getConnection();
            try {
                String doctorName = "";
                String doctorSpecialized = "";

                String getDoctor = "SELECT * FROM doctor WHERE doctor_id = '"
                        + nav_adminID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(getDoctor);

                if (result.next()) {
                    doctorName = result.getString("full_name");
                    doctorSpecialized = result.getString("specialized");
                }

                String checkPatientID = "SELECT * FROM patient WHERE patient_id = '"
                        + patients_PA_patientID.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkPatientID);
                if (result.next()) {
                    alert.errorMessage(patients_PA_patientID.getText() + " is already exist");
                } else {
                    String insertData = "INSERT INTO patient (patient_id, password, full_name, mobile_number, "
                            + "address, doctor, specialized, date, "
                            + "status) "
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, patients_PA_patientID.getText());
                    prepare.setString(2, patients_PA_password.getText());
                    prepare.setString(3, patients_PI_patientName.getText());
                    prepare.setString(4, patients_PI_mobileNumber.getText());
                    prepare.setString(5, patients_PI_address.getText());
                    prepare.setString(6, nav_adminID.getText());
                    prepare.setString(7, doctorSpecialized);
                    prepare.setString(8, "" + sqlDate);
                    prepare.setString(9, "Confirm");

                    prepare.executeUpdate();

                    alert.successMessage("Added successfully!");
                    patientClearFields();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //To perform the action of patient record button
    public void patientRecordBtn() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RecordPageForm.fxml")));
            Stage stage = new Stage();

            stage.setTitle("Hospital Management System | Record of Patients");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //To clear the fields in the patient table
    public void patientClearFields() {
        patients_patientID.clear();
        patients_patientName.clear();
        patients_gender.getSelectionModel().clearSelection();
        patients_mobileNumber.clear();
        patients_password.clear();
        patients_address.clear();

        patients_PA_patientID.setText("");
        patients_PA_password.setText("");
        patients_PA_dateCreated.setText("");

        patients_PI_patientName.setText("");
        patients_PI_gender.setText("");
        patients_PI_mobileNumber.setText("");
        patients_PI_address.setText("");
    }

    //To add the gender list in the combo box of the patient form
    private void patientGenderList() {

        List<String> listG = new ArrayList<>();

        Collections.addAll(listG, Data.gender);
        ObservableList<String> listData = FXCollections.observableList(listG);

        patients_gender.setItems(listData);

    }

    //To perform the action on the insert button of the appointment
    public void appointmentInsertBtn() {

        if (appointment_appointmentID.getText().isEmpty()
                || appointment_name.getText().isEmpty()
                || appointment_gender.getSelectionModel().getSelectedItem() == null
                || appointment_mobileNumber.getText().isEmpty()
                || appointment_description.getText().isEmpty()
                || appointment_address.getText().isEmpty()
                || appointment_status.getSelectionModel().getSelectedItem() == null
                || appointment_schedule.getValue() == null) {
            alert.errorMessage("Please fill the blank fields");
        } else {
            String checkAppointmentID = "SELECT * FROM appointment WHERE appointment_id = "
                    + appointment_appointmentID.getText();
            connect = DBConnection.getConnection();
            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkAppointmentID);

                if (result.next()) {
                    alert.errorMessage(appointment_appointmentID.getText() + " was already taken");
                } else {
                    String getSpecialized = "";
                    String getDoctorData = "SELECT * FROM doctor WHERE doctor_id = '"
                            + Data.doctor_id + "'";

                    statement = connect.createStatement();
                    result = statement.executeQuery(getDoctorData);

                    if (result.next()) {
                        getSpecialized = result.getString("specialized");
                    }

                    String insertData = "INSERT INTO appointment (appointment_id, name, gender"
                            + ", description, diagnosis, treatment, mobile_number"
                            + ", address, date, status, doctor, specialized, schedule) "
                            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);

                    prepare.setString(1, appointment_appointmentID.getText());
                    prepare.setString(2, appointment_name.getText());
                    prepare.setString(3, (String) appointment_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(4, appointment_description.getText());
                    prepare.setString(5, appointment_diagnosis.getText());
                    prepare.setString(6, appointment_treatment.getText());
                    prepare.setString(7, appointment_mobileNumber.getText());
                    prepare.setString(8, appointment_address.getText());

                    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

                    prepare.setString(9, "" + sqlDate);
                    prepare.setString(10, (String) appointment_status.getSelectionModel().getSelectedItem());
                    prepare.setString(11, Data.doctor_id);
                    prepare.setString(12, getSpecialized);
                    prepare.setString(13, "" + appointment_schedule.getValue());

                    prepare.executeUpdate();

                    appointmentShowData();
                    appointmentAppointmentID();
                    appointmentClearBtn();
                    alert.successMessage("Successully added!");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //To perform the action of the update button of the appointment form
    public void appointmentUpdateBtn() {

        if (appointment_appointmentID.getText().isEmpty()
                || appointment_name.getText().isEmpty()
                || appointment_gender.getSelectionModel().getSelectedItem() == null
                || appointment_mobileNumber.getText().isEmpty()
                || appointment_description.getText().isEmpty()
                || appointment_address.getText().isEmpty()
                || appointment_status.getSelectionModel().getSelectedItem() == null
                || appointment_schedule.getValue() == null) {
            alert.errorMessage("Please fill the blank fields");
        } else {
            java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

            String updateData = "UPDATE appointment SET name = '"
                    + appointment_name.getText() + "', gender = '"
                    + appointment_gender.getSelectionModel().getSelectedItem() + "', mobile_number = '"
                    + appointment_mobileNumber.getText() + "', description = '"
                    + appointment_description.getText() + "', address = '"
                    + appointment_address.getText() + "', status = '"
                    + appointment_status.getSelectionModel().getSelectedItem() + "', schedule = '"
                    + appointment_schedule.getValue() + "', date_modify = '"
                    + sqlDate + "' WHERE appointment_id = '"
                    + appointment_appointmentID.getText() + "'";

            connect = DBConnection.getConnection();

            try {
                if (alert.confirmMessage("Are you sure you want to UPDATE Appointment ID: "
                        + appointment_appointmentID.getText() + "?")) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    appointmentShowData();
                    appointmentAppointmentID();
                    appointmentClearBtn();
                    alert.successMessage("Successully Updated!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //To perform the action of the delete button of the appointment
    public void appointmentDeleteBtn() {

        if (appointment_appointmentID.getText().isEmpty()) {
            alert.errorMessage("Please select the item first");
        } else {

            String updateData = "UPDATE appointment SET date_delete = ? WHERE appointment_id = '"
                    + appointment_appointmentID.getText() + "'";

            connect = DBConnection.getConnection();

            try {
                java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

                if (alert.confirmMessage("Are you sure you want to DELETE Appointment ID: "
                        + appointment_appointmentID.getText() + "?")) {
                    prepare = connect.prepareStatement(updateData);

                    prepare.setString(1, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    appointmentShowData();
                    appointmentAppointmentID();
                    appointmentClearBtn();

                    alert.successMessage("Successully Updated!");
                } else {
                    alert.errorMessage("Cancelled.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    // To clear the fields of the appointment
    public void appointmentClearBtn() {
        appointment_appointmentID.clear();
        appointment_name.clear();
        appointment_gender.getSelectionModel().clearSelection();
        appointment_mobileNumber.clear();
        appointment_description.clear();
        appointment_treatment.clear();
        appointment_diagnosis.clear();
        appointment_address.clear();
        appointment_status.getSelectionModel().clearSelection();
        appointment_schedule.setValue(null);
    }

    private Integer appointmentID;

    //To find the appointment using the id of the appointment
    public void appointmentGetAppointmentID() {
        String sql = "SELECT MAX(appointment_id) FROM appointment";
        connect = DBConnection.getConnection();

        int tempAppID = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                tempAppID = result.getInt("MAX(appointment_id)");
            }
            if (tempAppID == 0) {
                tempAppID += 1;
            } else {
                tempAppID += 1;
            }
            appointmentID = tempAppID;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To display the appointment using the appointment id
    public void appointmentAppointmentID() {
        appointmentGetAppointmentID();

        appointment_appointmentID.setText("" + appointmentID);
        appointment_appointmentID.setDisable(true);

    }

    //To display the gender list on the appointments
    public void appointmentGenderList() {
        List<String> listG = new ArrayList<>();

        for (String data : Data.gender) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);
        appointment_gender.setItems(listData);

    }

    //To add the gender on the appointment status
    public void appointmentStatusList() {
        List<String> listS = new ArrayList<>();

        for (String data : Data.status) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        appointment_status.setItems(listData);

    }

    //To display the appointment for the specific doctor
    public ObservableList<AppointmentData> appointmentGetData() {

        ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointment WHERE date_delete IS NULL and doctor = '"
                + Data.doctor_id + "'";

        connect = DBConnection.getConnection();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            AppointmentData appData;

            while (result.next()) {
                appData = new AppointmentData(result.getInt("appointment_id"),
                        result.getString("name"), result.getString("gender"),
                        result.getLong("mobile_number"), result.getString("description"),
                        result.getString("diagnosis"), result.getString("treatment"),
                        result.getString("address"), result.getDate("date"),
                        result.getDate("date_modify"), result.getDate("date_delete"),
                        result.getString("status"), result.getDate("schedule"));
                listData.add(appData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    //To list the updated data of the appointment
    public ObservableList<AppointmentData> appoinmentListData;

    //To show the data of the appointment on the appointment form of the doctor
    public void appointmentShowData() {
        appoinmentListData = appointmentGetData();

        appointments_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointments_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointments_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        appointments_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        appointments_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointments_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointments_col_dateModify.setCellValueFactory(new PropertyValueFactory<>("dateModify"));
        appointments_col_dateDelete.setCellValueFactory(new PropertyValueFactory<>("dateDelete"));
        appointments_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointments_tableView.setItems(appoinmentListData);
    }

    //To select the specific data per row in the table
    public void appointmentSelect() {

        AppointmentData appData = appointments_tableView.getSelectionModel().getSelectedItem();
        int num = appointments_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        appointment_appointmentID.setText("" + appData.getAppointmentID());
        appointment_name.setText(appData.getName());
        appointment_gender.getSelectionModel().select(appData.getGender());
        appointment_mobileNumber.setText("" + appData.getMobileNumber());
        appointment_description.setText(appData.getDescription());
        appointment_diagnosis.setText(appData.getDiagnosis());
        appointment_treatment.setText(appData.getTreatment());
        appointment_address.setText(appData.getAddress());
        appointment_status.getSelectionModel().select(appData.getStatus());

    }

    //To update the profile of the doctor
    public void profileUpdateBtn() {

        connect = DBConnection.getConnection();

        if (profile_doctorID.getText().isEmpty()
                || profile_name.getText().isEmpty()
                || profile_email.getText().isEmpty()
                || profile_gender.getSelectionModel().getSelectedItem() == null
                || profile_mobileNumber.getText().isEmpty()
                || profile_address.getText().isEmpty()
                || profile_specialized.getSelectionModel().getSelectedItem() == null
                || profile_status.getSelectionModel().getSelectedItem() == null) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            if (Data.path == null || Data.path.isEmpty()) {
                String updateData = "UPDATE doctor SET full_name = ?, email = ?"
                        + ", gender = ?, mobile_number = ?, address = ?, specialized = ?, status = ?, modify_date = ?"
                        + " WHERE doctor_id = '"
                        + Data.doctor_id + "'";
                try {
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, profile_name.getText());
                    prepare.setString(2, profile_email.getText());
                    prepare.setString(3, profile_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(4, profile_mobileNumber.getText());
                    prepare.setString(5, profile_address.getText());
                    prepare.setString(6, profile_specialized.getSelectionModel().getSelectedItem());
                    prepare.setString(7, profile_status.getSelectionModel().getSelectedItem());
                    prepare.setString(8, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert.successMessage("Updated Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String updateData = "UPDATE doctor SET full_name = ?, email = ?"
                        + ", gender = ?, mobile_number = ?, address = ?, image = ?, specialized = ?, status = ?, modify_date = ?"
                        + " WHERE doctor_id = '"
                        + Data.doctor_id + "'";
                try {
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, profile_name.getText());
                    prepare.setString(2, profile_email.getText());
                    prepare.setString(3, profile_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(4, profile_mobileNumber.getText());
                    prepare.setString(5, profile_address.getText());
                    String path = Data.path;
                    path = path.replace("\\", "\\\\");
                    Path transfer = Paths.get(path);

                    // Link to the folder
                    Path copy = Paths.get("F:\\HospitalManagementSystem\\src\\main\\resources\\com\\example\\hospitalmanagementsystem\\1.jpg"
                            + Data.doctor_id + ".jpg");

                    try {
                        // To put the image file
                        Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    prepare.setString(6, copy.toAbsolutePath().toString());
                    prepare.setString(7, profile_specialized.getSelectionModel().getSelectedItem());
                    prepare.setString(8, profile_status.getSelectionModel().getSelectedItem());
                    prepare.setString(9, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert.successMessage("Updated Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //To change the profile image of the profile
    public void profileChangeProfile() {

        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*png", "*jpg", "*jpeg"));

        File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

        if (file != null) {
            Data.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 128, 103, false, true);
            profile_circleImage.setFill(new ImagePattern(image));
        }

    }

    public void profileLabels() {
        String selectData = "SELECT * FROM doctor WHERE doctor_id = '"
                + Data.doctor_id + "'";
        connect = DBConnection.getConnection();

        try {
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            if (result.next()) {
                profile_label_doctorID.setText(result.getString("doctor_id"));
                profile_label_name.setText(result.getString("full_name"));
                profile_label_email.setText(result.getString("email"));
                profile_label_dateCreated.setText(result.getString("date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To display the fields of the specific doctor
    public void profileFields() {
        String selectData = "SELECT * FROM doctor WHERE doctor_id = '"
                + Data.doctor_id + "'";

        connect = DBConnection.getConnection();
        try {
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            if (result.next()) {
                profile_doctorID.setText(result.getString("doctor_id"));
                profile_name.setText(result.getString("full_name"));
                profile_email.setText(result.getString("email"));
                profile_gender.getSelectionModel().select(result.getString("gender"));
                profile_mobileNumber.setText(result.getString("mobile_number"));
                profile_address.setText(result.getString("address"));
                profile_specialized.getSelectionModel().select(result.getString("specialized"));
                profile_status.getSelectionModel().select(result.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //To display the image of the doctor
    @FXML public void profileDisplayImages() {
        String selectData = "SELECT * FROM doctor WHERE doctor_id = " + Data.doctor_id + " ' ";
        connect = DBConnection.getConnection();

        String tempPath1 = "";
        String tempPath2 = "";
        try {
            assert connect != null;
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            if (result.next()) {
                tempPath1 = "File:" + result.getString("image");
                tempPath2 = "File:" + result.getString("image");

                if (result.getString("image") != null) {
                    image = new Image(tempPath1, 1012, 22, false, true);
                    top_profile.setFill(new ImagePattern(image));

                    image = new Image(tempPath2, 137, 95, false, true);
                    profile_circleImage.setFill(new ImagePattern(image));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //To display the gender list of the profile
    public void profileGenderList() {

        List<String> listG = new ArrayList<>();

        for (String data : Data.gender) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);
        profile_gender.setItems(listData);

    }

    //To store the specialization fields of the doctor
    private String[] specialization = {"Allergist", "Dermatologist", "Ophthalmologist", "Gynecologist", "Cardiologist"};

    //To display the specialized list of the doctor on the profile
    public void profileSpecializedList() {

        List<String> listS = new ArrayList<>();

        for (String data : specialization) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        profile_specialized.setItems(listData);
    }

    //To display the status of the doctor on the profile update
    public void profileStatusList() {
        List<String> listS = new ArrayList<>();

        for (String data : Data.status) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        profile_status.setItems(listData);
    }

    //To display the doctor id
    public void displayAdminIDNumberName() {
        String name = Data.doctor_name;
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        nav_username.setText(name);
        nav_adminID.setText(Data.doctor_id);
        top_username.setText(name);

    }

    //To perform the different anchorpanes on the action of different buttons
    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            profile_form.setVisible(false);
        } else if (event.getSource() == patients_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(true);
            appointments_form.setVisible(false);
            profile_form.setVisible(false);
        } else if (event.getSource() == appointments_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(true);
            profile_form.setVisible(false);
        } else if (event.getSource() == profile_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            profile_form.setVisible(true);
        }
    }

    //To perform the action of logout button
    public void logoutBtn() {
        try {
                Data.doctor_id = "";
                Data.doctor_name = "";
                Parent root = FXMLLoader.load(getClass().getResource("DoctorPage.fxml"));
                Stage stage = new Stage();

                stage.setScene(new Scene(root));
                stage.show();

                // TO HIDE YOUR MAIN FORM
                logout_btn.getScene().getWindow().hide();

                Data.doctor_id = "";
                Data.doctor_name = "";
                Data.temp_PatientID = 0;
                Data.temp_name = "";
                Data.temp_gender = "";
                Data.temp_number = Long.parseLong("0");
                Data.temp_address = "";
                Data.temp_status = "";
                Data.temp_date = "";
                Data.temp_path = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //To display the current date and time on the dashboard form of the doctor
    public void runTime() {
        new Thread(() -> {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    date_time.setText(format.format(new Date()));
                });
            }
        }).start();
    }

    //Method to display the initialization of the program or either GUI
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayAdminIDNumberName();
        runTime();

        dashbboardDisplayIP();
        dashbboardDisplayTP();
        dashbboardDisplayAP();
        dashbboardDisplayTA();
        dashboardDisplayData();
       dashboardNOP();
        dashboardNOA();

        // TO SHOW THE DATA IMMEDIATELY ONCE YOU LOGGED IN YOUR ACCOUNT
        appointmentShowData();
        appointmentGenderList();
        appointmentStatusList();
        appointmentAppointmentID();

        // TO SHOW THE DATA IMMEDIATELY THE PATIENT'S GENDER COMBOXBOX
        patientGenderList();

//        PROFILE
        profileLabels();
        profileFields(); // TO DISPLAY ALL DETAILS TO THE FIELDS
        profileGenderList();
        profileSpecializedList();
        profileStatusList();
        profileDisplayImages(); // TO DISPLAY THE PROFILE PICTURE OF THE DOCTOR

    }

}
