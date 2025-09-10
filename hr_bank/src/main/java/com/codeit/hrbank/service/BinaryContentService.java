package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentCreateRequest;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContentDTO createBinaryContentFromFile(MultipartFile file);

  BinaryContentDTO createBinaryContentFromRequest(BinaryContentCreateRequest request);

  BinaryContentDTO getBinaryContent(Long id);

  List<BinaryContentDTO> getBinaryContentList(int size, Long idAfter);

  BinaryContentDTO updateBinaryContent(Long id, BinaryContentUpdateRequest request);

  void deleteBinaryContent(Long id);

  ResponseEntity<Resource> downloadBinaryContent(Long id);
}