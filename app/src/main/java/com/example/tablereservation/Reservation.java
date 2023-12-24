package com.example.tablereservation;

public class Reservation {

    private String customerName;
    private String meal;
    private String seatingArea;
    private String phoneNumber;
    private String tableSize;
    private String date;

    public Reservation(String customerName, String meal, String seatingArea, String phoneNumber, String tableSize, String date) {
        this.customerName = customerName;
        this.meal = meal;
        this.seatingArea = seatingArea;
        this.phoneNumber = phoneNumber;
        this.tableSize = tableSize;
        this.date = date;
    }

    // Getter and setter methods

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getSeatingArea() {
        return seatingArea;
    }

    public void setSeatingArea(String seatingArea) {
        this.seatingArea = seatingArea;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTableSize() {
        return tableSize;
    }

    public void setTableSize(String tableSize) {
        this.tableSize = tableSize;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}