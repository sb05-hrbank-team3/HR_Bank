package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import com.codeit.hrbank.service.BinaryContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

}
