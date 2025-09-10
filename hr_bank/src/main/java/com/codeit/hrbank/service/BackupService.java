package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.entity.BackupStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface BackupService {
  //  List<BackupDTO> findAllBackups();


  BackupDTO createBackup(HttpServletRequest request) throws IOException;


//  BackupDTO findLatestBackup(HttpServletRequest request , BackupStatus status);
}
