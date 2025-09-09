package com.codeit.hrbank.entity;

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
@Table(name = "backups")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Backup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 20)
  private String worker;

  @Column(name = "started_at", nullable = false)
  private Instant startedAt;

  @Column(name = "ended_at", nullable = false)
  private Instant endedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BackupStatus status;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "binary_content_id", nullable = false)
  private BinaryContent file;
}
