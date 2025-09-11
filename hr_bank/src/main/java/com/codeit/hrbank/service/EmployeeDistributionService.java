package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.EmployeeDistributionDTO;
import com.codeit.hrbank.dto.data.KeyCount;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.EmployeeGroupBy;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.querydsl.core.Tuple;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDistributionService {

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;

  public List<EmployeeDistributionDTO> getDistribution(EmployeeGroupBy groupBy, EmployeeStatus status) {

    if (groupBy == EmployeeGroupBy.DEPARTMENT) {
      return distributionByDepartment(status);
    } else {
      return distributionByPosition(status);
    }
  }

  private List<EmployeeDistributionDTO> distributionByDepartment(EmployeeStatus status) {

    long total = employeeRepository.countEmployeesByStatus(status);
    if (total == 0L) return List.of();

    List<Department> departments = departmentRepository.findAll();
    if (departments.isEmpty()) return List.of();

    // id 집합을 만들어 한 번의 쿼리로 부서별 카운트를 받아옴 (N+1 방지)
    Set<Long> deptIds = new HashSet<>();
    departments.forEach(d -> deptIds.add(d.getId()));
    Map<Long, Long> counts = employeeRepository.countEmployeesByDepartmentIds(status, deptIds);


    List<EmployeeDistributionDTO> result = new ArrayList<>(departments.size());
    for (Department d : departments) {
      long countEmployees = counts.getOrDefault(d.getId(), 0L);        // 해당 부서 인원 (없으면 0)
      double percent = percent(countEmployees, total);                      // 소수 1자리 반올림
      result.add(new EmployeeDistributionDTO(d.getName(), countEmployees, percent));
    }

    result.sort(Comparator.comparingLong(EmployeeDistributionDTO::count).reversed());
    return result;
  }

  private List<EmployeeDistributionDTO> distributionByPosition(EmployeeStatus status) {

    long total = employeeRepository.countEmployeesByStatus(status);
    if (total == 0L) return List.of();


    List<Tuple> rows = employeeRepository.countEmployeesGroupByPosition(status);
    List<EmployeeDistributionDTO> result = new ArrayList<>(rows.size());
    for (Tuple t : rows) {
      Object keyObj = t.get(0, Object.class);             // 직무명(문자열/Enum/nullable)
      Long count      = t.get(1, Long.class);             // 인원 수
      String key      = (keyObj == null) ? "미지정"
          : (keyObj instanceof Enum<?> e) ? e.name() : keyObj.toString();
      double pct      = percent(count, total);
      result.add(new EmployeeDistributionDTO(key, count, pct));
    }
    return result;
  }

  private double percent(long part, long total) {
    if (total == 0) return 0.0;
    return BigDecimal.valueOf(part)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP)
        .doubleValue();
  }

}


