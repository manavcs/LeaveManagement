package com.manav.LeaveManagement.Model;

public class ApplyLeaveResponse {
    public boolean status;
    public String message;
    public ApplyLeaveResponse(boolean st, String ms)
    {
        this.status = st;
        this.message = ms;
    }
}
