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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
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
   String uuid = UUID.randomUUID().toString();

    File csvFile = new File(csvDir, "employeeList" + uuid  + ".csv"); // 파일 이름 고정

    try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
      // 헤더 작성
      String[] header = {"ID", "직원번호", "이름", "이메일", "부서", "직급", "입사일", "상태"};
      writer.writeNext(header);


      // 데이터 작성
      for (Employee emp : employees) {
        LocalDate localDate = emp.getHireDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
        // yyyy-MM-dd 형식으로 포맷
        String formattedDate = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        String[] data = {
            emp.getId().toString(),
            emp.getEmployeeNumber(),
            emp.getName(),
            emp.getEmail(),
            emp.getDepartment().getName(),
            emp.getPosition(),
            formattedDate,
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
    BinaryContent result = binaryContentRepository.save(binaryContent);

    return result;
  }
}
