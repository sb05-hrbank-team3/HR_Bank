package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import com.codeit.hrbank.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @PostMapping(consumes = {"multipart/form-data"})
  public BinaryContentDTO uploadFile(@RequestPart("file") MultipartFile file) {
    BinaryContentDTO dto = binaryContentService.createBinaryContentFromFile(file);
    return dto;
  }

  @GetMapping("/{id}")
  public BinaryContentDTO findFile(@PathVariable Long id) {
    return binaryContentService.getBinaryContent(id);
  }

  @GetMapping
  public List<BinaryContentDTO> findFiles(@RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) Long idAfter) {
    return binaryContentService.getBinaryContentList(size, idAfter);
  }

  @PatchMapping("/{id}")
  public BinaryContentDTO updateFileName(@PathVariable Long id,
      @RequestBody BinaryContentUpdateRequest request) {
    return binaryContentService.updateBinaryContent(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteFile(@PathVariable Long id) {
    binaryContentService.deleteBinaryContent(id);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
    return binaryContentService.downloadBinaryContent(id);
  }
}
