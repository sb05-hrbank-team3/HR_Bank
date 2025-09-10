package com.codeit.hrbank.service.csv;


import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.mapper.BinaryContentMapper;
import com.codeit.hrbank.repository.BinaryContentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CsvExportService {

  private final EmployeeRepository employeeRepository;
  private final BinaryContentRepository binaryContentRepository;

  public BinaryContent exportEmployeesToCsv() throws IOException {
    List<Employee> employees = employeeRepository.findAll();

    // 작업 디렉토리 루트
    String rootPath = System.getProperty("user.dir");
    File csvDir = new File(rootPath, "csv");

    if (!csvDir.exists()) {
      csvDir.mkdirs();
    }

    File csvFile = new File(csvDir, "employeeList.csv"); // 파일 이름 고정

    try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
      // 헤더 작성
      String[] header = {"ID", "직원번호", "이름", "이메일", "부서", "직급", "입사일", "상태"};
      writer.writeNext(header);

      // 데이터 작성
      for (Employee emp : employees) {
        String[] data = {
            emp.getId().toString(),
            emp.getEmployeeNumber(),
            emp.getName(),
            emp.getEmail(),
            emp.getDepartment().toString(),
            emp.getPosition(),
            emp.getHireDate().toString(),
            emp.getStatus().toString(),
        };
        writer.writeNext(data);
      }
    }

    BinaryContent binaryContent = BinaryContent.builder()
        .name(csvFile.getName())
        .size(csvFile.length())
        .contentType("test/csv")
        .build();
    BinaryContent bc = binaryContentRepository.save(binaryContent);

    return bc;
  }
}
