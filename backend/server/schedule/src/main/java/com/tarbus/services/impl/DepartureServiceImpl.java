package com.tarbus.services.impl;

import com.tarbus.models.*;
import com.tarbus.repositories.jpa.DepartureRepository;
import com.tarbus.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.models.ScheduleCalendar;
import com.tarbus.models.*;
import com.tarbus.services.*;

import java.util.*;

@Service
public class DepartureServiceImpl implements DepartureService {

  private DepartureRepository departureRepository;
  private CalendarService calendarService;
  private DestinationsService destinationsService;
  private RouteConnectionService routeConnectionService;
  private TrackService trackService;

  @Autowired
  public DepartureServiceImpl(DepartureRepository departureRepository) {
    this.departureRepository = departureRepository;
  }

  @Autowired
  public void setDestinationsService(DestinationsService destinationsService) {
    this.destinationsService = destinationsService;
  }

  @Autowired
  public void setRouteConnectionService(RouteConnectionService routeConnectionService) {
    this.routeConnectionService = routeConnectionService;
  }

  @Autowired
  public void setCalendarService(CalendarService calendarService) {
    this.calendarService = calendarService;
  }

  @Autowired
  public void setTrackService(TrackService trackService) {
    this.trackService = trackService;
  }

  @Override
  public List<Departure> getAll(Long versionId) {
    return departureRepository.getAll(versionId);
  }

  @Override
  public List<Departure> getNextDepartures(Long versionId, Long busStopId, int count) {
    ScheduleCalendar todayCalendar = calendarService.getToday();
    Date dateNow = new Date();
    Long time = (long) dateNow.getHours()*60 + dateNow.getMinutes();
    List<Departure> passedDepartures = new ArrayList<>();
    String[] dayTypes = todayCalendar.getDayTypes().split(" ");
    if(dayTypes.length == 1) {
//      departureRepository.getNextDepartures(versionId, busStopId, dayTypes[0], time);
    } else if( dayTypes.length == 2) {
//      departureRepository.getNextDepartures(versionId, busStopId, dayTypes[0], dayTypes[1], time);
    }
    return passedDepartures;
  }

  @Override
  public List<Departure> getDeparturesByDate(String date, int timeInMin, Long busStopId, int count) {
    ScheduleCalendar todayCalendar = calendarService.getByDate(date);
    List<Long> passedDeparturesId = new ArrayList<>();
    String[] dayTypes = todayCalendar.getDayTypes().split(" ");
    if(dayTypes.length == 1) {
      passedDeparturesId = departureRepository.getNextDepartures(busStopId, (long) timeInMin, dayTypes[0], dayTypes[0]).subList(0,count);
    } else if( dayTypes.length == 2) {
      passedDeparturesId = departureRepository.getNextDepartures(busStopId, (long) timeInMin, dayTypes[0], dayTypes[1]).subList(0,count);
    }
    return departureRepository.findAllById(passedDeparturesId);
  }
  @Override
  public Departure getNetDepartureByLine(String date, int timeInMin, Long busStopId, Long line) {
    ScheduleCalendar todayCalendar = calendarService.getByDate(date);
    Long passedDeparturesId = null;
    String[] dayTypes = todayCalendar.getDayTypes().split(" ");
    if(dayTypes.length == 1) {
      List<Long> tmpResult = departureRepository.getNextDeparturesByLine(busStopId, (long) timeInMin, dayTypes[0], dayTypes[0], line);
      if( tmpResult.size() > 0 ) {
        passedDeparturesId = tmpResult.get(0);
      }
    } else if( dayTypes.length == 2) {
      List<Long> tmpResult =departureRepository.getNextDeparturesByLine(busStopId, (long) timeInMin, dayTypes[0], dayTypes[1], line);
      if( tmpResult.size() > 0 ) {
        passedDeparturesId = tmpResult.get(0);
      }
    }

    if( passedDeparturesId != null ) {
      return departureRepository.findById(passedDeparturesId).get();
    } else {
      return null;
    }
  }

  @Override
  public Departure getNetDepartureByTrack(Long busStopId, String trackId) {
    List<Departure> result = departureRepository.getNetDepartureByTrack(busStopId, trackId);
    if( result.isEmpty() ) {
      return null;
    }
    return result.get(0);
  }

  @Override
  public List<Long> getTracksContainsBusStop(Long busStopId) {
    return departureRepository.getTracksContainsBusStop(busStopId);
  }

  @Override
  public List<Departure> getByTrack(String trackId) {
    return departureRepository.getByTrack(trackId);
  }

  @Override
  public List<Departure> getByRouteAndSymbol(Long routeId, String symbol) {
    return departureRepository.getByRouteAndSymbol(routeId, symbol);
  }

  @Override
  public Map<String, Object> getBusStopTimetable(Long busStopId, Long routeId) {
    List<Departure> departures = departureRepository.getDepartures(busStopId, routeId);
    List<Destination> destinations = destinationsService.getAllByRouteShort(routeId);
    List<Object> destinationsList = getDestinationsAsJson(departures, destinations);
    Map<String, List<Object>> sortedDepartures = sortDeparturesByDay(departures);

    Map<String, Object> result = new HashMap<>();
    result.put("destinations", destinationsList);
    result.put("departures", sortedDepartures);
    return result;
  }

  @Override
  public Map<String, Object> getRouteDepartures(Long routeId) {
    List<RouteConnection> routeConnections = routeConnectionService.getByRoute(routeId);
    List<Track> tracks = trackService.getByRoute(routeId);
    List<Object> tracksMap = getTracksAsJson(tracks);

    List<Map<String, Object>> resultList = new ArrayList<>();
    Map<String, Object> result = new HashMap<>();
    for (RouteConnection routeConnection : routeConnections) {
      String _busStopName = routeConnection.getBusStop().getName();
      Long _busStopId = routeConnection.getBusStop().getId();
      Long _routeId = routeConnection.getRoute().getId();

      List<Departure> departures = departureRepository.getDeparturesOrderByTrack(_busStopId, _routeId);

      Map<String, Object> routeConnectionResult = new HashMap<>();
      routeConnectionResult.put("stopId", _busStopId);
      routeConnectionResult.put("stopName", _busStopName);
      routeConnectionResult.put("departures", getMatchingDepartures(tracks, departures));
      resultList.add(routeConnectionResult);
    }

    result.put("tracks", tracksMap);
    result.put("departures", resultList);
    return result;
  }

  @Override
  public List<BusStop> getAllBusStopsOnTrack(String trackId) {
    return departureRepository.getAllBusStopsOnTrack(trackId);
  }

  @Override
  public void update(List<Departure> departures) {
    departureRepository.saveAll(departures);
  }

  @Override
  public void deleteAll(List<Departure> departures) {
    departureRepository.deleteAll(departures);
  }

  @Override
  public void delete(Departure departure) {
    departureRepository.delete(departure);
  }

  @Override
  public Departure getById(Long id) {
    return departureRepository.findById(id).orElse(null);
  }

  @Override
  public Departure add(Departure departure) {
    return departureRepository.save(departure);
  }

  @Override
  public List<Departure> addAll(List<Departure> departures) {
    return departureRepository.saveAll(departures);
  }


  Map<String, List<Object>> sortDeparturesByDay(List<Departure> departures) {
    String[] days = {"RO", "WS", "SW"};
    Map<String, List<Object>> sortedDepartures = new HashMap<>();
    for (String dayType : days) {
      sortedDepartures.put(dayType, new ArrayList<>());
    }
    for (Departure departure : departures) {
      for (String dayType : days) {
        if (departure.getTrack().getDayTypes().contains(dayType) || (dayType.equals("RO") && departure.getTrack().getDayTypes().contains("SC"))) {
          sortedDepartures.get(dayType).add(departure.toTimetableJson());
        }
      }
    }
    return sortedDepartures;
  }

  List<Object> getMatchingDepartures(List<Track> tracks, List<Departure> departures ) {
    List<Object> result = new ArrayList<>();
    for( Track track : tracks ) {
      Departure departure = findDepartureByTrack(departures, track.getId());
      if( departure == null ) {
        result.add(Departure.getEmptyJson());
      } else {
        result.add(departure.toTimetableJson());
      }
    }
    return result;
  }

  Departure findDepartureByTrack( List<Departure> departures, String trackId ) {
    for( Departure departure : departures ) {
      if(departure.getTrack().getId().equals(trackId)) {
        return departure;
      }
    }
    return null;
  }

  List<Object> getDestinationsAsJson(List<Departure> departures, List<Destination> destinations) {
    List<Object> destinationsList = new ArrayList<>();
    List<String> destinationValues = new ArrayList<>();
    for(Departure departure : departures) {
      char[] splitedSymbol = departure.getSymbols().toCharArray();
      for( char a : splitedSymbol ) {
        if( !destinationValues.contains(Character.toString(a))) {
          destinationValues.add(Character.toString(a));
        }
      }
    }
    for (Destination destination : destinations) {
      char[] splitedSymbol = destination.getDestinationId().getSymbols().toCharArray();
      for( char a : splitedSymbol ) {
        if( destinationValues.contains(Character.toString(a))) {
          destinationsList.add(destination.toTimetableJson());
        }
      }
    }
    return destinationsList;
  }

  List<Object> getTracksAsJson(List<Track> tracks) {
    List<Object> trackList = new ArrayList<>();
    for( Track track : tracks ) {
      trackList.add(track.toJson());
    }
    return trackList;
  }
  
  @Override
  public Map<String, List<Departure>> getDeparturesByDay(String dayType) {
    Map<String, List<Departure>> departuresByDay = new HashMap<>();
    List<Departure> departures = departureRepository.getDeparturesByDay(dayType);
    
    for( Departure departure : departures ) {
      departuresByDay.computeIfAbsent(departure.getTrack().getId(), k -> new ArrayList<>());
      departuresByDay.get(departure.getTrack().getId()).add(departure);
    }
    departuresByDay.forEach((key, value) -> value.sort(Comparator.comparing(Departure::getTimeInMin)));
    return departuresByDay;
  }
}
