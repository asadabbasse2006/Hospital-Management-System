package com.example.hospitalmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    //All the components required to control the forms
    @FXML private AnchorPane main_form;
    @FXML private AnchorPane login_form;
    @FXML private TextField login_username;
    @FXML private PasswordField login_password;
    @FXML private TextField login_showPassword;
    @FXML private CheckBox login_checkBox;
    @FXML private Button login_loginBtn;
    @FXML private ComboBox<?> login_user;
    @FXML private Hyperlink login_registerHere;
    @FXML private AnchorPane register_form;
    @FXML private TextField register_email;
    @FXML private TextField register_username;
    @FXML private PasswordField register_password;
    @FXML private TextField register_showPassword;
    @FXML private CheckBox register_checkBox;
    @FXML private Hyperlink register_loginHere;

    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    //Alert message object creation for success, error or confirmation message
    private final AlertMessage alert = new AlertMessage();

    //Method to login the admin account
    public void loginAccount() {

        if (login_username.getText().isEmpty()
                || login_password.getText().isEmpty()) {
            alert.errorMessage("Incorrect Username/Password");
        } else {

            String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

            connect = DBConnection.getConnection();

            try {

                if (!login_showPassword.isVisible()) {
                    if (!login_showPassword.getText().equals(login_password.getText())) {
                        login_showPassword.setText(login_password.getText());
                    }
                } else {
                    if (!login_showPassword.getText().equals(login_password.getText())) {
                        login_password.setText(login_showPassword.getText());
                    }
                }

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, login_username.getText());
                prepare.setString(2, login_password.getText());
                result = prepare.executeQuery();

                if (result.next()) {

                    Data.admin_username = login_username.getText();
                    Data.admin_id = Integer.parseInt(result.getString("admin_id"));

                    alert.successMessage("Login Successfully!");

                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminMainForm.fxml")));
                    Stage stage = new Stage();

                    stage.setTitle("Hospital Management System | Admin Portal");
                    stage.setScene(new Scene(root));
                    stage.show();

                    login_loginBtn.getScene().getWindow().hide();
                } else {
                    alert.errorMessage("Incorrect Username/Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //Method to show the login password
    public void loginShowPassword() {

        if (login_checkBox.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }

    }

    //Method to register the admin account
    public void registerAccount() {

        if (register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || register_password.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {

            String checkUsername = "SELECT * FROM admin WHERE username = '"
                    + register_username.getText() + "'";

            connect = DBConnection.getConnection();

            try {

                if (!register_showPassword.isVisible()) {
                    if (!register_showPassword.getText().equals(register_password.getText())) {
                        register_showPassword.setText(register_password.getText());
                    }
                } else {
                    if (!register_showPassword.getText().equals(register_password.getText())) {
                        register_password.setText(register_showPassword.getText());
                    }
                }

                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert.errorMessage(register_username.getText() + " is already exist!");
                } else if (register_password.getText().length() < 8) { // CHECK IF THE CHARACTERS OF THE PASSWORD IS LESS THAN TO 8
                    alert.errorMessage("Invalid Password, at least 8 characters needed");
                } else {
                    // TO INSERT THE DATA TO OUR DATABASE
                    String insertData = "INSERT INTO admin (email, username, password, date) VALUES(?,?,?,?)";

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, register_email.getText());
                    prepare.setString(2, register_username.getText());
                    prepare.setString(3, register_password.getText());
                    prepare.setString(4, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert.successMessage("Registered Successfully!");
                    registerClear();

                    // TO SWITCH THE FORM INTO LOGIN FORM
                    login_form.setVisible(true);
                    register_form.setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Method to clear the fields of the registration
    public void registerClear() {
        register_email.clear();
        register_username.clear();
        register_password.clear();
        register_showPassword.clear();
    }

    //Method to show the password of the registration form
    public void registerShowPassword() {

        if (register_checkBox.isSelected()) {
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        } else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }

    }

    //Method to select the user
    public void userList() {
        List<String> listU = Arrays.asList(Users.user);

        ObservableList listData = FXCollections.observableList(listU);
        login_user.setItems(listData);
    }

    //Method to switch the login page of the users
    public void switchPage() {

        if (login_user.getSelectionModel().getSelectedItem() == "Admin Portal") {

            try {

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")));
                Stage stage = new Stage();

                stage.setTitle("Hospital Management System");

                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (login_user.getSelectionModel().getSelectedItem() == "Doctor Portal") {

            try {

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DoctorPage.fxml")));
                Stage stage = new Stage();

                stage.setTitle("Hospital Management System");

                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (login_user.getSelectionModel().getSelectedItem() == "Patient Portal") {

            try {

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PatientPage.fxml")));
                Stage stage = new Stage();

                stage.setTitle("Hospital Management System");

                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        login_user.getScene().getWindow().hide();

    }

    //Method to switch the login and registration form
    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_registerHere) {
            // REGISTRATION FORM WILL SHOW
            login_form.setVisible(false);
            register_form.setVisible(true);
        } else if (event.getSource() == register_loginHere) {
            // LOGIN FORM WILL SHOW
            login_form.setVisible(true);
            register_form.setVisible(false);
        }

    }

    //Method to initialize the user list on the initialization of the program
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userList();
    }

}
