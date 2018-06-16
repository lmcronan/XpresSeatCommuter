package com.leanza.xpresseat.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by leanza on 29/03/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commuter {
    private String firstname;
    private String lastname;
    private String username;
    private String emailaddress;
    private String password;
    private String password2;

    public Commuter() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
