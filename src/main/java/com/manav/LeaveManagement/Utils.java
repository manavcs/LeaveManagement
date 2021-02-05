package com.manav.LeaveManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class Utils {

    @Autowired
    private Environment env;

    public long getNoOfDays(Date date1, Date date2)
    {
        var dt1 = Instant.ofEpochMilli(date1.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        var dt2 = Instant.ofEpochMilli(date2.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        long diffDays = ChronoUnit.DAYS.between(dt1, dt2) + 1;

        return diffDays;
    }

    public LocalDate getLocalDate(Date dt)
    {
        return Instant.ofEpochMilli(dt.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Date getDateFromString(String str)
    {
        var format = env.getProperty("dateFormat");

        var dtFormat =  new SimpleDateFormat(format);

        return dtFormat.parse(str, new ParsePosition(0));
    }
}