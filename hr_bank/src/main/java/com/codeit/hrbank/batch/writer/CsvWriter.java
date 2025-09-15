package com.codeit.hrbank.batch.writer;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
public class CsvWriter implements ItemWriter<String[]> {

  @Getter
  private static File csvFile;


  public CsvWriter() throws IOException {
    String rootPath = System.getProperty("user.dir");
    File csvDir = new File(rootPath, "storage");

    if(!csvDir.exists()){
      csvDir.mkdirs();
    }
    String uuid = UUID.randomUUID().toString();
    this.csvFile = new File(csvDir, "backupList"+uuid+".csv");
    if(!csvFile.exists()){
      csvFile.delete();
    }

  }

  @Override
  public void write(Chunk<? extends String[]> chunk) throws Exception {
    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(
            new FileOutputStream(csvFile, true), StandardCharsets.UTF_8))) {
      int index = 0;

      if(index == 0){
        writer.writeNext(new String[] {"Employee"});
        String[] header = {"ID", "직원번호", "이름", "이메일", "부서", "직급", "입사일", "상태",
        };
        writer.writeNext(header);

      }else if(index == 1){
        writer.writeNext(new String[]{});
        writer.writeNext(new String[] {"ChangeLog"});
        String[] Cheader = {"ID", "Type", "IP", "직원번호","메모"};
        writer.writeNext(Cheader);

      }else if(index ==2){
        writer.writeNext(new String[]{});
        writer.writeNext(new String[] {"History"});
        String[] Hheader = {"Id", "property", "before_data", "after_data"};
        writer.writeNext(Hheader);
      }

      for (String[] item : chunk) {
        writer.writeNext(item);
      }

      index++;

    }
  }

}
