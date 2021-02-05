package com.manav.LeaveManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class Utils {

    @Autowired
    private Environment env;

    public long getNoOfDays(String date1, String date2){

        var format = env.getProperty("dateFormat");
        var dtFormat =  new SimpleDateFormat(format);

        try {

            Date d1 = dtFormat.parse(date1);
            Date d2 = dtFormat.parse(date2);

            long diff = d2.getTime() - d1.getTime();

            var days =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            return days + 1;

        } catch (ParseException ex) {
            System.out.println("Error while getting no of days between two dates");
            return -1;
        }
    }

    public Date getDateFromString(String str)
    {
        var format = env.getProperty("dateFormat");

        var dtFormat =  new SimpleDateFormat(format);

        try{
            return dtFormat.parse(str);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public String getStringFromDate(Date dt)
    {
        var format = env.getProperty("dateFormat");

        var dtFormat =  new SimpleDateFormat(format);

        try{
            return dtFormat.format(dt);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public String addDays(String date, int days){
        var dt = getDateFromString(date);

        if(dt == null)
        {
            return  null;
        }

        long ltime= dt.getTime() + days * 24*60*60*1000;

        var newDate = new Date(ltime);

        return getStringFromDate(newDate);
    }
}