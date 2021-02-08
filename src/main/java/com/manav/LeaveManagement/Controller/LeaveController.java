package com.manav.LeaveManagement.Controller;

import com.manav.LeaveManagement.Model.*;
import com.manav.LeaveManagement.Service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    LeaveService leaveService;

    @PostMapping("/apply")
    public ApplyLeaveResponse applyLeave(@RequestBody ApplyLeaveRequest request)
    {
        var res = leaveService.applyLeave(request.manager,
                request.employee, request.fromDate, request.toDate);

        return res;
    }

    @DeleteMapping("/cancel")
    public CancelLeaveResponse cancelLeave(@RequestBody CancelLeaveRequest request)
    {
        var res = leaveService.cancelLeaves(
                request.employee, request.fromDate, request.toDate);

        return res;
    }

    @GetMapping("/pending")
    public Object[] pendingLeave(String employee)
    {
        var res = leaveService.getTotalPendingLeaves(employee);

        return res;
    }

    @GetMapping("/approved")
    public Object[] approvedLeave(String employee)
    {
        var res = leaveService.getTotalApprovedLeaves(employee);

        return res;
    }
}
