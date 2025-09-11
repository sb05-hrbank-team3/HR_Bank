package com.codeit.hrbank.entity;

public enum EmployeeGroupBy {
  DEPARTMENT, POSITION;

  public static EmployeeGroupBy fromString(String s) {
    if(s == null || s.isBlank()) return DEPARTMENT;
    return switch(s.toLowerCase()){
      case "department" -> DEPARTMENT;
      case "position" -> POSITION;
      default -> throw new IllegalArgumentException("지원하지 않는 groupBy: " + s);
    };
  }
}
