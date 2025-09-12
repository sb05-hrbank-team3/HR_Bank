package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import com.codeit.hrbank.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Tag(name = "파일 관리")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Operation(
      summary = "파일 업로드",
      description = "Multipart 파일을 업로드하고 정보를 반환합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "업로드 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 파일")
      }
  )
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BinaryContentDTO> uploadFile(@RequestPart("file") MultipartFile file) {
    BinaryContentDTO dto = binaryContentService.createBinaryContentFromFile(file);

    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @Operation(
      summary = "단일 파일 조회",
      description = "ID로 파일 정보를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "404", description = "파일 없음")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<BinaryContentDTO> findFile(@PathVariable("id") Long binaryContentId) {
    BinaryContentDTO binaryContent = binaryContentService.getBinaryContent(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @Operation(
      summary = "파일 목록 조회",
      description = "최대 size 만큼 파일 목록을 조회하며, idAfter로 커서 조회 가능",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  @GetMapping
  public ResponseEntity<List<BinaryContentDTO>> findFiles(
      @Parameter(description = "조회할 개수", example = "10")
      @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "이 ID 이후의 파일 조회용 커서", example = "5")
      @RequestParam(required = false) Long idAfter) {
    List<BinaryContentDTO> binaryContentList = binaryContentService.getBinaryContentList(size,
        idAfter);

    return ResponseEntity.status(HttpStatus.OK).body(binaryContentList);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BinaryContentDTO> updateFileName(
      @PathVariable("id") Long binaryContentId,
      @RequestBody BinaryContentUpdateRequest request) {
    BinaryContentDTO binaryContent = binaryContentService.updateBinaryContent(binaryContentId,
        request);

    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @Operation(
      summary = "파일 삭제",
      description = "ID를 기준으로 파일을 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "204", description = "삭제 성공"),
          @ApiResponse(responseCode = "404", description = "파일 없음")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFile(
      @Parameter(description = "삭제할 파일 ID", example = "1")
      @PathVariable("id") Long binaryContentId) {
    binaryContentService.deleteBinaryContent(binaryContentId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
