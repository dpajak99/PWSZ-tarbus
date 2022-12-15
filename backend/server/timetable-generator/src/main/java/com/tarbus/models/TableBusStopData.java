package com.tarbus.models;

import com.tarbus.AppConfig;
import com.tarbus.models.templates.KM001SKATRAINBUS;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class TableBusStopData {
  private TemplateSettings templateSettings;
  private AppConfig appConfig;

  private BusStop busStop;
  private ScheduleVersion version;
  private List<RouteHtmlVariables> routes;
  private List<String> destinations;

  public TableBusStopData(TemplateSettings templateSettings, AppConfig appConfig) {
    this.templateSettings = templateSettings;
    this.appConfig = appConfig;
  }

  public void configureData(BusStop busStop, ScheduleVersion version, List<RouteHtmlVariables> routes) {
    this.busStop = busStop;
    this.version = version;
    this.routes = routes;

    Set<String> _destinations = new HashSet<>();
    for(RouteHtmlVariables routeData : routes) {
      _destinations.addAll(routeData.getDestinations());
    }
    this.destinations = new ArrayList<>(_destinations);
    Collections.sort(this.destinations);
  }

  public boolean hasMultipleDates() {
    String dateStart = routes.get(0).getRoute().getDateStart();
    String dateEnd = routes.get(0).getRoute().getDateEnd();
    for( RouteHtmlVariables routeHtmlVariables : routes ) {
      boolean startDateEqual = routeHtmlVariables.getRoute().getDateStart().equals(dateStart);
      boolean endDateEqual = routeHtmlVariables.getRoute().getDateEnd().equals(dateEnd);
      if( !startDateEqual || !endDateEqual ) {
        return true;
      }
    }
    return false;
  }

  public Map<String, Object> getScheduleVariables() {
    Map<String, Object> scheduleVariables = new HashMap<>();
    scheduleVariables.put("template", new KM001SKATRAINBUS(this));
    scheduleVariables.put("logoPath", appConfig.appURL + "/api/drive/files/" + version.getCompany().getAvatar());
    scheduleVariables.put("driveUrl", appConfig.appURL + "/static/company/" + version.getCompany().getId() + "/");
    scheduleVariables.put("busStop", busStop);
    scheduleVariables.put("this", this);
    scheduleVariables.put("routes", routes);
    scheduleVariables.put("version", version);
    scheduleVariables.put("destinations", destinations);
    scheduleVariables.put("company", version.getCompany());
    scheduleVariables.put("isOptional", routes.get(0).getRouteConnection().getIsOptional());
    return scheduleVariables;
  }
}
