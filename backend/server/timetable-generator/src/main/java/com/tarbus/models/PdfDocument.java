package com.tarbus.models;

import com.tarbus.utilities.GenerateScheduleReport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PdfDocument {
  private String title;
  private List<PdfPage> pages;
  private String fileName;
  private String workingDirectory;
  private GenerateScheduleReport generateScheduleReport;

  @Override
  public String toString() {
    return "PdfDocument{" +
      "title='" + title + '\'' +
      ", pages=" + pages +
      ", fileName='" + fileName + '\'' +
      ", workingDirectory='" + workingDirectory + '\'' +
      '}';
  }
}
