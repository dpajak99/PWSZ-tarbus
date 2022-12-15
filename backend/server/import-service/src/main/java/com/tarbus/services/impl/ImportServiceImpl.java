package com.tarbus.services.impl;

import com.tarbus.models.*;
import com.tarbus.repositories.soap.*;
import com.tarbus.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.AppConfig;
import com.tarbus.models.*;
import com.tarbus.repositories.soap.*;
import com.tarbus.services.*;
import com.tarbus.utilities.LoggerClock;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ImportServiceImpl implements ImportService {
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
  private CompanyService companyService;
  public ScheduleConfigService scheduleConfigService;

  @Autowired
  public void setBusLineService(@Autowired BusLineService busLineService) {
    this.busLineService = busLineService;
  }

  @Autowired
  public void setScheduleConfigService(@Autowired ScheduleConfigService scheduleConfigService) {
    this.scheduleConfigService = scheduleConfigService;
  }

  @Autowired
  public void setCompanyService(@Autowired CompanyService companyService) {
    this.companyService = companyService;
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

  @Autowired
  public SoapBusStopsRepository soapBusStopsRepository;

  @Autowired
  public SoapBusLinesRepository soapBusLinesRepository;

  @Autowired
  public TimeTableInfoRepository timeTableInfoRepository;

  @Autowired
  public SoapDaysRepository soapDaysRepository;

  @Autowired
  public SoapDeparturesRepository soapDeparturesRepository;

  @Autowired
  public SoapNotesRepository soapNotesRepository;

  @Autowired
  public SoapRouteTimetableRepository soapRouteTimetableRepository;
  @Autowired
  public SoapRouteAndVariantsRepository soapRouteAndVariantsRepository;

  @Autowired
  public SoapBusStopDeparturesRepository soapBusStopDeparturesRepository;

  LoggerClock loggerClock = new LoggerClock();

  @Override
  public String fetchMpkSchedule() {

    try {
      TimeTableInfo timeTableInfo = timeTableInfoRepository.getTimeTableInfo();
      Company company = companyService.getCompanyById((long) 3);
      versionService.deleteVersion((long) 3);
      ScheduleVersion version = new ScheduleVersion((long) 1, company, "auto_generated", timeTableInfo.getTimeTableDate(), timeTableInfo.getTimeTableDate(), "mpktarnow", "created", "active");
      List<BusLine> busLines = fetchBusLines(timeTableInfo, version);


      for (BusLine busLine : busLines) {
        fetchTimetable(busLine, timeTableInfo);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

//    scheduleConfigService.generateDatabaseInfo();

    return "OK";
  }

  private void fetchTimetable(BusLine busLine, TimeTableInfo timeTableInfo) throws SOAPException, IOException {
    List<Departure> finalDepartures = new ArrayList<>();
    List<BusStop> finalBusStops = new ArrayList<>();
    List<Route> finalRoutes = new ArrayList<>();
    List<RouteConnection> finalRouteConnections = new ArrayList<>();
    Set<Integer> usedBusStops = new HashSet<>();
    Set<Track> finalTracks = new HashSet<>();
    Set<Destination> finalDestinations = new HashSet<>();

//    if(!Objects.equals(busLine.getName(), "46")) {
//      System.out.println(busLine.getName());
//      return;
//    };

    try {
      System.out.println("Downloading departures for line: " + busLine);
      List<SoapRouteVariant> soapRouteVariants = soapRouteAndVariantsRepository.fetchAll(timeTableInfo.getTimeTableVersion(), busLine.getName());
      List<SoapDeparture> soapDepartures = soapRouteTimetableRepository.getRouteTimetable(timeTableInfo.getTimeTableVersion(), busLine.getName());

      if (!soapDepartures.isEmpty()) {
        busLine = busLineService.add(busLine);
      } else {
        return;
      }


      TmpRoute directionT;
      try {
        directionT = new TmpRoute("T", busLine, soapRouteVariants);
        directionT.setMainRoute(routeService.add(directionT.getMainRoute()));
      } catch (Exception e) {
        e.printStackTrace();
        directionT = null;
      }

      TmpRoute directionP;
      try {
        directionP = new TmpRoute("P", busLine, soapRouteVariants);
        directionP.setMainRoute(routeService.add(directionP.getMainRoute()));
      } catch (Exception e) {
        e.printStackTrace();
        directionP = null;
      }

      for (SoapDeparture departure : soapDepartures) {
        Calendar departureTime = parseDepartureTime(departure.getTime());

        TmpRoute matchedRoute = matchDirection(departure.getDirection(), directionT, directionP);
        Route route = matchedRoute.getMainRoute();
        if (!matchedRoute.lineConnectionsBusStop.contains(departure.getBusStopId())) {
          matchedRoute.lineConnections.add(new RouteConnection(null, (long) 0, (long) departure.getBusStopNo(), route, new BusStop((long) departure.getBusStopId())));
          matchedRoute.lineConnectionsBusStop.add(departure.getBusStopId());
        }
        Track track = new Track();
        track.setId(departure.getCourseId());
        track.setLp((long) finalTracks.size());
        track.setDayTypes(departure.getDayId());
        track.setDayString(departure.getDayId());
        track.setRoute(route);
        finalTracks.add(track);

        usedBusStops.add(departure.getBusStopId());
        Departure finalDeparture = new Departure();
        finalDeparture.setBusStop(new BusStop((long) departure.getBusStopId()));
        finalDeparture.setBusStopNo((long) departure.getBusStopNo());

        String departureMinutes = departureTime.get(Calendar.MINUTE) < 10 ? "0" + departureTime.get(Calendar.MINUTE) : String.valueOf(departureTime.get(Calendar.MINUTE));
        finalDeparture.setTime(departureTime.get(Calendar.HOUR_OF_DAY) + ":" + departureMinutes);
        finalDeparture.setTimeInMin((long) (departureTime.get(Calendar.HOUR_OF_DAY) * 60 + departureTime.get(Calendar.MINUTE)));
        finalDeparture.setIsLastDeparture(false);
        String symbol = matchedRoute.getMatchedSymbolIfExists(departure.getDirection());
        if (Objects.equals(symbol, "")) {
          symbol = "-";
        }
        finalDeparture.setSymbols(symbol);
        finalDeparture.setTrack(track);
        finalDepartures.add(finalDeparture);
      }
      if (directionT != null) {
        finalRoutes.add(directionT.getMainRoute());
        finalRouteConnections.addAll(directionT.lineConnections);
        finalDestinations.addAll(directionT.getDestinations());
      }

      if (directionP != null) {
        finalRoutes.add(directionP.getMainRoute());
        finalRouteConnections.addAll(directionP.lineConnections);
        finalDestinations.addAll(directionP.getDestinations());
      }

      System.out.println("Line download duration: " + loggerClock.getSecondsFromLastCheck() + "seconds");

    } catch (Exception e) {
      e.printStackTrace();
    }
    List<SoapBusStop> busStops = soapBusStopsRepository.fetchAll(timeTableInfo.getTimeTableVersion());
    for (SoapBusStop busStop : busStops) {
      if (usedBusStops.contains(busStop.getBusStopId())) {
        finalBusStops.add(new BusStop((long) busStop.getBusStopId(), busStop.getName(), "test", busStop.getLat(), busStop.getLng()));
      }
    }

    busStopService.addAll(finalBusStops);
    finalRoutes = routeService.addAll(finalRoutes);
    trackService.addAll(new ArrayList<>(finalTracks));
    finalDepartures = departureService.addAll(finalDepartures);
    routeConnectionService.addAll(finalRouteConnections);
    finalDestinations = new HashSet<>(destinationsService.addAll(new ArrayList<>(finalDestinations)));

    for (BusStop stop : finalBusStops) {
      List<SDayDetails> dayDetails = soapBusStopDeparturesRepository.fetchAll(timeTableInfo.getTimeTableVersion(), stop.getId());
      for (SDayDetails details : dayDetails) {
        for (SLineDetails lineDetails : details.getRoutes()) {
          for (SLineDeparture lineDeparture : lineDetails.getDepartures()) {
            for (Departure departure : finalDepartures) {
              departure.setExternalUuid(lineDeparture.getUid());
              if (Objects.equals(departure.getTrack().getId(), lineDeparture.getCourseId()) && departure.getTimeInMin() == lineDeparture.getTimeInMin()) {
                StringBuilder symbols = new StringBuilder();
                for (SDepartureSymbol symbol : lineDeparture.getSymbols()) {
                  String currentSymbol = symbols.toString();
                  if( currentSymbol.isEmpty() ) {
                    currentSymbol = symbol.getOzn();
                  }
                  finalDestinations.add(new Destination(
                    new DestinationId(departure.getTrack().getRoute(), symbol.getOzn()),
                    findSymbolName(new ArrayList<>(finalDestinations), departure),
                    symbol.getOzn() + " - " + symbol.getOznDesc())
                  );
                  finalDestinations.add(new Destination(
                    new DestinationId(departure.getTrack().getRoute(), currentSymbol),
                    findSymbolName(new ArrayList<>(finalDestinations), departure),
                    currentSymbol+ " - " + symbol.getOznDesc())
                  );
                  symbols.append(symbol.getOzn());
                }
                if (symbols.length() != 0) {
                  departure.setSymbols(symbols.toString());
                }
              }
            }
          }
        }
      }
    }
    departureService.addAll(finalDepartures);
    destinationsService.addAll(new ArrayList<>(finalDestinations));
  }

  private TmpRoute matchDirection(String direction, TmpRoute directionT, TmpRoute directionP) throws Exception {
    if (direction == null) {
      throw new Exception("Cannot find tmpRoute");
    }
    if (directionT != null && directionT.getMatchedSymbolIfExists(direction) != null) {
      return directionT;
    } else if (directionP != null && directionP.getMatchedSymbolIfExists(direction) != null) {
      return directionP;
    } else {
      Map<String, String> customRoutes = new HashMap<>();
      customRoutes.put("Mościce", "Kapro");
      try {

        TmpRoute matchedRoute = matchDirection(customRoutes.get(direction), directionT, directionP);
        matchedRoute.addDestination(direction);
        return matchedRoute;
      } catch (Exception e) {
        System.err.println("Cannot find: " + direction);
        throw new Exception(e.getMessage());
      }
    }
  }

  private String findSymbolName(List<Destination> finalDestinations, Departure departure) {
    for (Destination destination : finalDestinations) {
      if (Objects.equals(destination.getDestinationId().getSymbols(), departure.getSymbols()) && Objects.equals(destination.getDestinationId().getId().getId(), departure.getTrack().getRoute().getId())) {
//        System.out.println(departure.getSymbols() + " ||| " + destination.getBoardName());
        return destination.getBoardName();
      } else if(Objects.equals(destination.getDestinationId().getSymbols(), "W")) {
        return departure.getTrack().getRoute().getName();
      }
    }
    return "Error";
  }


  private List<BusLine> fetchBusLines(TimeTableInfo timeTableInfo, ScheduleVersion version) {
    try {
      List<SoapBusLine> busLines = soapBusLinesRepository.fetchAll(timeTableInfo.getTimeTableVersion());

      List<String> blockedLineNumbers = Arrays.asList(":", "*", ",", ".", "..", "'", "''");
      List<BusLine> finalBusLines = new ArrayList<>();
      for (SoapBusLine busLine : busLines) {
        if (!blockedLineNumbers.contains(busLine.getNumber().trim())) {
          finalBusLines.add(new BusLine(null, busLine.getNumber().trim(), "", version));
        }
      }
      return finalBusLines;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private Calendar parseDepartureTime(String departureTime) throws ParseException {
    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(departureTime);
    Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
    calendar.setTime(date);
    return calendar;
  }

  private List<SoapNote> filterPassengerNotes(List<SoapNote> notes) {
    Predicate<SoapNote> byType = note -> note.getType().equals("P");

    return notes.stream().filter(byType)
      .collect(Collectors.toList());
  }
}
