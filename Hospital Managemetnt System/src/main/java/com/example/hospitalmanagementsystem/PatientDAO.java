package com.example.hospitalmanagementsystem;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class PatientDAO {

    //All the components required to register or login the patient page
    @FXML private TextField patientFullName;
    @FXML private TextField patientRegisterID;
    @FXML private PasswordField patientRe_password;
    @FXML private TextField patientEmail;
    @FXML private CheckBox register_showPassword;
    @FXML private AnchorPane patientLoginForm;
    @FXML private AnchorPane patientRegisterForm;
    @FXML private PasswordField patientLogin_password;
    @FXML private TextField patientRegisterPasswordVisible;
    @FXML private Hyperlink patientRegisterLogin;
    @FXML private Hyperlink patientLoginRegister;
    @FXML private TextField patientLoginID;
    @FXML private CheckBox login_showPassword;
    @FXML private TextField patientLoginPasswordVisible;
    @FXML private Button logoutButton;

    //Database tools
    private final Connection conn = DBConnection.getConnection();
    private PreparedStatement stmt;
    private ResultSet result;

    //Alert message object creation for the success, error or confirmation message
    private final AlertMessage alertMessage = new AlertMessage();

    //Method to register the account of the patient
    @FXML public void registerAccount() {
        if (patientFullName.getText().isEmpty() || patientRegisterID.getText().isEmpty() ||
                patientRe_password.getText().isEmpty() || patientEmail.getText().isEmpty()) {
            alertMessage.errorMessage("Please fill all the fields.");
        }
        String checkUsername = "SELECT * FROM patient WHERE patientID = ?";

        try {
            stmt = Objects.requireNonNull(conn).prepareStatement(checkUsername);
            stmt.setString(1, patientRegisterID.getText());
            result = stmt.executeQuery();

            if (result.next()) {
                alertMessage.errorMessage(patientRegisterID.getText() + " already exists.");
            } else if (patientRe_password.getLength() < 8) {
                alertMessage.errorMessage("Invalid password, at least 8 characters required");
            } else {
                String insertData = "INSERT INTO patient (email, patientID, password, date, fullName, status) VALUES (?, ?, ?, ?, ?, ?)";
                Date date = new Date();
                stmt = conn.prepareStatement(insertData);
                stmt.setString(1, patientEmail.getText());
                stmt.setString(2, patientRegisterID.getText());
                stmt.setString(3, patientRe_password.getText());
                stmt.setDate(4, new java.sql.Date(date.getTime()));
                stmt.setString(5, patientFullName.getText());
                stmt.setString(6, "Confirmed");

                stmt.executeUpdate();
                alertMessage.successMessage("Registered Successfully!");
                patientLoginForm.setVisible(true);
                patientRegisterForm.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to show the password on the registration form of the patient
    @FXML public void showPassword() {
        if (register_showPassword.isSelected()) {
            patientRegisterPasswordVisible.setText(patientRe_password.getText());
            patientRegisterPasswordVisible.setVisible(true);
            patientRe_password.setVisible(false);
        } else {
            patientRe_password.setText(patientRegisterPasswordVisible.getText());
            patientRe_password.setVisible(true);
            patientRegisterPasswordVisible.setVisible(false);
        }
    }

    //Method to show the password on the login page of the patient
    @FXML public void showLoginPassword() {
        if (login_showPassword.isSelected()) {
            patientLoginPasswordVisible.setText(patientLogin_password.getText());
            patientLoginPasswordVisible.setVisible(true);
            patientLogin_password.setVisible(false);
        } else {
            patientLogin_password.setText(patientLoginPasswordVisible.getText());
            patientLogin_password.setVisible(true);
            patientLoginPasswordVisible.setVisible(false);
        }
    }

    //Method to login the account of the patient
    @FXML public void loginAccount() {
        if (patientLoginID.getText().isEmpty() || patientLogin_password.getText().isEmpty()) {
            alertMessage.errorMessage("Invalid username or password");
        }

        String sql = "SELECT * FROM patient WHERE patient_id = ? AND password = ?";

        try {
            if (conn != null) {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, patientLoginID.getText());
                stmt.setString(2, patientLogin_password.getText());
                result = stmt.executeQuery();
            }
            if (result.next()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientMainForm.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setTitle("Patient Page");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ((Stage) patientLoginID.getScene().getWindow()).close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alertMessage.errorMessage("Invalid username or password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to switch the registration and login form
    @FXML void switchForm(ActionEvent e) {
        if (e.getSource() == patientRegisterLogin) {
            patientRegisterForm.setVisible(false);
            patientLoginForm.setVisible(true);
        } else if (e.getSource() == patientLoginRegister) {
            patientLoginForm.setVisible(false);
            patientRegisterForm.setVisible(true);
        }
    }

    //Method to perform the action on the logout buttin
    @FXML void logout(ActionEvent event){
        if (event.getSource() == logoutButton){
            patientLoginForm.setVisible(true);
        }
    }
}

