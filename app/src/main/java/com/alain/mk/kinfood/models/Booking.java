package com.alain.mk.kinfood.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Booking {

    private String firstName;
    private String lastName;
    private String peopleNumber;
    private String phoneNumber;
    private String email;
    private String bookingDate;
    private String bookingHour;
    private Date dateCreated;
    private User userSender;
    private String userId;

    public Booking() {
    }

    public Booking(String firstName, String lastName, String peopleNumber, String phoneNumber, String email, String bookingDate, String bookingHour, User userSender, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.peopleNumber = peopleNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bookingDate = bookingDate;
        this.bookingHour = bookingHour;
        this.userSender = userSender;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(String peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingHour() {
        return bookingHour;
    }

    public void setBookingHour(String bookingHour) {
        this.bookingHour = bookingHour;
    }
    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
