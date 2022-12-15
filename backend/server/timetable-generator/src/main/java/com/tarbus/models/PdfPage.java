package com.tarbus.models;

import com.tarbus.models.schedule.builder.ScheduleIdentifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class PdfPage {

  /**
   * Whether to save each subpage of a document separately, apart from the document itself
   */
  @Setter(AccessLevel.NONE)
  private boolean saveSinglePage;

  private List<ScheduleIdentifier> identifiers;

  private String workingDirectory;

  private String fileName;

  public void setFileName(String fileName) {
    this.saveSinglePage = true;
    this.fileName = fileName;
  }
}
