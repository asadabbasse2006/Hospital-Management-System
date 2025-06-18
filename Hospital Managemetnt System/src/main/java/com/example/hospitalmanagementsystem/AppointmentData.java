package com.example.hospitalmanagementsystem;

import java.sql.Date;
public class AppointmentData {

    //Data members of appointments
    private Integer id;
    private Integer appointmentID;
    private Integer patientID;
    private String name;
    private String gender;
    private String description;
    private String diagnosis;
    private String treatment;
    private Long mobileNumber;
    private String address;
    private Date date;
    private Date dateModify;
    private Date dateDelete;
    private String status;
    private String doctorID;
    private String specialized;
    private Date schedule;

    //Constructor of appointment data
    public AppointmentData(Integer id, Integer appointmentID, String name, String gender,
                           Long mobileNumber, String description, String diagnosis, String treatment, String address,
                           String doctorID, String specialized,
                           Date date, Date dateModify, Date dateDelete, String status, Date schedule) {
        this.id = id;
        this.appointmentID = appointmentID;
        this.name = name;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.description = description;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.address = address;
        this.doctorID = doctorID;
        this.specialized = specialized;
        this.date = date;
        this.dateModify = dateModify;
        this.dateDelete = dateDelete;
        this.status = status;
        this.schedule = schedule;

    }

    //Constructor of appointment data
    public AppointmentData(Integer appointmentID, String name, String gender,
                           Long mobileNumber, String description, String diagnosis, String treatment, String address,
                           Date date, Date dateModify, Date dateDelete, String status, Date schedule) {

        this.appointmentID = appointmentID;
        this.name = name;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.description = description;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.address = address;
        this.date = date;
        this.dateModify = dateModify;
        this.dateDelete = dateDelete;
        this.status = status;
        this.schedule = schedule;

    }

    //Constructor of appointment data
    public AppointmentData(Integer appointmentID, String name,
                           String description, Date date, String status) {
        this.appointmentID = appointmentID;
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    //Constructor of appointment data
    public AppointmentData(Integer appointmentID, String description,
                           String diagnosis, String treatment, String doctorID, Date schedule) {
        this.appointmentID = appointmentID;
        this.description = description;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.doctorID = doctorID;
        this.schedule = schedule;
    }

    public Integer getId() {
        return id;
    }

    //Getter and setter method of appointment data
    public Integer getAppointmentID() {
        return appointmentID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getAddress() {
        return address;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getSpecialized() {
        return specialized;
    }

    public Date getDate() {
        return date;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public Date getDateDelete() {
        return dateDelete;
    }

    public String getStatus() {
        return status;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public void setDateDelete(Date dateDelete) {
        this.dateDelete = dateDelete;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public void setSpecialized(String specialized) {
        this.specialized = specialized;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }
}
