package com.codeit.hrbank.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class LocalDateToInstantDeserializer extends JsonDeserializer<Instant> {
  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String dateStr = p.getText();
    if (dateStr == null || dateStr.isBlank()) return null;

    LocalDate localDate = LocalDate.parse(dateStr); // "yyyy-MM-dd"
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

}
