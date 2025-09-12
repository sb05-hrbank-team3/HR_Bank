package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.EmployeeDistributionDTO;
import com.codeit.hrbank.dto.data.EmployeeTrendDTO;
import com.codeit.hrbank.entity.DateUnit;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.EmployeeGroupBy;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.service.EmployeeAnalyticsService;
import com.querydsl.core.Tuple;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
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
public class EmployeeAnalyticsServiceImpl implements EmployeeAnalyticsService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;


  @Override
  public List<EmployeeTrendDTO> getTrend(LocalDate from, LocalDate to, String unit, ZoneId zone) {
    DateUnit dateUnit = DateUnit.from(unit); // 문자열을 enum으로 안전 변환 (null -> month)

    LocalDate toDate = (to == null) ? LocalDate.now(zone) : to;

    // from 기본값 : to에서 12구간
    LocalDate defaultFrom = switch(dateUnit){
      case day -> toDate.minusDays(12);
      case week -> toDate.minusWeeks(12);
      case month -> toDate.minusMonths(12);
      case quarter -> toDate.minusMonths(12 * 3);
      case year -> toDate.minusYears(12);
    };
    LocalDate fromDate = (from == null) ? defaultFrom : from;

    // 버킷 시작일 목록
    List<LocalDate> starts = buildBucketStarts(fromDate, toDate, dateUnit);

    List<Long> counts = new ArrayList<>(starts.size());

    for (int i = 0; i < starts.size(); i++) {
      LocalDate start = starts.get(i);
      LocalDate nextStart = bump(start, dateUnit);
      // 마지막 버킷의 nextStart가 toDate를 넘으면 toDate+1로 보정
      LocalDate endExclusive = (i + 1 < starts.size()) ? nextStart : toDate.plusDays(1);

      LocalDate ref = endExclusive.minusDays(1); // 구간 끝
      counts.add(employeeRepository.countEmployeesAt(ref)); // 구간 끝 직원수
    }

    // 증감 수/ 증감률 계산 후 DTO로 변환
    List<EmployeeTrendDTO> result = new ArrayList<>(starts.size());
    for (int i = 0; i < starts.size(); i++) {
      long current = counts.get(i);
      long previous = (i == 0) ? 0 : counts.get(i - 1);
      long change = current - previous;
      double changeRate = (previous == 0) ? 0.0 :  Math.round((change * 100.0 / previous) * 10.0) / 10.0;

      result.add(new EmployeeTrendDTO(starts.get(i), current, change, changeRate));
    }
    return result;
  }

  @Override
  public List<EmployeeDistributionDTO> getDistribution(EmployeeGroupBy groupBy, EmployeeStatus status) {
    if (groupBy == EmployeeGroupBy.DEPARTMENT) {
      return distributionByDepartment(status);
    } else {
      return distributionByPosition(status);
    }
  }

  private List<LocalDate> buildBucketStarts(LocalDate from, LocalDate to, DateUnit unit) {
    List<LocalDate> list = new ArrayList<>();
    LocalDate cursor = alignToUnitStart(from, unit);
    while (!cursor.isAfter(to)) {
      list.add(cursor);
      cursor = bump(cursor, unit);
    }
    return list;
  }

  private LocalDate alignToUnitStart(LocalDate d, DateUnit unit) {
    return switch (unit) {
      case day -> d;
      case week -> d.minusDays((d.getDayOfWeek().getValue() + 6) % 7); // 월요일 시작
      case month -> d.withDayOfMonth(1);
      case quarter -> d.withMonth(((d.getMonthValue()-1)/3)*3 + 1).withDayOfMonth(1);
      case year -> d.withDayOfYear(1);
    };
  }

  private LocalDate bump(LocalDate d, DateUnit unit) {
    return switch (unit) {
      case day -> d.plusDays(1);
      case week -> d.plusWeeks(1);
      case month -> d.plusMonths(1);
      case quarter -> d.plusMonths(3);
      case year -> d.plusYears(1);
    };
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
    if (total == 0)
      return 0.0;
    return BigDecimal.valueOf(part)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP)
        .doubleValue();
  }

}