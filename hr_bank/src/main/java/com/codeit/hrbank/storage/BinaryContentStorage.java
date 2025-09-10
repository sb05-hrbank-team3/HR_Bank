package com.codeit.hrbank.storage;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import java.io.InputStream;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  Long putFile(Long id, byte[] data, String FileName);

  InputStream getFile(Long id);

  ResponseEntity<Resource> downloadFile(BinaryContentDTO binaryContentDTO);

  void deleteFile(Long id);
}