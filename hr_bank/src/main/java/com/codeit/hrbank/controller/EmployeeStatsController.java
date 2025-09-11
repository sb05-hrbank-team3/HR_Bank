package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.EmployeeDistributionDTO;
import com.codeit.hrbank.dto.data.EmployeeTrendDTO;
import com.codeit.hrbank.entity.EmployeeGroupBy;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.service.EmployeeDistributionService;
import com.codeit.hrbank.service.EmployeeTrendService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees/stats")
public class EmployeeStatsController {

  private final EmployeeTrendService trendService;
  private final EmployeeDistributionService distributionService;

  @GetMapping("/trend")
  public List<EmployeeTrendDTO> trend(
      @RequestParam(required = false) LocalDate from,
      @RequestParam(required = false) LocalDate to,
      @RequestParam(required = false, defaultValue = "month") String unit
  ){
    return trendService.getTrend(from,to,unit, ZoneId.systemDefault());
  }


  @GetMapping("/distribution")
  public List<EmployeeDistributionDTO> distribution(
      @RequestParam(required = false, defaultValue = "department") String groupBy,
      @RequestParam(required = false, defaultValue = "ACTIVE")EmployeeStatus status
  ){
    try {
      return distributionService.getDistribution(EmployeeGroupBy.fromString(groupBy), status);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

}
