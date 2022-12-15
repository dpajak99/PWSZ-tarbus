package com.tarbus.models.schedule.builder;

import com.tarbus.AppConfig;
import com.tarbus.config.UserDirectories;
import com.tarbus.models.*;
import com.tarbus.models.jpa.BusLineScheduleTemplate;
import com.tarbus.models.jpa.BusLineScheduleTemplateId;
import com.tarbus.models.jpa.CompanyScheduleTemplate;
import com.tarbus.models.jpa.CompanyScheduleTemplateId;
import com.tarbus.services.BusLineScheduleTemplateService;
import com.tarbus.services.CompanyScheduleTemplateService;
import com.tarbus.services.DepartureService;
import com.tarbus.utilities.GenerateScheduleReport;
import com.tarbus.utilities.HtmlTemplateFinder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class HtmlScheduleBuilder {
  @Getter
  @Setter
  protected TemplateSettings templateSettings;
  @Getter
  @Setter
  protected Company mainCompany;

  protected final DepartureService departureService;
  protected final AppConfig appConfig;
  protected final CompanyScheduleTemplateService companyScheduleTemplateService;
  protected final BusLineScheduleTemplateService busLineScheduleTemplateService;
  protected final UserDirectories userDirectories;
  protected GenerateScheduleReport generateScheduleReport = new GenerateScheduleReport();


  public HtmlScheduleBuilder(BusLineScheduleTemplateService busLineScheduleTemplateService, DepartureService departureService, AppConfig appConfig, CompanyScheduleTemplateService companyScheduleTemplateService, UserDirectories userDirectories) {
    this.departureService = departureService;
    this.appConfig = appConfig;
    this.companyScheduleTemplateService = companyScheduleTemplateService;
    this.userDirectories = userDirectories;
    this.busLineScheduleTemplateService = busLineScheduleTemplateService;
  }

  public abstract List<PdfDocument> generateScheduleTables(Company company, TemplateSettings templateSettings);

  protected void setMainCompany(Company company) {
    this.mainCompany = company;
    generateScheduleReport.setCompany(company);
  }

  protected RouteHtmlVariables getRouteHtmlVariables(RouteConnection connection, List<RouteConnection> routeConnections, TemplateSettings templateSettings) {
    Map<String, Object> scheduleDeparturesDestinations = departureService.getBusStopTimetable(connection.getBusStop().getId(), connection.getRoute().getId());
    RouteHtmlVariables templateStopData = new RouteHtmlVariables(appConfig, templateSettings);
    templateStopData.configureData(connection, routeConnections, scheduleDeparturesDestinations);
    return templateStopData;
  }

  protected SinglePdfPage preparePdfPageData(BusStop stop, ScheduleVersion version, List<RouteHtmlVariables> variables) {
    HtmlTemplateFinder htmlTemplateFinder = getRouteScheduleTemplate(variables.get(0).getRoute());
    TableBusStopData tableBusStopData = new TableBusStopData(templateSettings, appConfig);
    tableBusStopData.configureData(stop, version, variables);

    SinglePdfPage page = new SinglePdfPage(htmlTemplateFinder, tableBusStopData.getScheduleVariables());
    page.setWorkingDirectory(userDirectories.getCompanyDrivePath(version.getCompany().getId()) + "/schedule/stop/" + stop.getId() + "/");
    page.setFileName(version.getId().toString());
    page.setIdentifiers(variables.stream().map(ScheduleIdentifier::new).collect(Collectors.toList()));
    return page;
  }

  protected HtmlTemplateFinder getRouteScheduleTemplate(Route route) {
    Long companyId = route.getBusLine().getVersion().getCompany().getId();
    Long busLineId = route.getBusLine().getId();

    CompanyScheduleTemplate companyScheduleTemplateObject = companyScheduleTemplateService.getCompanyTemplate(companyId);
    CompanyScheduleTemplateId companyScheduleTemplate = companyScheduleTemplateObject.getId();
//
//    BusLineScheduleTemplate busLineScheduleTemplate = busLineScheduleTemplateService.getBusLineTemplate(busLineId);
//
    String templateId = companyScheduleTemplate.getTemplate().getId();
//    if( busLineScheduleTemplate != null ) {
//      templateId = busLineScheduleTemplate.getId().getTemplate().getId();
//    }

    HtmlTemplateFinder htmlTemplateFinder = new HtmlTemplateFinder();
    htmlTemplateFinder.setHtmlFilePath("templates/schedule/" + templateId + ".html");
    htmlTemplateFinder.setCssFilePath("templates/schedule/css/" + templateId + ".css");
    htmlTemplateFinder.setWorkingDirectory(appConfig.STORAGE_ABSOLUTE_PATH);
    htmlTemplateFinder.setTemplateType(companyScheduleTemplate.getTemplate().getType());
    htmlTemplateFinder.process();
    return htmlTemplateFinder;
  }
}
