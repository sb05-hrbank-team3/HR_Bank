package com.codeit.hrbank.controller;

import com.codeit.hrbank.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
@Tag(name = "파일 다운로드")
public class DownloadController {

  private final BinaryContentService binaryContentService;

  @Operation(
      summary = "파일 다운로드",
      description = "파일 ID로 서버에 저장된 파일을 다운로드합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "다운로드 성공"),
          @ApiResponse(responseCode = "404", description = "파일 없음"),
          @ApiResponse(responseCode = "500", description = "서버 오류")
      }
  )
  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> downloadFile(
      @Parameter(description = "다운로드할 파일 ID", example = "1") @PathVariable Long id
  ) {
    return binaryContentService.downloadBinaryContent(id);
  }
}
