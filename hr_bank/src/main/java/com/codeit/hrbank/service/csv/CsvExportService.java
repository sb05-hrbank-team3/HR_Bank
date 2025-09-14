package com.codeit.hrbank.service.csv;


import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Csv;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.History;
import com.codeit.hrbank.repository.BinaryContentRepository;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.repository.HistoryRepository;
import com.codeit.hrbank.storage.type.LocalBinaryContentStorage;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
  private final LocalBinaryContentStorage localBinaryContentStorage;
  private final DepartmentRepository departmentRepository;
  private final ChangeLogRepository changeLogRepository;
  private final HistoryRepository historyRepository;

  public BinaryContent exportEmployeesToCsv() throws IOException {
    List<Employee> employees = employeeRepository.findAll();
    List<Department>  departments = departmentRepository.findAll();
    List<ChangeLog> changeLogs = changeLogRepository.findAll();
    List<History> history = historyRepository.findAll();



    // 작업 디렉토리 루트
    String rootPath = System.getProperty("user.dir");
    File csvDir = new File(rootPath, "storage");

    if (!csvDir.exists()) {
      csvDir.mkdirs();
    }
   String uuid = UUID.randomUUID().toString();

    File csvFile = new File(csvDir, "backupList" + uuid  + ".csv"); // 파일 이름 고정

    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(
            new FileOutputStream(csvFile), StandardCharsets.UTF_8))) {
      // 헤더 작성
      writer.writeNext(new String[] {"Employee"});
      String[] header = {"ID", "직원번호", "이름", "이메일", "부서", "직급", "입사일", "상태",
      };
      writer.writeNext(header);

      // 데이터 작성
      // 직원
      for (Employee emp : employees) {
        // yyyy-MM-dd 형식으로 포맷
        String formattedDate = emp.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE);

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

      writer.writeNext(new String[]{});
      writer.writeNext(new String[] {"ChangeLog"});
      String[] Cheader = {"ID", "Type", "IP", "직원번호","메모"};
      writer.writeNext(Cheader);
      for (ChangeLog ch : changeLogs) {

        String[] data = {
            ch.getId().toString(),
            ch.getType().toString(),
            ch.getIpAddress(),
            ch.getEmployeeNumber(),
            ch.getMemo()
        };
        writer.writeNext(data);
      }

      writer.writeNext(new String[]{});
      writer.writeNext(new String[] {"History"});
      String[] Hheader = {"Id", "property", "before_data", "after_data"};
      writer.writeNext(Hheader);
      for (History his : history) {

        String[] data = {
            his.getId().toString(),
            his.getPropertyName(),
            his.getBefore(),
            his.getAfter(),
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

    localBinaryContentStorage.putFile(result.getId(), Files.readAllBytes(csvFile.toPath()) , csvFile.getName());


    return result;
  }
}
