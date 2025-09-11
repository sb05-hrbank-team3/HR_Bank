package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.EmployeeTrendDTO;
import com.codeit.hrbank.entity.DateUnit;
import com.codeit.hrbank.repository.EmployeeRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeTrendService {

  private final EmployeeRepository employeeRepository;

  public List<EmployeeTrendDTO> getTrend(LocalDate from, LocalDate to, String unit, ZoneId zone){
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

      result.add(new EmployeeTrendDTO(starts.get(i).toString(), current, change, changeRate));
    }
    return result;
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

}
