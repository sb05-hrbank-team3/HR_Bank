package com.codeit.hrbank.storage;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import java.io.InputStream;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  Long put(Long id, byte[] data);

  InputStream get(Long id);

  ResponseEntity<Resource> download(BinaryContentDTO binaryContentDTO);

  void delete(Long id);
}