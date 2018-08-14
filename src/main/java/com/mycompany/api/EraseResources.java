/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.api;

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
    public Response delete(@DefaultValue("error") @QueryParam("start") String Start,
            @DefaultValue("error") @QueryParam("end") String End,
            @DefaultValue("9999") @QueryParam("lat") float lat,
            @DefaultValue("9999") @QueryParam("lon") float lon) {

        //If all Params are empty :: delete All
        if (Start.equals(defaultValue) && End.equals(defaultValue) && lat == defaultFloat && lon == defaultFloat) {
            WeatherService.deleteAll();
            return Response.status(200).build();
        } 
        //If Some are missing :: return Error 404
        else if (Start.equals(defaultValue) || End.equals(defaultValue) || lat == defaultFloat || lon == defaultFloat) {
            return Response.status(400).build();
        //If All params are ok :: delete Weather by Start, end, lat and lon
        } else {
            WeatherService.delete(Start, End, lat, lon);
            return Response.status(200).build();
        }

    }
}
