package com.tarbus.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarbus.AppConfig;
import com.tarbus.models.Company;
import com.tarbus.models.DatabaseObject;
import com.tarbus.models.ScheduleVersion;
import com.tarbus.services.*;
import com.tarbus.utilities.FileScanner;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.services.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {
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
  @SuppressWarnings("unchecked")
  public String exportByVersion(Long versionId) {
    FileScanner fileScanner = new FileScanner();
    try {
      List<String> templates = fileScanner.getResourceFiles("mobile-database-structure");
      ScheduleVersion version = versionService.getById(versionId);
      for (String templateName : templates) {
        prepareScheduleFile(templateName, version);
      }
      updatePublishedFile();
    } catch (Exception e) {
      return e.toString();
    }
    return appConfig.appURL + "/static/database/";
  }

  void updatePublishedFile() throws ParseException {
    List<ScheduleVersion> activeVersions = versionService.getAllActiveVersions();
    Map<String, Object> result = new HashMap<>();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    for( ScheduleVersion version : activeVersions ) {
      Date date = formatter.parse(version.getDate());
      result.put(version.getSubscribeCode(), date.getTime());
    }
    String fileName = appConfig.STORAGE_ABSOLUTE_PATH + "/config/database-info.json";
    try (FileWriter file = new FileWriter(fileName)) {
      file.write(JSONObject.toJSONString(result));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  void prepareScheduleFile(String templateName, ScheduleVersion version) throws IOException {
    FileScanner fileScanner = new FileScanner();
    String mainTemplateName = templateName.split("\\.")[0];
    InputStream databaseConfigData = fileScanner.getResourceAsStream("mobile-database-structure/" + templateName);
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> databaseConfig = mapper.readValue(databaseConfigData, Map.class);
    String databaseFileContent = parseDatabaseConfig(version.getId(), databaseConfig);
    String fileName = appConfig.STORAGE_ABSOLUTE_PATH + "/database/" + mainTemplateName + "-" + version.getSubscribeCode() + ".json";
    Files.createDirectories(Paths.get(appConfig.STORAGE_ABSOLUTE_PATH + "/database"));
    try (FileWriter file = new FileWriter(fileName)) {
      file.write(databaseFileContent);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  public String exportAll() {
    List<ScheduleVersion> activeVersions = versionService.getAllActiveVersions();
    for( ScheduleVersion version : activeVersions ) {
      exportByVersion(version.getId());
    }
    return "OK";
  }

  @SuppressWarnings("unchecked")
  private String parseDatabaseConfig(Long versionId, Map<String, Object> databaseConfig) {
    List<Object> result = new ArrayList<>();
    for (Map<String, Object> table : (List<Map<String, Object>>) databaseConfig.get("tables")) {
      result.add(parseTable(versionId, table));
    }
    return JSONArray.toJSONString(result);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> parseTable(Long versionId, Map<String, Object> table) {
    String mainTableName = (String) table.get("title");
    String exportedTableName = (String) table.get("exported_title");
    List<DatabaseObject> tableItems = (List<DatabaseObject>) getTableData(versionId, mainTableName);
    List<Object> exportedTableItems = new ArrayList<>();
    Map<String, String> columns = (Map<String, String>) table.get("columns");
    for (DatabaseObject tableItem : tableItems) {
      Map<String, Object> finalItem = new HashMap<>();
      for (Map.Entry<String, String> entry : columns.entrySet()) {
        Object property = tableItem.getPropertyByParam(entry.getKey());
        String result = "";
        if (property != null) {
          result = property.toString();
        }
        finalItem.put(entry.getValue(), result);
      }
      exportedTableItems.add(finalItem);
    }

    Map<String, Object> tableObject = new HashMap<>();
    tableObject.put("type", "table");
    tableObject.put("name", exportedTableName);
    tableObject.put("data", exportedTableItems);

    return tableObject;
  }

  private List<? extends DatabaseObject> getTableData(Long versionId, String tableName) {
    switch (tableName) {
      case "bus_lines":
        return busLineService.getAll(versionId);
      case "bus_stops":
        return busStopService.getAllByVersion(versionId);
      case "bus_stops_connections":
        return busStopConnectionService.getAll();
      case "schedule_calendar":
        return calendarService.getAll();
      case "departures":
        return departureService.getAll(versionId);
      case "destinations":
        return destinationsService.getAll(versionId);
      case "routes":
        return routeService.getAll(versionId);
      case "routes_connections":
        return routeConnectionService.getAll(versionId);
      case "tracks":
        return trackService.getAll(versionId);
      case "schedule_versions":
        List<ScheduleVersion> versionsResult = new ArrayList<>();
        versionsResult.add(versionService.getById(versionId));
        return versionsResult;
      case "company":
        List<Company> companyResult = new ArrayList<>();
        companyResult.add(versionService.getById(versionId).getCompany());
        return companyResult;
      default:
        return new ArrayList<>();
    }
  }
}
