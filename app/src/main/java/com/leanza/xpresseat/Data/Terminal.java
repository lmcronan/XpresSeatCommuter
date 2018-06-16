package com.leanza.xpresseat.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by leanza on 29/03/2018.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class Terminal {
    private String name;

    public Terminal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
