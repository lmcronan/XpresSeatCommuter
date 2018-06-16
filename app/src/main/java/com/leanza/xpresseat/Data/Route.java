package com.leanza.xpresseat.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by leanza on 29/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private String name;
    private String test;
    private String url;
    private double originLat;
    private  double originLong;
    private  double destLat;
    private  double destLong;
    private String origin;
    private String dest;

    public Route() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLong() {
        return originLong;
    }

    public void setOriginLong(double originLong) {
        this.originLong = originLong;
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLong() {
        return destLong;
    }

    public void setDestLong(double destLong) {
        this.destLong = destLong;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
