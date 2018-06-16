package com.leanza.xpresseat.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by leanza on 29/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Driver {
    private String name;
    private String email;
    private String notify;
    private String status;
    private String van;

    public Driver() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVan() {
        return van;
    }

    public void setVan(String van) {
        this.van = van;
    }
}
