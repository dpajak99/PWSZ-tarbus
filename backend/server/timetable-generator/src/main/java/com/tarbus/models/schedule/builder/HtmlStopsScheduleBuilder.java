package com.tarbus.models.schedule.builder;

import com.google.common.collect.Lists;
import com.tarbus.AppConfig;
import com.tarbus.config.UserDirectories;
import com.tarbus.models.*;
import com.tarbus.services.*;
import com.tarbus.utilities.GenerateScheduleReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class HtmlStopsScheduleBuilder extends HtmlScheduleBuilder {
  private final Logger logger = LoggerFactory.getLogger(HtmlStopsScheduleBuilder.class);

  private final BusStopService busStopService;
  private final RouteService routeService;
  private final RouteConnectionService routeConnectionService;

  @Autowired
  public HtmlStopsScheduleBuilder(AppConfig appConfig, DepartureService departureService, BusStopService busStopService, RouteService routeService, RouteConnectionService routeConnectionService, BusLineScheduleTemplateService busLineScheduleTemplate, CompanyScheduleTemplateService companyScheduleTemplateService, CompanyScheduleTemplateService companyScheduleTemplateService1, UserDirectories userDirectories) {
    super(busLineScheduleTemplate, departureService, appConfig, companyScheduleTemplateService, userDirectories);

    this.busStopService = busStopService;
    this.routeService = routeService;
    this.routeConnectionService = routeConnectionService;
  }

  @Override
  public List<PdfDocument> generateScheduleTables(Company company, TemplateSettings templateSettings) {
    System.out.println("Generate by stops");
    generateScheduleReport.setStartDate(new Date());
    setMainCompany(company);
    setTemplateSettings(templateSettings);

    List<PdfDocument> documents = new ArrayList<>();

    List<BusStop> busStops = new ArrayList<>(getGeneratedBusStops(templateSettings.getRoutes()));
    busStops.sort(Comparator.comparing(BusStop::getName));
    System.out.println("Generate by stops");
    List<PdfPage> allPages = new ArrayList<>();
    for (BusStop busStop : busStops) {
      allPages.addAll(generateSchedulePagesForStop(busStop));
    }

    Set<ScheduleIdentifier> allIdentifiers = new HashSet<>();
    for (PdfPage page : allPages) {
      allIdentifiers.addAll(page.getIdentifiers());
    }

    for (ScheduleIdentifier identifier : allIdentifiers) {
      List<PdfPage> documentPages = allPages.stream().filter((e) -> e.getIdentifiers().contains(identifier)).collect(Collectors.toList());
      PdfDocument pdfDocument = new PdfDocument();
      GenerateScheduleReport finalReport = new GenerateScheduleReport(generateScheduleReport);
      finalReport.configureReport(identifier, documentPages);
      pdfDocument.setPages(documentPages);
      pdfDocument.setTitle(identifier.getTitle());
      pdfDocument.setFileName(identifier.getTitle() + ".pdf");
      pdfDocument.setWorkingDirectory(appConfig.STORAGE_ABSOLUTE_PATH + "/schedule/version/");
      pdfDocument.setGenerateScheduleReport(finalReport);
      documents.add(pdfDocument);
    }
    return documents;
  }

  private Set<BusStop> getGeneratedBusStops(List<Long> routesId) {
    Set<BusStop> busStops = new HashSet<>();
    for (Long routeId : routesId) {
      busStops.addAll(busStopService.findByRoute(routeId));
    }
    return busStops;
  }

  private List<PdfPage> generateSchedulePagesForStop(BusStop busStop) {
    List<Route> companyRoutes = routeService.getRoutesFromBusStopAndRoutesId(busStop.getId(), templateSettings.getRoutes());
   List<Route> nonCompanyRoutes = getNonCompanyRoutes(busStop);

    List<RouteHtmlVariables> companyRoutesHtmlVariables = getAllStopVariables(busStop, companyRoutes);
    List<RouteHtmlVariables> nonCompanyRoutesHtmlVariables = getAllStopVariables(busStop, nonCompanyRoutes);
    List<PdfPage> pages = new ArrayList<>();

    if (!nonCompanyRoutesHtmlVariables.isEmpty()) {
      List<RouteHtmlVariables> mergedVariables = Stream.concat(companyRoutesHtmlVariables.stream(), nonCompanyRoutesHtmlVariables.stream()).collect(Collectors.toList());

      // Split variables to pages
      List<List<RouteHtmlVariables>> routeStopDatas = Lists.partition(mergedVariables, templateSettings.getRoutesPerPage());

      // For each page generate html
      for (List<RouteHtmlVariables> routeStopData : routeStopDatas) {
        PdfPage page = generateMultiHtmlPageForStop(busStop, routeStopData);
        if (page != null) {
          pages.add(page);
        }
      }
    } else {
      List<List<RouteHtmlVariables>> routeStopDatas = Lists.partition(companyRoutesHtmlVariables, templateSettings.getRoutesPerPage());
      for (List<RouteHtmlVariables> routeStopData : routeStopDatas) {
        SinglePdfPage mainRoutesPage = preparePdfPageData(busStop, routeStopData.get(0).getVersion(), routeStopData);
        pages.add(mainRoutesPage);
      }
    }
    return pages;
  }

  private PdfPage generateMultiHtmlPageForStop(BusStop busStop, List<RouteHtmlVariables> pageVariables) {
    List<RouteHtmlVariables> companyRoutesHtmlVariables = new ArrayList<>();
    List<RouteHtmlVariables> nonCompanyRoutesHtmlVariables = new ArrayList<>();
    for (RouteHtmlVariables routeStopDataItem : pageVariables) {
      if (templateSettings.getRoutes().contains(routeStopDataItem.getRoute().getId())) {
        companyRoutesHtmlVariables.add(routeStopDataItem);
      } else {
        nonCompanyRoutesHtmlVariables.add(routeStopDataItem);
      }
    }
    if (!companyRoutesHtmlVariables.isEmpty() && !nonCompanyRoutesHtmlVariables.isEmpty()) {
      SinglePdfPage mainRoutesPage = preparePdfPageData(busStop, companyRoutesHtmlVariables.get(0).getVersion(), companyRoutesHtmlVariables);
      SinglePdfPage additionalRoutesPage = preparePdfPageData(busStop, nonCompanyRoutesHtmlVariables.get(0).getVersion(), nonCompanyRoutesHtmlVariables);
      MultiPdfPage finalPage = new MultiPdfPage();
      // TODO(dominik): Add sort option for lines
      finalPage.setPages(Lists.newArrayList(mainRoutesPage, additionalRoutesPage));
      finalPage.setIdentifiers(Stream.concat(mainRoutesPage.getIdentifiers().stream(), additionalRoutesPage.getIdentifiers().stream())
        .collect(Collectors.toList()));
      return finalPage;
    } else if (!companyRoutesHtmlVariables.isEmpty()) {
      return preparePdfPageData(busStop, companyRoutesHtmlVariables.get(0).getVersion(), companyRoutesHtmlVariables);
    } else if (!nonCompanyRoutesHtmlVariables.isEmpty()) {
      return preparePdfPageData(busStop, nonCompanyRoutesHtmlVariables.get(0).getVersion(), nonCompanyRoutesHtmlVariables);
    }
    return null;
  }

  private List<Route> getNonCompanyRoutes(BusStop busStop) {
    List<Route> nonCompanyRoutes = routeService.getRoutesFromBusStopAndRoutesId(busStop.getId(), templateSettings.getAdditionalRoutes());
    nonCompanyRoutes.sort(Comparator.comparing(Route::getId));
    return nonCompanyRoutes;
  }

  private List<RouteHtmlVariables> getAllStopVariables(BusStop busStop, List<Route> routes) {
    List<RouteHtmlVariables> variables = new ArrayList<>();
    routes = templateSettings.sortRoutes(routes);
    System.out.println(routes);
    for (Route route : routes) {
      List<RouteConnection> routeConnections = routeConnectionService.getByRoute(route.getId());
      RouteConnection currentConnection = findCurrentConnection(routeConnections, busStop.getId());
      if (currentConnection != null) {
        RouteHtmlVariables routeHtmlVariables = getRouteHtmlVariables(currentConnection, routeConnections, templateSettings);
        if (!routeHtmlVariables.getDepartures().isEmpty()) {
          variables.add(routeHtmlVariables);
        } else {
          System.err.println("Cannot found departures for " + busStop.getName() + " | " + busStop.getId() + " and busLine " + route.getBusLine().getName() + " | " + route.getId() );
        }
      } else {
        System.err.println("Cannot find route connection for route " + route.getId() + " and busStop " + busStop.getId());
      }
    }
    System.out.println("--------");
    return variables;
  }

  private RouteConnection findCurrentConnection(List<RouteConnection> connections, Long busStopId) {
    for (RouteConnection connection : connections) {
      if (Objects.equals(connection.getBusStop().getId(), busStopId)) {
        return connection;
      }
    }
    return null;
  }
}
