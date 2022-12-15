package com.tarbus.models.schedule.builder;

import com.tarbus.AppConfig;
import com.tarbus.config.UserDirectories;
import com.tarbus.models.*;
import com.tarbus.services.*;
import com.tarbus.utilities.GenerateScheduleReport;
import com.tarbus.utilities.HtmlTemplateFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HtmlRoutesScheduleBuilder extends HtmlScheduleBuilder{
  private final Logger logger = LoggerFactory.getLogger(HtmlRoutesScheduleBuilder.class);
  private final RouteService routeService;
  private final RouteConnectionService routeConnectionService;

  @Autowired
  public HtmlRoutesScheduleBuilder(DepartureService departureService, AppConfig appConfig, BusLineScheduleTemplateService busLineScheduleTemplateService, CompanyScheduleTemplateService companyScheduleTemplateService, UserDirectories userDirectories, RouteService routeService, RouteConnectionService routeConnectionService) {
    super(busLineScheduleTemplateService, departureService, appConfig, companyScheduleTemplateService, userDirectories);
    this.routeService = routeService;
    this.routeConnectionService = routeConnectionService;
  }

  @Override
  public List<PdfDocument> generateScheduleTables(Company company, TemplateSettings templateSettings) {
    setMainCompany(company);
    setTemplateSettings(templateSettings);
    generateScheduleReport.setStartDate(new Date());
    List<PdfDocument> documents = new ArrayList<>();
    for (Long routeId : templateSettings.getRoutes()) {
      System.out.println("Generating schedule for route " + routeId);
      Optional<Route> routeOptional = routeService.findById(routeId);
      if (routeOptional.isPresent()) {
        Route route = routeOptional.get();
        String busLineName = route.getBusLine().getName();
        String routeName = route.getName();
        System.out.println("Generating schedule for route " + busLineName + " " + routeName);

        List<PdfPage> pages = new ArrayList<>();
        List<RouteConnection> routeConnections = routeConnectionService.getByRoute(route.getId());
        System.out.println("Generating schedule for route " + busLineName + " " + routeName + " " + routeConnections.size());
        for (RouteConnection connection : routeConnections) {
          Company routeCompany = connection.getRoute().getBusLine().getVersion().getCompany();
          HtmlTemplateFinder htmlTemplateFinder = getRouteScheduleTemplate(connection.getRoute());
          RouteHtmlVariables routeHtmlVariables = getRouteHtmlVariables(connection, routeConnections, templateSettings);

          SinglePdfPage page = new SinglePdfPage(htmlTemplateFinder, routeHtmlVariables.getScheduleVariables());
          page.setWorkingDirectory(userDirectories.getCompanyDrivePath(routeCompany.getId()) + "/schedule/stop/" + connection.getBusStop().getId() + "/");
          page.setIdentifiers(new ArrayList<>(List.of((new ScheduleIdentifier(routeHtmlVariables)))));
          page.setFileName(connection.getRoute().getId().toString());
          pages.add(page);
        }

        Set<ScheduleIdentifier> allIdentifiers = new HashSet<>();
        for (PdfPage page : pages) {
          allIdentifiers.addAll(page.getIdentifiers());
        }

        System.out.println("All pages: " + pages.size());
        for (ScheduleIdentifier identifier : allIdentifiers) {
          List<PdfPage> documentPages = pages.stream().filter((e) -> e.getIdentifiers().contains(identifier)).collect(Collectors.toList());
          PdfDocument pdfDocument = new PdfDocument();
          GenerateScheduleReport finalReport = new GenerateScheduleReport(generateScheduleReport);
          finalReport.configureReport(identifier, documentPages);
          pdfDocument.setPages(documentPages);
          pdfDocument.setTitle(identifier.getTitle());
          pdfDocument.setFileName(identifier.getTitle() + ".pdf");
          pdfDocument.setWorkingDirectory(appConfig.STORAGE_ABSOLUTE_PATH + "/schedule/line/");
          pdfDocument.setGenerateScheduleReport(finalReport);
          documents.add(pdfDocument);
        }
      } else {
        logger.error("Cannot found specified route " + routeId);
      }
    }
    System.out.println("All documents: " + documents.size());
    return documents;
  }
}
