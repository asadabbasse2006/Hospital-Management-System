package com.example.hospitalmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EditPatientFormController implements Initializable {

    //All the components required to control the update form of the patient
    @FXML private AnchorPane main_form;
    @FXML private TextField edit_patientID;
    @FXML private TextField edit_name;
    @FXML private ComboBox<String> edit_gender;
    @FXML private TextField edit_contactNumber;
    @FXML private TextArea edit_address;
    @FXML private ComboBox<String> edit_status;
    @FXML private Button edit_updateBtn;

    //Alert message object creation for success, error or either confirmation message
    private final AlertMessage alert = new AlertMessage();

    //Database tool
    private ResultSet result;

    //Method to update the profile
    public void updateBtn() {
        if (edit_patientID.getText().isEmpty() || edit_name.getText().isEmpty()
                || edit_gender.getSelectionModel().getSelectedItem() == null
                || edit_contactNumber.getText().isEmpty()
                || edit_address.getText().isEmpty()
                || edit_status.getSelectionModel().getSelectedItem() == null) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            String updateData = "UPDATE patient SET full_name = ?, gender = ?"
                    + ", mobile_number = ?, address = ?, status = ?, date_modify = ? "
                    + "WHERE patient_id = '"
                    + edit_patientID.getText() + "'";
            Connection connect = DBConnection.getConnection();
            try {

                PreparedStatement prepare = null;
                if (connect != null) {
                    prepare = connect.prepareStatement(updateData);
                }
                Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(1, edit_name.getText());
                    prepare.setString(2, edit_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(3, edit_contactNumber.getText());
                    prepare.setString(4, edit_address.getText());
                    prepare.setString(5, edit_status.getSelectionModel().getSelectedItem());
                    prepare.setString(6, String.valueOf(sqlDate));
                    prepare.executeUpdate();
                    alert.successMessage("Updated Successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //Method to set the fields on the patient form
    public void setField() {
        edit_patientID.setText(String.valueOf(Data.temp_PatientID));
        edit_name.setText(Data.temp_name);
        edit_gender.getSelectionModel().select(Data.temp_gender);
        edit_contactNumber.setText(String.valueOf(Data.temp_number));
        edit_address.setText(Data.temp_address);
        edit_status.getSelectionModel().select(Data.temp_status);
    }

    //Method to store the gender list in the combo box
    public void genderList() {
        List<String> genderL = new ArrayList<>();

        Collections.addAll(genderL, Data.gender);

        ObservableList listData = FXCollections.observableList(genderL);
        edit_gender.setItems(listData);
    }

    //Method to store the status type in the combo box
    public void statusList() {
        List<String> statusL = new ArrayList<>();

        Collections.addAll(statusL, Data.status);

        ObservableList listData = FXCollections.observableList(statusL);
        edit_status.setItems(listData);
    }

    //Method to display the following methods on the initialization of the program or either GUI
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setField();
        genderList();
        statusList();
    }

}
