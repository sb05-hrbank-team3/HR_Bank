package com.codeit.hrbank.storage.type;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "hrbank.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${hrbank.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Long putFile(Long binaryContentId, byte[] bytes, String fileName) {
    Path filePath = resolvePath(binaryContentId, fileName);
    if (Files.exists(filePath)) {
      throw new IllegalArgumentException("파일이 이미 존재합니다.");
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  @Override
  public InputStream getFile(Long binaryContentId) {
    Path filePath = findFileById(binaryContentId);
    if (filePath == null || Files.notExists(filePath)) {
      throw new NoSuchElementException("파일을 찾을 수 없습니다.");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(Long key, String fileName) {

    return root.resolve(key.toString() + "_" + fileName);
  }

  private Path findFileById(Long key) {

    try {
      return Files.list(root)
          .filter(p -> p.getFileName().toString().startsWith(key.toString() + "_"))
          .findFirst()
          .orElse(null);
    } catch (IOException e) {
      throw new RuntimeException("파일을 찾을 수 없습니다.");
    }
  }

  @Override
  public ResponseEntity<Resource> downloadFile(BinaryContentDTO metaData) {
    InputStream inputStream = getFile(metaData.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metaData.name() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size())).body(resource);
  }

  @Override
  public void deleteFile(Long id) {
    Path filePath = findFileById(id);
    if (filePath != null && Files.exists(filePath)) {
      try {
        Files.delete(filePath);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    } else {
      throw new NoSuchElementException("파일을 찾을 수 없습니다.");
    }
  }
}