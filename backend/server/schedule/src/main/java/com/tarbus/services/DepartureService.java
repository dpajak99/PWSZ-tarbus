package com.tarbus.services;

import com.tarbus.models.BusStop;
import com.tarbus.models.Departure;

import java.util.List;
import java.util.Map;

public interface DepartureService {

    List<Departure> getAll(Long versionId);
    List<Departure> getNextDepartures(Long versionId, Long busStopId, int count);
    List<Departure> getDeparturesByDate(String date, int timeInMin, Long busStopId, int count);
    Departure getNetDepartureByLine(String date, int timeInMin, Long busStopId, Long line);
    Departure getNetDepartureByTrack(Long busStopId, String trackId);
    List<Long> getTracksContainsBusStop(Long busStopId);

    List<Departure> getByTrack(String trackId);
    List<Departure> getByRouteAndSymbol(Long routeId, String symbol);
    Map<String, Object> getBusStopTimetable(Long busStopId, Long routeId);

    Departure getById(Long id);
    Departure add(Departure departure);
    List<Departure> addAll(List<Departure> departure);

    Map<String, Object> getRouteDepartures(Long routeId);

    List<BusStop> getAllBusStopsOnTrack(String trackId);

    void update( List<Departure> departures );
    void deleteAll(List<Departure> departures );
    void delete(Departure departure );

    Map<String, List<Departure>> getDeparturesByDay(String dayName);
}
