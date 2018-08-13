/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.api;

import entity.Location;
import entity.LocationInfo;
import entity.Weather;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import services.WeatherService;

/**
 *
 * @author Dusan13
 */
@Path("weather")
public class WeatherResources {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@DefaultValue("9999") @QueryParam("lat") float lat, @DefaultValue("9999") @QueryParam("lon") float lon) {
        List<Weather> list;
        if (lat == 9999 || lon == 9999) {
            list = WeatherService.getAllWeathers();
            GenericEntity<List<Weather>> genList = new GenericEntity<List<Weather>>(list) {
            };
            return Response.status(200).entity(genList).build();
        } else {
            list = WeatherService.getWeathersByLocation(lat, lon);
            GenericEntity<List<Weather>> genList = new GenericEntity<List<Weather>>(list) {
            };
            if (list.isEmpty()) {
                return Response.status(400).entity(genList).build();
            } else {
                return Response.status(200).entity(genList).build();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWeather(Weather weather) {
        try {
            //Check if Weather exists
            Connection c = db.DB.getInstance().getConnection();
            PreparedStatement s1 = c.prepareStatement("SELECT * FROM weather WHERE Id = ?");
            s1.setLong(1, weather.getId());
            ResultSet rs = s1.executeQuery();
            if (rs.next()) {
                return Response.status(400).build();
            }
            s1.close();
            //Check if Location exists
            PreparedStatement s2 = c.prepareStatement("SELECT * FROM location WHERE Lat >= ?-0.0001 and Lat <= ?+0.0001 and Lon >= ?-0.0001 and Lon <= ?+0.0001");
            Location location = weather.getLocation();
            s2.setDouble(1, location.getLat());
            s2.setDouble(2, location.getLat());
            s2.setDouble(3, location.getLon());
            s2.setDouble(4, location.getLon());

            ResultSet rs2 = s2.executeQuery();

            //Insert Location if not exists
            long IdLoc = 0L;
            if (!rs2.next()) {
                PreparedStatement s3 = c.prepareStatement("INSERT INTO location (Lat,Lon,City,State) VALUES (?,?,?,?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                s3.setDouble(1, location.getLat());
                s3.setDouble(2, location.getLon());
                s3.setString(3, location.getCity());
                s3.setString(4, location.getState());
                s3.executeUpdate();
                ResultSet idRS = s3.getGeneratedKeys();

                if (idRS.next()) {
                    IdLoc = idRS.getLong(1);
                }
                s3.close();
            } else {
                IdLoc = rs2.getLong(1);
            }
            s2.close();

            //Insert Weather
            PreparedStatement s4 = c.prepareStatement("INSERT INTO weather (Id,Date,IdLoc) VALUES (?,?,?)");
            s4.setLong(1, weather.getId());
            s4.setDate(2, weather.getDate());
            s4.setLong(3, IdLoc);
            s4.executeUpdate();
            s4.close();
            //Insert Temperatures
            PreparedStatement s5 = c.prepareStatement("INSERT INTO temperature (IdWea,hour,temperature) VALUES (?,?,?)");
            for (int i = 0; i < 24; i++) {
                s5.setLong(1, weather.getId());
                s5.setInt(2, i);
                s5.setFloat(3, weather.getTemperature()[i]);
                s5.executeUpdate();
            }
            s5.close();
            db.DB.getInstance().putConnection(c);
            //CODE::Okay
            return Response.status(201).build();
        } catch (SQLException ex) {
            Logger.getLogger(WeatherResources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(401).build();
    }

    @GET
    @Path("temperature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMinAndMax(@QueryParam("start") Date start, @QueryParam("end") Date end) {
        List<LocationInfo> list = WeatherService.getMinMaxForLocation(start, end);

        GenericEntity<List<LocationInfo>> genList = new GenericEntity<List<LocationInfo>>(list) {
        };
        return Response.status(200).entity(genList).build();
    }
}
