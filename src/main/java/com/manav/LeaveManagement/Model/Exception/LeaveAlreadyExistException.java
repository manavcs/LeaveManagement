package com.manav.LeaveManagement.Model.Exception;

public class LeaveAlreadyExistException extends Exception {
    public LeaveAlreadyExistException(String message)
    {
        super(message);
    }
}
