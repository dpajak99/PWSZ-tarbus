package com.tarbus.utilities;

import com.tarbus.models.BusLine;
import com.tarbus.models.Company;
import com.tarbus.models.PdfPage;
import com.tarbus.models.schedule.builder.ScheduleIdentifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GenerateScheduleReport {
  private Company company;
  private Date startDate;
  private Date endDate;
  private BusLine busLine;
  private String additionalLines;
  private int pagesCount;

  public GenerateScheduleReport( GenerateScheduleReport other ) {
    this.company = other.company;
    this.startDate = other.startDate;
    this.endDate = other.endDate;
    this.busLine = other.busLine;
    this.additionalLines = other.additionalLines;
    this.pagesCount = other.pagesCount;
  }

  public void configureReport(ScheduleIdentifier identifier, List<PdfPage> documentPages) {
    this.pagesCount = documentPages.size();
    this.busLine = identifier.getRouteHtmlVariables().getBusLine();
    this.company = this.busLine.getVersion().getCompany();
    Set<BusLine> allBusLines = new HashSet<>();
    for( PdfPage page : documentPages ) {
      for( ScheduleIdentifier id : page.getIdentifiers() ) {
        allBusLines.add(id.getRouteHtmlVariables().getBusLine());
      }
    }
    this.additionalLines = allBusLines.stream().map(BusLine::getName).collect(Collectors.joining(", "));
  }
}
