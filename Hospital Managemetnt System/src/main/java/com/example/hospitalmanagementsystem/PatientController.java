package com.example.hospitalmanagementsystem;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PatientController implements Initializable {

    // All the components required to control the patient page
    @FXML private Label checkout_patientID;
    @FXML private Label checkout_name;
    @FXML private Label checkout_doctor;
    @FXML private DatePicker checkout_date;
    @FXML private ImageView checkout_imageView;
    @FXML private DatePicker checkout_checkout;
    @FXML private Label checkout_totalDays;
    @FXML private Label checkout_totalPrice;
    @FXML private Button checkout_payBtn;
    @FXML private Button checkout_countBtn;
    private Image image;

    //Alert message object creation for success, error or confirmation message
    private AlertMessage alert = new AlertMessage();

    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    //Method to calculate the amount of the patient
    public void countBtn() {
        long countDays = 0;
        if (checkout_date.getValue() != null || checkout_checkout.getValue() != null) {
            countDays
                    = ChronoUnit.DAYS.between(checkout_date.getValue(), checkout_checkout.getValue());
        } else {
            alert.errorMessage("Something went wrong.");
        }
        double price = 2000;
        double totalprice = (price * countDays);
        checkout_totalDays.setText(String.valueOf(countDays));
        checkout_totalPrice.setText(String.valueOf(totalprice));

    }

    //Method to pay the amount of the patient and confirmed
    public void payBtn() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        if (checkout_checkout.getValue() == null
                || checkout_totalDays.getText().isEmpty()
                || checkout_totalPrice.getText().isEmpty()) {
            alert.errorMessage("Invalid");
        } else {
                String sql = "INSERT INTO payment "
                        + "(patient_id, doctor, total_days, total_price, date, date_checkout, date_pay) "
                        + "VALUES(?,?,?,?,?,?,?)";

                connect = DBConnection.getConnection();

                try {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, checkout_patientID.getText());
                    prepare.setString(2, checkout_doctor.getText());
                    prepare.setString(3, checkout_totalDays.getText());
                    prepare.setString(4, checkout_totalPrice.getText());
                    prepare.setString(5, String.valueOf(checkout_date.getValue()));
                    prepare.setString(6, String.valueOf(checkout_checkout.getValue()));
                    prepare.setString(7, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    String updateData = "UPDATE patient SET status_pay = ? WHERE patient_id = "
                            + checkout_patientID.getText();

                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, "Paid");
                    prepare.executeUpdate();

                    alert.successMessage("Successful!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }

    }

    //Method to display the fields of the patient page
    public void displayFields() {

        checkout_patientID.setText(String.valueOf(Data.temp_PatientID));
        checkout_name.setText(Data.temp_name);
        checkout_doctor.setText(Data.temp_doctorID);
        checkout_date.setValue(LocalDate.parse(Data.temp_date));
        checkout_date.setDisable(true);

        image = new Image("File:" + Data.temp_path, 116, 132, false, true);
        checkout_imageView.setImage(image);

    }

    //Method to initialize the components on the initialization of the program
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayFields();
    }

}
