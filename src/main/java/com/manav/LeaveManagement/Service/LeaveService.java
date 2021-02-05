package com.manav.LeaveManagement.Service;

import com.manav.LeaveManagement.Model.ApplyLeaveResponse;
import com.manav.LeaveManagement.Model.CancelLeaveResponse;
import com.manav.LeaveManagement.Model.Leave;
import com.manav.LeaveManagement.Model.LeaveStatus;
import com.manav.LeaveManagement.Respository.LeaveRepository;
import com.manav.LeaveManagement.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private Utils utils;

    public ApplyLeaveResponse applyLeave(String manager, String employee, String frmDate, String tDate)
    {
        var fromDate = utils.getDateFromString(frmDate);
        var toDate = utils.getDateFromString(tDate);

        var noOfDays = utils.getNoOfDays(fromDate, toDate);

        System.out.println(String.format("Applying %d day leave by %s to %s",
                noOfDays, manager, employee));

        var valid = validateLeaves(manager, employee, fromDate, toDate);

        if(!valid)
        {
            return new ApplyLeaveResponse(false, "Invalid leaves");
        }

        var leaveStatus = getLeaveStatus(fromDate, toDate);

        for(int i = 0; i< noOfDays; i++)
        {
            LocalDate dt1 = utils.getLocalDate(fromDate);

            var leaveDate = dt1.plusDays(i);

            var leave = new Leave(manager, employee, leaveDate, "");
            leave.setStatus(leaveStatus);

            System.out.println(String.format("Leave is %s", leave.getStatus().toString()));

            this.leaveRepository.save(leave);

        }

        System.out.println("Leave applied successfully");
        return new ApplyLeaveResponse(true, "Leave applied successfully");

    }

    public CancelLeaveResponse cancelLeaves(String employee, Date fromDate, Date toDate)
    {
        LocalDate frmDate = utils.getLocalDate(fromDate);
        LocalDate tDate = utils.getLocalDate(toDate);

        if(fromDate.before(new Date()))
        {
            System.out.println("Can not cancel the leaves with past date");
            return new CancelLeaveResponse(false, "Can not cancel the leaves with past date");
        }

        System.out.println(String.format("Cancelling leave for %s from %s to %s ", employee,
                fromDate.toString(), toDate.toString()));

        var leaves = this.leaveRepository.findByEmployee(employee);

        var appliedLeaves = (Leave[])leaves.stream()
                .filter(l-> (l.getDate().isAfter(frmDate)
                        || l.getDate().equals(frmDate))
                        && (l.getDate().isBefore(tDate)
                        || l.getDate().equals(tDate))).toArray();


        if(appliedLeaves == null || appliedLeaves.length == 0)
        {
            System.out.println("Error! Leave does not exists");
            return new CancelLeaveResponse(false, "Error! Leave does not exists");
        }
        else {
            this.leaveRepository.deleteAll(Arrays.asList(appliedLeaves));
            System.out.println("Leave cancelled successfully");
            return new CancelLeaveResponse(true, "Leave cancelled successfully");
        }
    }

    public long getTotalPendingLeaves(String employee)
    {
        var leaves = this.leaveRepository.findByEmployee(employee);

        var pending = leaves.stream()
                .filter(l->l.getStatus() == LeaveStatus.PendingForApproval)
                .count();

        return pending;
    }

    public long getTotalApprovedLeaves(String employee)
    {
        var leaves = this.leaveRepository.findByEmployee(employee);

        var pending = leaves.stream()
                .filter(l->l.getStatus() == LeaveStatus.Approved)
                .count();

        return pending;
    }

    private LeaveStatus getLeaveStatus (Date fromDate, Date toDate) {
        if(utils.getNoOfDays(fromDate, toDate) >= 7)
        {
            return LeaveStatus.Approved;
        }
        else{
            return LeaveStatus.PendingForApproval;
        }
    }

    private boolean validateLeaves(String manager, String employee, Date fromDate, Date toDate) {
        if(manager == null || manager.isEmpty() ||
                employee == null || employee.isEmpty()
                || fromDate == null || toDate == null)
        {
            System.out.println("Invalid leave!");
            return false;
        }
        if(fromDate.after(toDate))
        {
            System.out.println("from date in a leave can not be greater than to date");
            return false;
        }
        if(fromDate.before(new Date()))
        {
            System.out.println("Leaves can not be applied to past dates");
            return false;
        }

        return true;
    }
}
