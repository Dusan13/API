/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dusan13
 */
@XmlRootElement
public class LocationInfo {

    private String state;
    private double Lat;
    private double Lon;
    private String City;
    private String message;
    private Float min;
    private Float max;

    public LocationInfo() {

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double Lat) {
        this.Lat = Lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double Lon) {
        this.Lon = Lon;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

}
