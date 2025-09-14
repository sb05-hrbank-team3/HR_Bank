package com.codeit.hrbank.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Csv {
  private Employee emp;
  private Department dept;
  private ChangeLog chg;
  private History hist;
}
