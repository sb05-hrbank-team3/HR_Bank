package com.codeit.hrbank.entity;

public enum DateUnit {
  day, week, month, quarter, year;

  public static DateUnit from(String unitStr) {
    if(unitStr == null || unitStr.isBlank()) return month;
    return DateUnit.valueOf(unitStr.toLowerCase());
  }
}
