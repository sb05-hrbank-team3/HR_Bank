package com.codeit.hrbank.controller;


import com.codeit.hrbank.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class DownloadController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
    return binaryContentService.downloadBinaryContent(id);
  }

}
