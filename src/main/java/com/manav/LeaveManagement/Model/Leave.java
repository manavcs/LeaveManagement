package com.manav.LeaveManagement.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@IdClass(Leave.class)
public class Leave implements Serializable {
    @Id
    public String employee;
    @Id
    public String date;
    private String reason;
    private LeaveStatus status;
    private String manager;

    public Leave(String manager, String employee, String date, String reason)
    {
        this.manager = manager;
        this.employee = employee;
        this.date = date;
        this.reason = reason;
    }

    public  Leave()
    {

    }

    public String getManager()
    {
        return manager;
    }

    public String getEmployee()
    {
        return employee;
    }

    public String getDate() { return date; }

    public LeaveStatus getStatus()
    {
        return status;
    }

    public void setStatus(LeaveStatus status)
    {
        this.status = status;
    }

}
