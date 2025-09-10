package com.codeit.hrbank.util;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.History;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChangeLogUtils {

    public static ChangeLog createChangeLog(ChangeLogType type, String ipAddress, String memo, Employee employee) {
        return ChangeLog.builder()
                .type(type)
                .ipAddress(ipAddress)
                .memo(memo != null ? memo : "")
                .at(LocalDate.now())
                .employee(employee)
                .build();
    }

    public static List<History> createHistoriesForCreate(ChangeLog changeLog, Employee employee) {
        List<History> histories = new ArrayList<>();
        Field[] fields = Employee.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object newValue;
            try {
                newValue = field.get(employee);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 접근 실패: " + field.getName(), e);
            }

            if (newValue != null) {
                histories.add(History.builder()
                        .propertyName(field.getName())
                        .before(null)
                        .after(newValue.toString())
                        .changeLogs(changeLog)
                        .build());
            }
        }
        return histories;
    }

    // Employee의 값을 Map으로 추출
    public static Map<String, String> extractEmployeeValues(Employee employee) {
        Map<String, String> values = new HashMap<>();
        values.put("name", employee.getName());
        values.put("email", employee.getEmail());
        values.put("position", employee.getPosition());
        values.put("department", employee.getDepartment() != null ? employee.getDepartment().getName() : null);
        values.put("binaryContent", employee.getBinaryContent() != null ? employee.getBinaryContent().getName() : null);
        values.put("hireDate", employee.getHireDate() != null ? employee.getHireDate().toString() : null);
        return values;
    }

    // 변경된 값만 History 생성
    public static List<History> createHistoriesForUpdate(ChangeLog changeLog, Employee oldEmployee, Employee newEmployee) {
        Map<String, String> oldValues = extractEmployeeValues(oldEmployee);
        Map<String, String> newValues = extractEmployeeValues(newEmployee);

        List<History> histories = new ArrayList<>();
        for (String key : oldValues.keySet()) {
            String before = oldValues.get(key);
            String after = newValues.get(key);
            if (!Objects.equals(before, after)) {
                histories.add(History.builder()
                    .propertyName(key)
                    .before(before)
                    .after(after)
                    .changeLogs(changeLog)
                    .build());
            }
        }
        return histories;
    }


    // 삭제용: 삭제되는 직원의 모든 주요 필드를 before 값으로 기록
    public static List<History> createHistoriesForDelete(ChangeLog changeLog, Employee employee) {
        List<History> histories = new ArrayList<>();
        Field[] fields = Employee.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object oldValue;
            try {
                oldValue = field.get(employee);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 접근 실패: " + field.getName(), e);
            }

            if (oldValue != null) {
                histories.add(History.builder()
                        .propertyName(field.getName())
                        .before(oldValue.toString())
                        .after(null) // 삭제니까 after는 null
                        .changeLogs(changeLog)
                        .build());
            }
        }
        return histories;
    }
}
