package com.tarbus.models;

import com.tarbus.models.schedule.builder.ScheduleIdentifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MultiPdfPage extends PdfPage {
  private List<SinglePdfPage> pages;

  public List<ScheduleIdentifier> getIdentifiers() {
    List<ScheduleIdentifier> identifiers = new ArrayList<>();
    for (SinglePdfPage page : pages) {
      identifiers.addAll(page.getIdentifiers());
    }
    return identifiers;
  }
}
