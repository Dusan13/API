/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.api;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import services.WeatherService;

/**
 *
 * @author Dusan13
 */
@Path("erase")
public class EraseResources {

    private static final String defaultValue = "error";
    private static final float defaultFloat = 9999;

    @DELETE
    public Response delete(@DefaultValue("error") @QueryParam("start") String StartString,
            @DefaultValue("error") @QueryParam("end") String EndString,
            @DefaultValue("9999") @QueryParam("lat") String latString,
            @DefaultValue("9999") @QueryParam("lon") String lonString) {
        
        float lat = 0;
        float lon = 0;
        Date start;
        Date end;
        try {
            lat = Float.parseFloat(latString);
            lon = Float.parseFloat(lonString);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            start = new Date(df.parse(StartString).getTime());
            end = new Date(df.parse(EndString).getTime());
            
        } catch (NumberFormatException nfe) {
            return Response.status(400).build();
        } catch (ParseException ex) {
            return Response.status(400).build();
        }

        //If all Params a re empty :: delete All
        if (lat == defaultFloat && lon == defaultFloat) {
            WeatherService.deleteAll();
            return Response.status(200).build();
        } //If Some are missing :: return Error 404
        else if (lat == defaultFloat || lon == defaultFloat) {
            return Response.status(400).build();
            //If All params are ok :: delete Weather by Start, end, lat and lon
        } else {
            WeatherService.delete(StartString, EndString, lat, lon);
            return Response.status(200).build();
        }

    }
}
