/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Location;
import entity.LocationInfo;
import entity.Weather;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jd140542d
 */
public class WeatherService {

    public static List<Weather> getAllWeathers() {
        List<Weather> list = new ArrayList<>();
        try {
            Connection c = db.DB.getInstance().getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT w.Id,w.Date,l.id,l.lat,l.lon,l.city,l.state FROM weather w, location l where l.id = w.idloc order by w.id");
            while (rs.next()) {
                Weather w = new Weather();
                Location l = new Location();
                w.setId(rs.getLong(1));
                w.setDate(rs.getDate(2));
                l.setId(rs.getLong(3));
                l.setLat(rs.getFloat(4));
                l.setLon(rs.getFloat(5));
                l.setCity(rs.getString(6));
                l.setState(rs.getString(7));
                w.setLocation(l);
                Statement s2 = c.createStatement();
                ResultSet rs2 = s2.executeQuery("SELECT hour, temperature FROM temperature where idwea = " + w.getId());
                while (rs2.next()) {
                    int hour = rs2.getInt(1);
                    float temp = rs2.getFloat(2);
                    w.getTemperature()[hour] = temp;
                }
                s2.close();
                list.add(w);
            }
            s.close();
            db.DB.getInstance().putConnection(c);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Weather> getWeathersByLocation(float lat, float lon) {
        List<Weather> list = new ArrayList<>();
        try {

            Connection c = db.DB.getInstance().getConnection();
            PreparedStatement s = c.prepareStatement("SELECT w.Id,w.Date,l.id,l.lat,l.lon,l.city,l.state FROM weather w, location l where l.id = w.idloc"
                    + " and l.lat<=?+0.000001 and l.lat>=?-0.00001 and l.lon<=?+0.000001 and l.lon>=?-0.00001 order by w.id");
            s.setFloat(1, lat);
            s.setFloat(2, lat);
            s.setFloat(3, lon);
            s.setFloat(4, lon);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                Weather w = new Weather();
                Location l = new Location();
                w.setId(rs.getLong(1));
                w.setDate(rs.getDate(2));
                l.setId(rs.getLong(3));
                l.setLat(rs.getFloat(4));
                l.setLon(rs.getFloat(5));
                l.setCity(rs.getString(6));
                l.setState(rs.getString(7));
                w.setLocation(l);
                Statement s2 = c.createStatement();
                ResultSet rs2 = s2.executeQuery("SELECT hour, temperature FROM temperature where idwea = " + w.getId());
                while (rs2.next()) {
                    int hour = rs2.getInt(1);
                    float temp = rs2.getFloat(2);
                    w.getTemperature()[hour] = temp;
                }
                s2.close();
                list.add(w);
            }
            s.close();
            db.DB.getInstance().putConnection(c);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<LocationInfo> getMinMaxForLocation(Date start, Date end) {
        List<LocationInfo> list = new ArrayList<LocationInfo>();
        try {
            Connection c = db.DB.getInstance().getConnection();
            Statement s = c.createStatement();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String startString = df.format(start);
            String endString = df.format(end);
            ResultSet rs = s.executeQuery("SELECT l.Lat, l.Lon, l.State, l.City FROM location l WHERE l.Id NOT iN "
                    + "(SELECT distinct l.id FROM location l, weather w "
                    + "WHERE l.Id = w.IdLoc AND w.Date >= '" + startString + "' AND w.Date <= '" + endString + "')");
            while (rs.next()) {
                LocationInfo li = new LocationInfo();
                li.setLat(rs.getFloat(1));
                li.setLon(rs.getFloat(2));
                li.setState(rs.getString(3));
                li.setCity(rs.getString(4));
                li.setMax(null);
                li.setMin(null);
                li.setMessage("There is no weather data in the given date range");
                list.add(li);
            }
            s.close();
            Statement s2 = c.createStatement();
            ResultSet rs2 = s2.executeQuery("SELECT l.lat,l.lon,l.state,l.city, MIN(t.temperature) as min, MAX(t.temperature) as max \n"
                    + "FROM location l, weather w, temperature t\n"
                    + "WHERE l.id = w.idloc\n"
                    + "AND w.date >= '" + startString + "'\n"
                    + "AND w.date <= '" + endString + "'\n"
                    + "GROUP BY l.id");
            while (rs2.next()) {
                LocationInfo li = new LocationInfo();
                li.setLat(rs2.getFloat(1));
                li.setLon(rs2.getFloat(2));
                li.setState(rs2.getString(3));
                li.setCity(rs2.getString(4));
                li.setMax(rs2.getFloat(5));
                li.setMin(rs2.getFloat(6));
                li.setMessage(null);
                list.add(li);
            }
            s2.close();
            db.DB.getInstance().putConnection(c);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;

    }

    public static void deleteAll() {
        try {
            Connection c = db.DB.getInstance().getConnection();
            Statement s2 = c.createStatement();
            s2.executeUpdate("DELETE FROM temperature");
            s2.close();
            Statement s3 = c.createStatement();
            s3.executeUpdate("DELETE FROM weather");
            s3.close();
            db.DB.getInstance().putConnection(c);
        } catch (SQLException ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void delete(String start, String end, float lat, float lon) {
        try {
            Connection c = db.DB.getInstance().getConnection();
            Statement s = c.createStatement();
            String str = "SELECT w.id FROM weather w, location l WHERE l.lat >= " + (lat - 0.0001) + " AND l.lat<=" + (lat + 0.0001) + " AND l.lon>=" + (lon - 0.0001) + " AND l.lon<=" + (lon + 0.0001) + " AND w.Date>='" + start + "' AND w.Date<='" + end + "' AND l.id = w.idLoc";
            ResultSet rs = s.executeQuery("SELECT w.id FROM weather w, location l WHERE l.lat >= " + (lat - 0.0001) + " AND l.lat<=" + (lat + 0.0001) + " AND l.lon>=" + (lon - 0.0001) + " AND l.lon<=" + (lon + 0.0001) + " AND w.Date>='" + start + "' AND w.Date<='" + end + "' AND l.id = w.idLoc");
            while (rs.next()) {
                Statement s2 = c.createStatement();
                s2.executeUpdate("DELETE FROM temperature WHERE idWea = " + rs.getInt(1));
                s2.close();
                Statement s3 = c.createStatement();
                s3.executeUpdate("DELETE FROM weather WHERE id = " + rs.getInt(1));
                s3.close();
            }
            s.close();
            db.DB.getInstance().putConnection(c);
        } catch (SQLException ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
