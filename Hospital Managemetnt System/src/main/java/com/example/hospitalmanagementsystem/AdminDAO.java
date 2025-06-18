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
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AdminDAO implements Initializable {

    // Name of all components
    @FXML private Circle top_profile;
    @FXML private Label top_username;
    @FXML private Label date_time;
    @FXML private Label current_form;
    @FXML private Label nav_adminID;
    @FXML private Label nav_username;
    @FXML private Button dashboard_btn;
    @FXML private Button doctors_btn;
    @FXML private Button patients_btn;
    @FXML private Button appointments_btn;
    @FXML private Button payment_btn;
    @FXML private Button profile_btn;
    @FXML private AnchorPane dashboard_form;
    @FXML private Label dashboard_AD;
    @FXML private Label dashboard_TP;
    @FXML private Label dashboard_AP;
    @FXML private Label dashboard_tA;
    @FXML private AreaChart<?, ?> dashboad_chart_PD;
    @FXML private BarChart<?, ?> dashboad_chart_DD;
    @FXML private TableView<DoctorData> dashboad_tableView;
    @FXML private TableColumn<DoctorData, String> dashboad_col_doctorID;
    @FXML private TableColumn<DoctorData, String> dashboad_col_name;
    @FXML private TableColumn<DoctorData, String> dashboad_col_specialized;
    @FXML private TableColumn<DoctorData, String> dashboad_col_status;
    @FXML private AnchorPane doctors_form;
    @FXML private TableView<DoctorData> doctors_tableView;
    @FXML private TableColumn<DoctorData, String> doctors_col_doctorID;
    @FXML private TableColumn<DoctorData, String> doctors_col_name;
    @FXML private TableColumn<DoctorData, String> doctors_col_gender;
    @FXML private TableColumn<DoctorData, String> doctors_col_contactNumber;
    @FXML private TableColumn<DoctorData, String> doctors_col_email;
    @FXML private TableColumn<DoctorData, String> doctors_col_specialization;
    @FXML private TableColumn<DoctorData, String> doctors_col_address;
    @FXML private TableColumn<DoctorData, String> doctors_col_status;
    @FXML private TableColumn<DoctorData, String> doctors_col_action;
    @FXML private AnchorPane patients_form;
    @FXML private TableView<PatientsData> patients_tableView;
    @FXML private TableColumn<PatientsData, String> patients_col_patientID;
    @FXML private TableColumn<PatientsData, String> patients_col_name;
    @FXML private TableColumn<PatientsData, String> patients_col_gender;
    @FXML private TableColumn<PatientsData, String> patients_col_contactNumber;
    @FXML private TableColumn<PatientsData, String> patients_col_description;
    @FXML private TableColumn<PatientsData, String> patients_col_date;
    @FXML private TableColumn<PatientsData, String> patients_col_dateModify;
    @FXML private TableColumn<PatientsData, String> patients_col_dateDelete;
    @FXML private TableColumn<PatientsData, String> patients_col_status;
    @FXML private TableColumn<PatientsData, String> patients_col_action;
    @FXML private AnchorPane appointments_form;
    @FXML private TableView<AppointmentData> appointments_tableView;
    @FXML private TableColumn<AppointmentData, String> appointments_appointmentID;
    @FXML private TableColumn<AppointmentData, String> appointments_name;
    @FXML private TableColumn<AppointmentData, String> appointments_gender;
    @FXML private TableColumn<AppointmentData, String> appointments_contactNumber;
    @FXML private TableColumn<AppointmentData, String> appointments_description;
    @FXML private TableColumn<AppointmentData, String> appointments_date;
    @FXML private TableColumn<AppointmentData, String> appointments_dateModify;
    @FXML private TableColumn<AppointmentData, String> appointments_dateDelete;
    @FXML private TableColumn<AppointmentData, String> appointments_status;
    @FXML private TableColumn<AppointmentData, String> appointments_action;
    @FXML private AnchorPane profile_form;
    @FXML private Circle profile_circle;
    @FXML private Button profile_importBtn;
    @FXML private Label profile_label_adminIO;
    @FXML private Label profile_label_name;
    @FXML private Label profile_label_email;
    @FXML private Label profile_label_dateCreated;
    @FXML private TextField profile_adminID;
    @FXML private TextField profile_username;
    @FXML private TextField profile_email;
    @FXML private ComboBox<String> profile_status;
    @FXML private AnchorPane payment_form;
    @FXML private TableView<PatientsData> payment_tableView;
    @FXML private TableColumn<PatientsData, String> payment_col_patientID;
    @FXML private TableColumn<PatientsData, String> payment_col_name;
    @FXML private TableColumn<PatientsData, String> payment_col_gender;
    @FXML private TableColumn<PatientsData, String> payment_col_diagnosis;
    @FXML private TableColumn<PatientsData, String> payment_col_doctor;
    @FXML private TableColumn<PatientsData, String> payment_col_date;
    @FXML private Circle payment_circle;
    @FXML private Label payment_patientID;
    @FXML private Button add_doctorBtn;
    @FXML private Label payment_name;
    @FXML private Label payment_doctor;
    @FXML private Label payment_date;
    @FXML private Button logout_btn;

    //    Database Connection
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    //Object creation for alert message
    final private AlertMessage alert = new AlertMessage();

    // Image requires to update or add to the profile so use the image object declaration
    private Image image;

    //Method to display the active doctors
    @FXML public void dashboardAD() {

        String sql = "SELECT COUNT(id) FROM doctor WHERE status = 'Active' AND delete_date IS NULL";

        connect = DBConnection.getConnection();

        int tempAD = 0;
        try {

            if(connect != null) {
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                if (result.next()) {
                    tempAD = result.getInt("COUNT(id)");
                }
                dashboard_AD.setText(String.valueOf(tempAD));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to display the total patients
    @FXML public void dashboardTP() {

        String sql = "SELECT COUNT(id) FROM patient WHERE date_delete IS NULL";

        connect = DBConnection.getConnection();

        int tempTP = 0;
        try {

            if(connect != null) {
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                if (result.next()) {
                    tempTP = result.getInt("COUNT(id)");
                }
                dashboard_TP.setText(String.valueOf(tempTP));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to display the active patient
    @FXML public void dashboardAP() {

        String sql = "SELECT COUNT(patient_id) FROM patient WHERE status = 'Active'";

        connect = DBConnection.getConnection();

        int tempAP = 0;
        try {

            if (connect != null) {
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                if (result.next()) {
                    tempAP = result.getInt("COUNT(patient_id)");
                }
                dashboard_AP.setText(String.valueOf(tempAP));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to display the total appointments
    @FXML public void dashboardTA() {

        String sql = "SELECT COUNT(id) FROM appointment";

        connect = DBConnection.getConnection();

        int tempTA = 0;
        try {

            if (connect != null) {
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                if (result.next()) {
                    tempTA = result.getInt("COUNT(id)");
                }
                dashboard_tA.setText(String.valueOf(tempTA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to get the data of doctor for the dashboard
    @FXML public ObservableList<DoctorData> dashboardGetDoctorData() {

        ObservableList<DoctorData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM doctor WHERE delete_date IS NULL";

        connect = DBConnection.getConnection();

        try {

            if (connect != null) {
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                DoctorData dData;

                while (result.next()) {

                    dData = new DoctorData(result.getString("doctor_id"),
                            result.getString("full_name"), result.getString("specialized"),
                            result.getString("status"));

                    listData.add(dData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    //Method to get the updated data of doctor
    @FXML public ObservableList<DoctorData> dashboardGetDoctorListData;

    //Method to show the data of doctor on the dashboard of the admin
    @FXML public void dashboardGetDoctorDisplayData() {
        dashboardGetDoctorListData = dashboardGetDoctorData();
        dashboad_col_doctorID.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        dashboad_col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        dashboad_col_specialized.setCellValueFactory(new PropertyValueFactory<>("specialized"));
        dashboad_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        dashboad_tableView.setItems(dashboardGetDoctorListData);

    }

    //Method to display the patient chart
    @FXML public void dashboardPatientDataChart() {
        dashboad_chart_PD.getData().clear();
        String selectData = "SELECT date, COUNT(id) FROM doctor GROUP BY date ORDER BY date ASC LIMIT 8";
        connect = DBConnection.getConnection();
        XYChart.Series chart = new XYChart.Series<>();

        try {
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboad_chart_PD.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to show the doctor chart
    @FXML public void dashboardDoctorDataChart() {
        dashboad_chart_DD.getData().clear();

        String selectData = "SELECT date, COUNT(id) FROM doctor WHERE delete_date IS NULL GROUP BY date ORDER BY date ASC LIMIT 8";

        connect = DBConnection.getConnection();
        XYChart.Series chart = new XYChart.Series<>();

        try {
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboad_chart_DD.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to retrieve the doctor data
    @FXML public ObservableList<DoctorData> doctorGetData() {
        ObservableList<DoctorData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM doctor";

        connect = DBConnection.getConnection();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            DoctorData dData;
            while (result.next()) {
                dData = new DoctorData(result.getInt("id"), result.getString("doctor_id"),
                        result.getString("password"), result.getString("full_name"),
                        result.getString("email"), result.getString("gender"),
                        result.getLong("mobile_number"), result.getString("specialized"),
                        result.getString("address"), result.getString("image"),
                        result.getDate("date"), result.getDate("modify_date"),
                        result.getDate("delete_date"), result.getString("status"));

                listData.add(dData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    //Method to list the doctor data
    private ObservableList<DoctorData> doctorListData;

    //Method to display the doctor data
    @FXML public void doctorDisplayData() {
        doctorListData = doctorGetData();
        doctors_col_doctorID.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        doctors_col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        doctors_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        doctors_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        doctors_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        doctors_col_specialization.setCellValueFactory(new PropertyValueFactory<>("specialized"));
        doctors_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        doctors_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        doctors_tableView.setItems(doctorListData);

    }

    //Method to perform the action of doctor button
    @FXML public void doctorActionButton() {
        connect = DBConnection.getConnection();
        doctorListData = doctorGetData();
        Callback<TableColumn<DoctorData, String>, TableCell<DoctorData, String>> cellFactory = (TableColumn<DoctorData, String> param) -> {
            final TableCell<DoctorData, String> cell = new TableCell<>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button editButton = new Button("Edit");
                        Button removeButton = new Button("Delete");

                        editButton.setStyle("""
                                -fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);
                                    -fx-cursor: hand;
                                    -fx-text-fill: #fff;
                                    -fx-font-size: 14px;
                                    -fx-font-family: Arial;""");

                        removeButton.setStyle("""
                                -fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);
                                    -fx-cursor: hand;
                                    -fx-text-fill: #fff;
                                    -fx-font-size: 14px;
                                    -fx-font-family: Arial;""");

                        editButton.setOnAction((ActionEvent event) -> {
                            try {

                                DoctorData pData = doctors_tableView.getSelectionModel().getSelectedItem();
                                int num = doctors_tableView.getSelectionModel().getSelectedIndex();

                                if ((num - 1) < -1) {
                                    alert.errorMessage("Please select item first");
                                    return;
                                }

                                Data.temp_doctorID = pData.getDoctorID();
                                Data.temp_doctorName = pData.getFullName();
                                Data.temp_doctorEmail = pData.getEmail();
                                Data.temp_doctorPassword = pData.getPassword();
                                Data.temp_doctorSpecialized = pData.getSpecialized();
                                Data.temp_doctorGender = pData.getGender();
                                Data.temp_doctorMobileNumber = String.valueOf(pData.getMobileNumber());
                                Data.temp_doctorAddress = pData.getAddress();
                                Data.temp_doctorStatus = pData.getStatus();
                                Data.temp_doctorImagePath = pData.getImage();

                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("EditDoctorForm.fxml")));
                                Stage stage = new Stage();

                                stage.setScene(new Scene(root));
                                stage.show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        removeButton.setOnAction((ActionEvent event) -> {
                            DoctorData pData = doctors_tableView.getSelectionModel().getSelectedItem();
                            int num = doctors_tableView.getSelectionModel().getSelectedIndex();

                            if ((num - 1) < -1) {
                                alert.errorMessage("Please select item first");
                                return;
                            }

                            String deleteData = "UPDATE doctor SET delete_date = ? WHERE doctor_id = '"
                                    + pData.getDoctorID() + "'";

                            try {
                                if (alert.confirmMessage("Are you sure you want to delete Doctor ID: " + pData.getDoctorID() + "?")) {
                                    prepare = connect.prepareStatement(deleteData);
                                    Date date = new Date();
                                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                                    prepare.setString(1, String.valueOf(sqlDate));
                                    prepare.executeUpdate();

                                    doctorGetData();
                                    alert.successMessage("Deleted Successfully!");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        HBox manageBtn = new HBox(editButton, removeButton);
                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(5);
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            doctorDisplayData();
            return cell;
        };

        doctors_col_action.setCellFactory(cellFactory);
        doctors_tableView.setItems(doctorListData);

    }

    //Method to get the data of patient
    public ObservableList<PatientsData> patientGetData() {
        ObservableList<PatientsData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM patient";

        connect = DBConnection.getConnection();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            PatientsData pData;

            while (result.next()) {

                pData = new PatientsData(result.getInt("id"), result.getInt("patient_id"),
                        result.getString("password"), result.getString("full_name"),
                        result.getLong("mobile_number"), result.getString("gender"),
                        result.getString("address"),
                        result.getString("image"), result.getString("description"),
                        result.getString("diagnosis"),
                        result.getString("treatment"), result.getString("doctor"),
                        result.getString("specialized"), result.getDate("date"),
                        result.getDate("date_modify"), result.getDate("date_delete"),
                        result.getString("status"));

                listData.add(pData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    //Method to list the data of patient
    public ObservableList<PatientsData> patientListData;

    //Method to display the data of patient
    @FXML public void patientDisplayData() {
        patientListData = patientGetData();
        patients_col_patientID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patients_col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        patients_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        patients_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        patients_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        patients_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        patients_col_dateModify.setCellValueFactory(new PropertyValueFactory<>("dateModify"));
        patients_col_dateDelete.setCellValueFactory(new PropertyValueFactory<>("dateDelete"));
        patients_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        patients_tableView.setItems(patientListData);
    }

    //Method to perform the action of patient button
    @FXML public void patientActionButton() {
        connect = DBConnection.getConnection();
        patientListData = patientGetData();

        Callback<TableColumn<PatientsData, String>, TableCell<PatientsData, String>> cellFactory = (TableColumn<PatientsData, String> param) -> {
            final TableCell<PatientsData, String> cell = new TableCell<>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button editButton = new Button("Edit");
                        Button removeButton = new Button("Delete");

                        editButton.setStyle("""
                                -fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);
                                    -fx-cursor: hand;
                                    -fx-text-fill: #fff;
                                    -fx-font-size: 14px;
                                    -fx-font-family: Arial;""");

                        removeButton.setStyle("""
                                -fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);
                                    -fx-cursor: hand;
                                    -fx-text-fill: #fff;
                                    -fx-font-size: 14px;
                                    -fx-font-family: Arial;""");

                        editButton.setOnAction((ActionEvent event) -> {
                            try {

                                PatientsData pData = patients_tableView.getSelectionModel().getSelectedItem();
                                int num = patients_tableView.getSelectionModel().getSelectedIndex();

                                if ((num - 1) < -1) {
                                    alert.errorMessage("Please select item first");
                                    return;
                                }

                                Data.temp_PatientID = pData.getPatientID();
                                Data.temp_address = pData.getAddress();
                                Data.temp_name = pData.getFullName();
                                Data.temp_gender = pData.getGender();
                                Data.temp_number = pData.getMobileNumber();
                                Data.temp_status = pData.getStatus();

                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("EditPatientForm.fxml")));
                                Stage stage = new Stage();

                                stage.setScene(new Scene(root));
                                stage.show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        removeButton.setOnAction((ActionEvent event) -> {
                            PatientsData pData = patients_tableView.getSelectionModel().getSelectedItem();
                            int num = patients_tableView.getSelectionModel().getSelectedIndex();

                            if ((num - 1) < -1) {
                                alert.errorMessage("Please select item first");
                                return;
                            }

                            String deleteData = "UPDATE patient SET date_delete = ? WHERE patient_id = '"
                                    + pData.getPatientID() + "'";

                            try {
                                if (alert.confirmMessage("Are you sure you want to delete Patient ID: " + pData.getPatientID() + "?")) {
                                    prepare = connect.prepareStatement(deleteData);
                                    Date date = new Date();
                                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                                    prepare.setString(1, String.valueOf(sqlDate));
                                    prepare.executeUpdate();

                                    doctorGetData();
                                    alert.successMessage("Deleted Successfully!");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        HBox manageBtn = new HBox(editButton, removeButton);
                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(5);
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            doctorDisplayData();
            return cell;
        };

        patients_col_action.setCellFactory(cellFactory);
        patients_tableView.setItems(patientListData);

    }

    //Method to get the data of appointment from the database
    @FXML public ObservableList<AppointmentData> appointmentGetData() {
        ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointment";

        connect = DBConnection.getConnection();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            AppointmentData aData;
            while (result.next()) {

                aData = new AppointmentData(result.getInt("id"), result.getInt("appointment_id"),
                        result.getString("name"), result.getString("gender"), result.getLong("mobile_number"),
                        result.getString("description"), result.getString("diagnosis"),
                        result.getString("treatment"), result.getString("address"),
                        result.getString("doctor"), result.getString("specialized"),
                        result.getDate("date"), result.getDate("date_modify"),
                        result.getDate("date_delete"), result.getString("status"),
                        result.getDate("schedule"));
                listData.add(aData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    //Method to list the data of appointment
    private ObservableList<AppointmentData> appointmentListData;

    //Method to display the data of appointment
    @FXML public void appointmentDisplayData() {
        appointmentListData = appointmentGetData();
        appointments_appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointments_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointments_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        appointments_contactNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        appointments_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointments_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointments_dateModify.setCellValueFactory(new PropertyValueFactory<>("dateModify"));
        appointments_dateDelete.setCellValueFactory(new PropertyValueFactory<>("dateDelete"));
        appointments_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointments_tableView.setItems(appointmentListData);

    }

    //Method to perform the action of appointment button
    @FXML public void appointmentActionButton() {
        connect = DBConnection.getConnection();
        appointmentListData = appointmentGetData();

        Callback<TableColumn<AppointmentData, String>, TableCell<AppointmentData, String>> cellFactory = (TableColumn<AppointmentData, String> param) -> {
            final TableCell<AppointmentData, String> cell = new TableCell<>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button editButton = new Button("Edit");
                        Button removeButton = new Button("Delete");

                        editButton.setStyle("""
                                -fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);
                                    -fx-cursor: hand;
                                    -fx-text-fill: #fff;
                                    -fx-font-size: 14px;
                                    -fx-font-family: Arial;""");

                        removeButton.setStyle("""
                                -fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);
                                    -fx-cursor: hand;
                                    -fx-text-fill: #fff;
                                    -fx-font-size: 14px;
                                    -fx-font-family: Arial;""");

                        editButton.setOnAction((ActionEvent event) -> {
                            try {

                                AppointmentData aData = appointments_tableView.getSelectionModel().getSelectedItem();
                                int num = appointments_tableView.getSelectionModel().getSelectedIndex();

                                if ((num - 1) < -1) {
                                    alert.errorMessage("Please select item first");
                                    return;
                                }

                                Data.temp_appID = String.valueOf(aData.getAppointmentID());
                                Data.temp_appName = aData.getName();
                                Data.temp_appGender = aData.getGender();
                                Data.temp_appAddress = aData.getAddress();
                                Data.temp_appDescription = aData.getDescription();
                                Data.temp_appDiagnosis = aData.getDiagnosis();
                                Data.temp_appTreatment = aData.getTreatment();
                                Data.temp_appMobileNumber = String.valueOf(aData.getMobileNumber());
                                Data.temp_appDoctor = aData.getDoctorID();
                                Data.temp_appSpecialized = aData.getSpecialized();
                                Data.temp_appStatus = aData.getStatus();

                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("EditAppointmentForm.fxml")));
                                Stage stage = new Stage();

                                stage.setScene(new Scene(root));
                                stage.show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        removeButton.setOnAction((ActionEvent event) -> {
                            AppointmentData aData = appointments_tableView.getSelectionModel().getSelectedItem();
                            int num = appointments_tableView.getSelectionModel().getSelectedIndex();

                            if ((num - 1) < -1) {
                                alert.errorMessage("Please select item first");
                                return;
                            }

                            String deleteData = "UPDATE appointment SET date_delete = ? WHERE appointment_id = '"
                                    + aData.getAppointmentID() + "'";

                            try {
                                if (alert.confirmMessage("Are you sure you want to delete Appointment ID: " + aData.getAppointmentID() + "?")) {
                                    prepare = connect.prepareStatement(deleteData);
                                    Date date = new Date();
                                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                                    prepare.setString(1, String.valueOf(sqlDate));
                                    prepare.executeUpdate();

                                    doctorGetData();
                                    alert.successMessage("Deleted Successfully!");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        HBox manageBtn = new HBox(editButton, removeButton);
                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(5);
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            doctorDisplayData();
            return cell;
        };

        appointments_action.setCellFactory(cellFactory);
        appointments_tableView.setItems(appointmentListData);

    }

    //Method to get payment data using list of patients data
    @FXML public ObservableList<PatientsData> paymentGetData() {

        ObservableList<PatientsData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM patient WHERE date_delete IS NULL AND status_pay IS NULL";
        connect = DBConnection.getConnection();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            PatientsData pData;
            while (result.next()) {

                pData = new PatientsData(result.getInt("id"),
                        result.getInt("patient_id"), result.getString("full_name"),
                        result.getString("gender"), result.getString("description"),
                        result.getString("diagnosis"), result.getString("treatment"),
                        result.getString("doctor"), result.getString("image"), result.getDate("date"));

                listData.add(pData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    //Method to list the patients data
    public ObservableList<PatientsData> paymentListData;

    //Method to display the payment date
    @FXML public void paymentDisplayData() {
        paymentListData = paymentGetData();

        payment_col_patientID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        payment_col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        payment_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        payment_col_diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        payment_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        payment_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        payment_tableView.setItems(paymentListData);

    }

    //Method to select the item whose payment is to be found
    @FXML public void paymentSelectItems() {

        PatientsData pData = payment_tableView.getSelectionModel().getSelectedItem();
        int num = payment_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        if (pData.getImage() != null) {
            String path = "File:" + pData.getImage();
            image = new Image(path, 144, 105, false, true);
            payment_circle.setFill(new ImagePattern(image));

            Data.temp_path = pData.getImage();
        }

        Data.temp_PatientID = pData.getPatientID();
        Data.temp_name = pData.getFullName();
        Data.temp_date = String.valueOf(pData.getDate());

        payment_patientID.setText(String.valueOf(pData.getPatientID()));
        payment_name.setText(pData.getFullName());
        payment_doctor.setText(pData.getDoctor());
        payment_date.setText(String.valueOf(pData.getDate()));

    }

    //Method to check the payment
    @FXML public void paymentCheckOutBtn() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CheckOutPatient.fxml")));
            Stage stage = new Stage();

            stage.setTitle("Hospital Management System | Check-Out");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to update the profile
    @FXML public void profileUpdateBtn() {
        connect = DBConnection.getConnection();
        if (profile_adminID.getText().isEmpty()
                || profile_username.getText().isEmpty()
                || profile_email.getText().isEmpty()
                || profile_status.getSelectionModel().getSelectedItem() == null) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            if (Data.path == null || Data.path.isEmpty()) {
                String updateData = "UPDATE admin SET username = ?, email = ?, gender = ? "
                        + "WHERE admin_id = " + Data.admin_id;

                try {
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, profile_username.getText());
                    prepare.setString(2, profile_email.getText());
                    prepare.setString(3, profile_status.getSelectionModel().getSelectedItem());

                    prepare.executeUpdate();

                    profileDisplayInfo();

                    alert.successMessage("Updated Successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                String updateData = "UPDATE admin SET username = ?, email = ?, image = ?, gender = ? "
                        + "WHERE admin_id = " + Data.admin_id;
                try {
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, profile_username.getText());
                    prepare.setString(2, profile_email.getText());

                    String path = Data.path;
                    path = path.replace("\\", "\\\\");
                    Path transfer = Paths.get(path);

                    Path copy = Paths.get("F:\\HospitalManagementSystem\\src\\main\\resources\\com\\example\\hospitalmanagementsystem\\"
                            + Data.admin_id + ".jpg");

                    Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);
                    prepare.setString(3, copy.toAbsolutePath().toString());
                    prepare.setString(4, profile_status.getSelectionModel().getSelectedItem());

                    prepare.executeUpdate();
                    profileDisplayInfo();
                    profileDisplayImages();
                    alert.successMessage("Updated Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //Method to display the profile images
    @FXML public void profileDisplayImages() {
        String selectData = "SELECT * FROM admin WHERE admin_id = " + Data.admin_id;
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
                    profile_circle.setFill(new ImagePattern(image));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to insert the profile image
    @FXML public void profileInsertImage() {
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

        File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

        if (file != null) {
            Data.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 137, 95, false, true);
            profile_circle.setFill(new ImagePattern(image));
        }

    }

    //Method to view the profile display
    @FXML public void profileDisplayInfo() {
        String sql = "SELECT * FROM admin WHERE admin_id = " + Data.admin_id;
        System.out.println(Data.admin_id);
        connect = DBConnection.getConnection();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                profile_adminID.setText(result.getString("admin_id"));
                profile_username.setText(result.getString("username"));
                profile_email.setText(result.getString("email"));
                profile_status.getSelectionModel().select(result.getString("gender"));

                profile_label_adminIO.setText(result.getString("admin_id"));
                profile_label_name.setText(result.getString("username"));
                profile_label_email.setText(result.getString("email"));
                profile_label_dateCreated.setText(result.getString("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to show the list of gender in combo box
    @FXML public void profileStatusList() {
        List<String> listS = new ArrayList<>();
        Collections.addAll(listS, Data.gender);
        ObservableList listData = FXCollections.observableArrayList(listS);
        profile_status.setItems(listData);
    }

    //Method to switch the different panes on the action of different buttons
    @FXML public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            doctors_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            payment_form.setVisible(false);
            profile_form.setVisible(false);

            dashboardAD();
            dashboardTP();
            dashboardAP();
            dashboardTA();
            dashboardGetDoctorDisplayData();

            current_form.setText("Dashboard Form");
        } else if (event.getSource() == doctors_btn) {
            dashboard_form.setVisible(false);
            doctors_form.setVisible(true);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            payment_form.setVisible(false);
            profile_form.setVisible(false);

            doctorDisplayData();
            doctorActionButton();

            current_form.setText("Doctor's Form");
        } else if (event.getSource() == patients_btn) {
            dashboard_form.setVisible(false);
            doctors_form.setVisible(false);
            patients_form.setVisible(true);
            appointments_form.setVisible(false);
            payment_form.setVisible(false);
            profile_form.setVisible(false);

            patientDisplayData();
            patientActionButton();
            current_form.setText("Patient's Form");
        } else if (event.getSource() == appointments_btn) {
            dashboard_form.setVisible(false);
            doctors_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(true);
            payment_form.setVisible(false);
            profile_form.setVisible(false);

            appointmentDisplayData();

            current_form.setText("Appointment's Form");
        } else if (event.getSource() == payment_btn) {
            dashboard_form.setVisible(false);
            doctors_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            payment_form.setVisible(true);
            profile_form.setVisible(false);

            paymentDisplayData();

            current_form.setText("Payment Form");
        } else if (event.getSource() == profile_btn) {
            dashboard_form.setVisible(false);
            doctors_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            payment_form.setVisible(false);
            profile_form.setVisible(true);

            profileStatusList();
            profileDisplayInfo();
            profileDisplayImages();

            current_form.setText("Profile Form");
        }

    }

    //Method to display the admin id
    @FXML public void displayAdminIDUsername() {
        String sql = "SELECT * FROM admin WHERE username = '"
                + Data.admin_username + "'";

        connect = DBConnection.getConnection();

        try {

            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nav_adminID.setText(result.getString("admin_id"));
                String tempUsername = result.getString("username");
                tempUsername = tempUsername.substring(0, 1).toUpperCase() + tempUsername.substring(1); // TO SET THE FIRST LETTER TO UPPER CASE
                nav_username.setText(tempUsername);
                top_username.setText(tempUsername);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to perform the action of logout button
    @FXML public void logoutBtn() {

        try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                logout_btn.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Failed to logout");
        }

    }

    //Method to display the current date and time
    @FXML public void runTime() {

        new Thread(() -> {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            while (true) {
                try {

                    Thread.sleep(1000); // 1000 ms = 1s

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    date_time.setText(format.format(new Date()));
                });
            }
        }).start();

    }

    //This initializes all the methods when program will run
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime();
        displayAdminIDUsername();
        dashboardAD();
        dashboardTP();
        dashboardAP();
        dashboardTA();
        dashboardGetDoctorDisplayData();
        dashboardPatientDataChart();
        dashboardDoctorDataChart();
        doctorDisplayData();
        doctorActionButton();
        patientDisplayData();
        patientActionButton();
        appointmentDisplayData();
        appointmentActionButton();
        paymentDisplayData();

        profileStatusList();
        profileDisplayInfo();
        profileDisplayImages();
    }

}
