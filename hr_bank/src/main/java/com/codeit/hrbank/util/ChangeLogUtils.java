package com.codeit.hrbank.util;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.History;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public static List<History> createHistoriesForUpdate(ChangeLog changeLog, Employee oldEmployee, Employee newEmployee) {
        List<History> histories = new ArrayList<>();
        Field[] fields = Employee.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object oldValue;
            Object newValue;
            try {
                oldValue = field.get(oldEmployee);
                newValue = field.get(newEmployee);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 접근 실패: " + field.getName(), e);
            }

            if (!Objects.equals(oldValue, newValue)) {
                histories.add(History.builder()
                        .propertyName(field.getName())
                        .before(oldValue != null ? oldValue.toString() : null)
                        .after(newValue != null ? newValue.toString() : null)
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
