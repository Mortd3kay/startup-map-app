package com.skyletto.startappfrontend.data.requests;

public class RegisterDataRequest {
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String phoneNumber;

    public RegisterDataRequest(String email, String password, String firstName, String secondName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
    }

}
