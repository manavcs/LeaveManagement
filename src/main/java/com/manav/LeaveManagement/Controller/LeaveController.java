package com.manav.LeaveManagement.Controller;

import com.manav.LeaveManagement.Model.*;
import com.manav.LeaveManagement.Model.Exception.LeaveAlreadyExistException;
import com.manav.LeaveManagement.Model.Exception.LeaveValidationException;
import com.manav.LeaveManagement.Service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    LeaveService leaveService;

    @PostMapping("/apply")
    public ResponseEntity applyLeave(@RequestBody ApplyLeaveRequest request)
    {
        try{
            var res = leaveService.applyLeave(request.manager,
                    request.employee, request.fromDate, request.toDate);

            return ResponseEntity.ok(res);
        }
        catch (LeaveValidationException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (LeaveAlreadyExistException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

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
