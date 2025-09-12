package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.EmployeeDistributionDTO;
import com.codeit.hrbank.dto.data.EmployeeTrendDTO;
import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.entity.EmployeeGroupBy;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public interface EmployeeAnalyticsService {

  List<EmployeeTrendDTO> getTrend(LocalDate from, LocalDate to, String unit, ZoneId zone);

  List<EmployeeDistributionDTO> getDistribution(EmployeeGroupBy groupBy, EmployeeStatus status);

}