package com.manav.LeaveManagement.Model;

public class CancelLeaveResponse {
    public boolean status;
    public String message;
    public CancelLeaveResponse(boolean st, String ms)
    {
        this.status = st;
        this.message = ms;
    }
}
