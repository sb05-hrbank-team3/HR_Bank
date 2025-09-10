package com.codeit.hrbank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Column(nullable = false, length = 20)
  private String name;

  @Column(name = "employee_number", nullable = false, length = 100, unique = true)
  private String employeeNumber;

  @Column(name = "hire_date", nullable = false)
  private Instant hireDate;

  @Column(nullable = false, length = 30)
  private String position;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EmployeeStatus status;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "binary_content_id")
  private BinaryContent binaryContent;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "department_id", nullable = false)
  @JsonIgnore
  private Department department;
}
