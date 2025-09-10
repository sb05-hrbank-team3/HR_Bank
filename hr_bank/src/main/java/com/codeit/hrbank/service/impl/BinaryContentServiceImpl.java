package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentCreateRequest;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.mapper.BinaryContentMapper;
import com.codeit.hrbank.repository.BinaryContentRepository;
import com.codeit.hrbank.service.BinaryContentService;
import com.codeit.hrbank.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage storage;
  private final BinaryContentMapper BinaryContentMapper;

  @Override
  public BinaryContentDTO createBinaryContentFromFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("파일이 비어있습니다.");
    }
    BinaryContent saved = binaryContentRepository.save(BinaryContent.builder()
        .name(file.getOriginalFilename())
        .size(file.getSize())
        .contentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
        .build());

    try {
      storage.putFile(saved.getId(), file.getBytes(), file.getOriginalFilename());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return BinaryContentMapper.toDTO(saved);
  }

  @Override
  public BinaryContentDTO createBinaryContentFromRequest(BinaryContentCreateRequest request) {
    if (request == null || request.bytes() == null) {
      throw new IllegalArgumentException("요청 바이트가 비어있습니다.");
    }
    BinaryContent saved = binaryContentRepository.save(BinaryContent.builder()
        .name(request.fileName())
        .size((long) request.bytes().length)
        .contentType(request.contentType() == null ? "application/octet-stream" : request.contentType())
        .build());
    storage.putFile(saved.getId(), request.bytes(), request.fileName() == null ? "알 수 없음" : request.fileName());
    return BinaryContentMapper.toDTO(saved);
  }

  @Override
  public BinaryContentDTO getBinaryContent(Long id) {
    BinaryContent entity = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("파일 메타데이터를 찾을 수 없습니다. id=" + id));
    return BinaryContentMapper.toDTO(entity);
  }

  @Override
  public List<BinaryContentDTO> getBinaryContentList(int size, Long idAfter) {
    List<BinaryContent> all = binaryContentRepository.findAll();
    return all.stream()
        .sorted(Comparator.comparing(BinaryContent::getId))
        .filter(e -> idAfter == null || e.getId() > idAfter)
        .limit(size <= 0 ? 10 : size)
        .map(BinaryContentMapper::toDTO)
        .toList();
  }

  @Override
  public BinaryContentDTO updateBinaryContent(Long id, BinaryContentUpdateRequest request) {
    BinaryContent entity = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("파일 메타데이터를 찾을 수 없습니다. id=" + id));

    if (request.name() != null && !request.name().isBlank()) {
      entity = BinaryContent.builder()
          .id(entity.getId())
          .name(request.name())
          .size(entity.getSize())
          .contentType(entity.getContentType())
          .build();
      entity = binaryContentRepository.save(entity);
    }
    return BinaryContentMapper.toDTO(entity);
  }

  @Override
  public void deleteBinaryContent(Long id) {
    binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("파일 메타데이터를 찾을 수 없습니다. id=" + id));
    storage.deleteFile(id);
    binaryContentRepository.deleteById(id);
  }

  @Override
  public ResponseEntity<Resource> downloadBinaryContent(Long id) {
    BinaryContentDTO dto = getBinaryContent(id);
    return storage.downloadFile(dto);
  }
}