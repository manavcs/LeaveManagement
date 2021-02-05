package com.manav.LeaveManagement.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.rmi.server.UID;
import java.time.LocalDate;

@Entity
public class Leave {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String manager;
    private String employee;
    private String reason;
    private LocalDate date;
    private LeaveStatus status;

    public Leave(String manager, String employee, LocalDate date, String reason)
    {
        this.manager = manager;
        this.employee = employee;
        this.date = date;
        this.reason = reason;
    }

    public Integer getId()
    {
        return id;
    }

    public String getManager()
    {
        return manager;
    }

    public String getEmployee()
    {
        return employee;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public LeaveStatus getStatus()
    {
        return status;
    }

    public void setStatus(LeaveStatus status)
    {
        this.status = status;
    }

}
