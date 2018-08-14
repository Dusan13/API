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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    //GET:: SiteURL/weather (return all)
    //or
    //GET:: SiteURL/weather?lat=...&lon=... (return filtered data)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@DefaultValue("9999") @QueryParam("lat") String latStr, @DefaultValue("9999") @QueryParam("lon") String lonStr) {
        List<Weather> list;
        //if Param are in bad format
        float lat=0;
        float lon = 0;
        try {
           lat = Float.parseFloat(latStr);
           lon = Float.parseFloat(lonStr);
        }  catch (NumberFormatException nfe) {
            return Response.status(400).build();
        }
        //If some Param is missing
        if (lat == 9999 || lon == 9999) {
            return Response.status(400).build();
        } 
        //If all Params are there return Weathers for Location
        else {
            list = WeatherService.getWeathersByLocation(lat, lon);
            GenericEntity<List<Weather>> genList = new GenericEntity<List<Weather>>(list) {
            };
            if (list.isEmpty()) {
                return Response.status(404).entity(genList).build();
            } else {
                return Response.status(200).entity(genList).build();
            }
        }
    }
    
    //POST:: SiteURL/weather
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWeather(Weather weather) {
        int ret = WeatherService.InsertWeather(weather);
        return Response.status(ret).build();
    }
    
    //GET:: SiteURL/weather/temperature?start=...&end=...
    @GET
    @Path("temperature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMinAndMax(@QueryParam("start") String startStr, @QueryParam("end") String endStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date start, end;
        try {
            start = new Date(df.parse(startStr).getTime());
            end = new Date(df.parse(endStr).getTime());
        } catch (ParseException ex) {
            return Response.status(400).build();
        } 
        
        List<LocationInfo> list = WeatherService.getMinMaxForLocation(start, end);

        GenericEntity<List<LocationInfo>> genList = new GenericEntity<List<LocationInfo>>(list) {
        };
        return Response.status(200).entity(genList).build();
    }
}
