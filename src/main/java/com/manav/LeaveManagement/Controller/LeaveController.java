package com.manav.LeaveManagement.Controller;

import com.manav.LeaveManagement.Model.ApplyLeaveRequest;
import com.manav.LeaveManagement.Model.ApplyLeaveResponse;
import com.manav.LeaveManagement.Model.CancelLeaveRequest;
import com.manav.LeaveManagement.Model.CancelLeaveResponse;
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
}
