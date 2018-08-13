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
        
        if (Start.equals(defaultValue) && End.equals(defaultValue) && lat==defaultFloat && lon==defaultFloat) 
            WeatherService.deleteAll();
        else if (Start.equals(defaultValue) || End.equals(defaultValue) || lat==defaultFloat || lon==defaultFloat) {
            
        } else {
            WeatherService.delete(Start, End, lat, lon);
            return Response.status(200).build();
        }
        return Response.status(200).build();
    }
}
