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

    @DELETE
    public Response delete(@QueryParam("start") String StartString,
            @QueryParam("end") String EndString,
            @QueryParam("lat") String latString,
            @QueryParam("lon") String lonString) {
        
        //No params :: Delete All
        if (StartString == null && EndString == null && latString == null && lonString == null) {
            WeatherService.deleteAll();
            return Response.status(200).build();
        }
        //Some params are missing
        if (StartString == null || EndString == null || latString == null || lonString == null) {
            return Response.status(400).build();
        }

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

        WeatherService.delete(StartString, EndString, lat, lon);
        return Response.status(200).build();

    }
}
