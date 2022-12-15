
package com.tarbus.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarbus.AppConfig;
import com.tarbus.models.ScheduleVersion;
import com.tarbus.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.services.*;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleConfigServiceImpl implements ScheduleConfigService {
  @Autowired
  AppConfig appConfig;

  private BusLineService busLineService;
  private BusStopService busStopService;
  private DepartureService departureService;
  private BusStopConnectionService busStopConnectionService;
  private DestinationsService destinationsService;
  private RouteConnectionService routeConnectionService;
  private RouteService routeService;
  private TrackService trackService;
  private VersionService versionService;
  private CalendarService calendarService;

  @Autowired
  public void setBusLineService(@Autowired BusLineService busLineService) {
    this.busLineService = busLineService;
  }

  @Autowired
  public void setBusStopConnectionService(BusStopConnectionService busStopConnectionService) {
    this.busStopConnectionService = busStopConnectionService;
  }

  @Autowired
  public void setBusStopService(BusStopService busStopService) {
    this.busStopService = busStopService;
  }

  @Autowired
  public void setDepartureService(DepartureService departureService) {
    this.departureService = departureService;
  }


  @Autowired
  public void setDestinationsService(DestinationsService destinationsService) {
    this.destinationsService = destinationsService;
  }

  @Autowired
  public void setRouteService(RouteConnectionService routeConnectionService) {
    this.routeConnectionService = routeConnectionService;
  }

  @Autowired
  public void setRouteService(RouteService routeService) {
    this.routeService = routeService;
  }

  @Autowired
  public void setTrackService(TrackService trackService) {
    this.trackService = trackService;
  }

  @Autowired
  public void setVersionService(VersionService versionService) {
    this.versionService = versionService;
  }

  @Autowired
  public void setCalendarService(CalendarService calendarService) {
    this.calendarService = calendarService;
  }


  @Override
  public Map<String, Object> generateDatabaseInfo() throws RuntimeException {
    List<ScheduleVersion> activeVersions = versionService.getAllActiveVersions();
    Map<String, Object> result = new HashMap<>();
    for (ScheduleVersion version : activeVersions) {
      DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
      try {
        Date versionDate = format.parse(version.getDate());
        result.put(version.getSubscribeCode(), versionDate.getTime());
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    }
    String fileName = appConfig.STORAGE_ABSOLUTE_PATH + "/config/database-info.json";
    try {
      Files.createDirectories(Paths.get(appConfig.STORAGE_ABSOLUTE_PATH + "/config"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    try (FileWriter file = new FileWriter(fileName)) {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(result);
      file.write(json);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }
}
