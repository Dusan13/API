/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Date;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jd140542d
 */
@XmlRootElement
public class Weather {
    private long Id;
    private Date date;
    private Location location;
    private float[] temperature = new float[24];

    public Weather() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float[] getTemperature() {
        return temperature;
    }

    public void setTemperature(float[] temperature) {
        this.temperature = temperature;
    }
    
}
