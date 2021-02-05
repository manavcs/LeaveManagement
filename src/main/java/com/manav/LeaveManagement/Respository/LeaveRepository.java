package com.manav.LeaveManagement.Respository;

import com.manav.LeaveManagement.Controller.LeaveController;
import com.manav.LeaveManagement.Model.Leave;
import com.manav.LeaveManagement.Model.LeaveStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface LeaveRepository extends CrudRepository<Leave, Integer>{
    List<Leave> findByEmployee(String employee);
    List<Leave> findByDateAndEmployee(String date, String employee);
}
