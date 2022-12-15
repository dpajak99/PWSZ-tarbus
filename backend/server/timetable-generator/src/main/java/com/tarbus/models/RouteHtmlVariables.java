package com.tarbus.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarbus.AppConfig;
import com.tarbus.models.templates.KM001SKABUS;
import com.tarbus.models.templates.KM001SKATRAINBUS;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class RouteHtmlVariables {
  private static String defaultQrCodeGeneratorUrl = "https://chart.googleapis.com/chart?cht=qr&chs=500x500&chl=https://app.tarbus.pl/store?directFrom=$directFrom%26busStopId=$busStopId%26busLineId=$busLineId";

  private AppConfig appConfig;
  private TemplateSettings templateSettings;


  private RouteConnection routeConnection;
  private List<RouteConnection> allRouteConnections;
  private List<String> cities;
  private DepartureHolder departures;
  private ArrayList<String> destinations;

  private String qrCode;
  private String logoPath;
  private String driveUrl;
  private List<BusStop> busStops;
  private BusStop busStop;
  private BusLine busLine;
  private Route route;
  private ScheduleVersion version;
  private Company company;
  private Integer[] availableHours;

  public RouteHtmlVariables(AppConfig appConfig, TemplateSettings templateSettings) {
    this.appConfig = appConfig;
    this.templateSettings = templateSettings;
  }

  public void configureData(RouteConnection routeConnection, List<RouteConnection> allRouteConnections, Map<String, Object> scheduleData) {
    this.version = routeConnection.getRoute().getBusLine().getVersion();
    System.out.println("############ ROUTE CONNECTION: " + routeConnection.getLp());
    this.routeConnection = routeConnection;
    this.allRouteConnections = allRouteConnections;
    Map<String, Object> departuresData = (Map<String, Object>) scheduleData.get("departures");
    Set<String> destinationsSet = new HashSet<>((List<String>) scheduleData.get("destinations"));
    this.destinations = new ArrayList<>(destinationsSet);
    final ObjectMapper mapper = new ObjectMapper();
    this.departures = mapper.convertValue(departuresData, DepartureHolder.class);
    this.company = version.getCompany();
    this.qrCode = _getQrCode(routeConnection);
    this.logoPath = appConfig.appURL + "/api/drive/files/" + version.getCompany().getAvatar();
    this.driveUrl = appConfig.STORAGE_ABSOLUTE_PATH + "/company/" + this.company.getId() + "/";
    this.busStop = routeConnection.getBusStop();
    this.busLine = routeConnection.getRoute().getBusLine();
    this.route = routeConnection.getRoute();
    this.version = routeConnection.getRoute().getBusLine().getVersion();
    this.availableHours = new Integer[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    this.busStops = allRouteConnections.stream().map(RouteConnection::getBusStop).distinct().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    this.cities = busStops.stream().map(BusStop::getCity).distinct().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    this.departures.sort();
  }

  public List<String> getRoDestinations() {
    List<SingleDeparture> departures = this.departures.getRo();
    Set<String> newDestinations = new HashSet<>();
    for (SingleDeparture departure : departures) {
      for(String destination : destinations) {
        String pattern = destination.split("")[0];
        System.out.println("PATTERN: [" + departure.getSymbolArray() +"] = " + pattern);
        if(departure.getSymbolArray().contains(pattern)) {
          System.out.println("found: " + destination);
          newDestinations.add(destination);
        }
      }
    }
    return new ArrayList<>(newDestinations);
  }

  public Map<String, Object> getScheduleVariables() {
    Map<String, Object> scheduleVariables = new HashMap<>();


    scheduleVariables.put("template", new KM001SKABUS(this));
    scheduleVariables.put("this", this);
    scheduleVariables.put("qrCode", qrCode);
    scheduleVariables.put("logoPath", logoPath);
    scheduleVariables.put("driveUrl", driveUrl);
    scheduleVariables.put("routeConnection", routeConnection);
    scheduleVariables.put("allRouteConnections", allRouteConnections);
    scheduleVariables.put("departures", departures);
    scheduleVariables.put("destinations", destinations);
    scheduleVariables.put("busStops", busStops);
    scheduleVariables.put("busStop", busStop);
    scheduleVariables.put("busLine", busLine);
    scheduleVariables.put("cities", cities);
    scheduleVariables.put("route", route);
    scheduleVariables.put("version", version);
    scheduleVariables.put("company", company);
    scheduleVariables.put("availableHours", availableHours);
    return scheduleVariables;
  }

  private String _getQrCode(RouteConnection connection) {
    String qrGeneratorUrl = templateSettings.getCustomQrImageLink();
    if (qrGeneratorUrl == null) {
      qrGeneratorUrl = defaultQrCodeGeneratorUrl;
    }
    qrGeneratorUrl.replaceFirst("$busStopId", connection.getBusStop().getId().toString());
    qrGeneratorUrl.replaceFirst("$busLineId", connection.getRoute().getBusLine().getId().toString());
    qrGeneratorUrl.replaceFirst("$directFrom", "schedule");
    return qrGeneratorUrl;
  }
}
