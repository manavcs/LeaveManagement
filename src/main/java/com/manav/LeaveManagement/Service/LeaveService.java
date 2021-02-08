package com.manav.LeaveManagement.Service;

import com.manav.LeaveManagement.Model.ApplyLeaveResponse;
import com.manav.LeaveManagement.Model.CancelLeaveResponse;
import com.manav.LeaveManagement.Model.Exception.LeaveAlreadyExistException;
import com.manav.LeaveManagement.Model.Exception.LeaveValidationException;
import com.manav.LeaveManagement.Model.Leave;
import com.manav.LeaveManagement.Model.LeaveStatus;
import com.manav.LeaveManagement.Respository.LeaveRepository;
import com.manav.LeaveManagement.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private Utils utils;

    public ApplyLeaveResponse applyLeave(String manager, String employee, String fromDate, String toDate)
            throws LeaveValidationException, LeaveAlreadyExistException {

        var noOfDays = utils.getNoOfDays(fromDate, toDate);

        System.out.println(String.format("Applying %d day leave by %s to %s",
                noOfDays, employee, manager));

        validateLeaves(manager, employee, fromDate, toDate, noOfDays);

        var existingLeaves = leaveRepository.findByEmployee(employee);
        var map = new HashMap<String, Boolean>();
        for(Leave lv:existingLeaves)
        {
            map.put(lv.date + lv.employee, true);
        }

        var leaves = new ArrayList<Leave>();

        for(int i = 0; i< noOfDays; i++) {
            Leave leave = createLeave(manager, employee, fromDate, map, i);
            leaves.add(leave);
        }

        leaveRepository.saveAll(leaves);

        System.out.println("Leaves applied successfully");
        return new ApplyLeaveResponse(true, "Leaves applied successfully");

    }

    public CancelLeaveResponse cancelLeaves(String employee, String fromDate, String toDate)
    {
        var frmDate = utils.getDateFromString(fromDate);
        var tDate = utils.getDateFromString(toDate);

        if(frmDate.before(new Date()))
        {
            System.out.println("Can not cancel the leaves with past date");
            return new CancelLeaveResponse(false, "Can not cancel the leaves with past date");
        }

        System.out.println(String.format("Cancelling leave for %s from %s to %s ", employee,
                fromDate.toString(), toDate.toString()));

        var leaves = this.leaveRepository.findByEmployee(employee);

        var appliedLeaves = leaves.stream()
                .filter(l-> (utils.getDateFromString(l.getDate()).after(frmDate)
                        || utils.getDateFromString(l.getDate()).equals(frmDate))
                        && (utils.getDateFromString(l.getDate()).before(tDate)
                        || utils.getDateFromString(l.getDate()).equals(tDate))).collect(Collectors.toList());


        if(appliedLeaves == null || appliedLeaves.size() == 0)
        {
            System.out.println("Error! Leave does not exists");
            return new CancelLeaveResponse(false, "Error! Leave does not exists");
        }
        else {
            this.leaveRepository.deleteAll(appliedLeaves);
            System.out.println("Leave cancelled successfully");
            return new CancelLeaveResponse(true, "Leave cancelled successfully");
        }
    }

    public Object[] getTotalPendingLeaves(String employee)
    {
        var leaves = this.leaveRepository.findByEmployee(employee);

        var pending = leaves.stream()
                .filter(l->l.getStatus() == LeaveStatus.PendingForApproval)
                .toArray();

        return pending;
    }

    public Object[] getTotalApprovedLeaves(String employee)
    {
        var leaves = this.leaveRepository.findByEmployee(employee);

        var approved = leaves.stream()
                .filter(l->l.getStatus() == LeaveStatus.Approved)
                .toArray();

        return approved;
    }

    private Leave createLeave(String manager, String employee, String fromDate,
                              HashMap<String, Boolean> map, int i) throws LeaveAlreadyExistException {

        var today = utils.getStringFromDate(new Date());

        var date = utils.addDays(fromDate, i);

        if(map.containsKey(date + employee))
        {
            throw new LeaveAlreadyExistException(String.format("{0}:{0}", employee, date));
        }

        var leave = new Leave(manager, employee, date, "");

        var daysRemaining = utils.getNoOfDays(today,date) -1;

        var status = getLeaveStatus(daysRemaining);

        leave.setStatus(status);

        return leave;
    }

    private LeaveStatus getLeaveStatus (long noOfDays) {
        if(noOfDays > 7)
        {
            return LeaveStatus.Approved;
        }
        else{
            return LeaveStatus.PendingForApproval;
        }
    }

    private boolean validateLeaves(String manager, String employee, String frmDate, String tDate, long noOfDays) throws LeaveValidationException {

        var fromDate = utils.getDateFromString(frmDate);
        var toDate = utils.getDateFromString(tDate);

        if(noOfDays <1)
        {
            System.out.println("Invalid leave!");
            throw new LeaveValidationException("Invalid leave!");
        }

        if(manager == null || manager.isEmpty() ||
                employee == null || employee.isEmpty()
                || fromDate == null || tDate == null)
        {
            System.out.println("Invalid leave!");
            throw new LeaveValidationException("Invalid leave!");
        }

        if(fromDate.after(toDate))
        {
            System.out.println("from date in a leave can not be greater than to date");
            throw new LeaveValidationException("from date in a leave can not be greater than to date");
        }

        if(fromDate.before(new Date()))
        {
            System.out.println("Leaves can not be applied to past dates");
            throw new LeaveValidationException("Leaves can not be applied to past dates");
        }

        return true;
    }
}
