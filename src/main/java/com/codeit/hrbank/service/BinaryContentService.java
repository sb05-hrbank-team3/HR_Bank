package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentCreateRequest;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BinaryContentService {

  BinaryContentDTO create(MultipartFile file);

  BinaryContentDTO create(BinaryContentCreateRequest request);

  BinaryContentDTO get(Long id);

  List<BinaryContentDTO> list(int size, Long idAfter);

  BinaryContentDTO update(Long id, BinaryContentUpdateRequest request);

  void delete(Long id);

  ResponseEntity<Resource> download(Long id);
}