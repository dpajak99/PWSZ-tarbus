package com.tarbus.repositories.jpa;


import com.tarbus.models.BusStop;
import com.tarbus.models.Departure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartureRepository extends JpaRepository<Departure, Long> {
  @Query("SELECT d FROM Departure d WHERE d.track.route.busLine.version.id = ?1")
  List<Departure> getAll(Long versionId);

  @Query("SELECT d FROM Departure d WHERE d.busStop.id = ?1 AND d.track.route.id = ?2 AND d.isLastDeparture = false ORDER BY d.timeInMin")
  List<Departure> getDepartures(Long busStopId, Long routeId);

  @Query("SELECT d FROM Departure d WHERE d.busStop.id = ?1 AND d.track.route.id = ?2 ORDER BY d.track.lp")
  List<Departure> getDeparturesOrderByTrack(Long busStopId, Long routeId);

  @Query("SELECT d.busStop FROM Departure d WHERE d.track.id = ?1 ORDER BY d.timeInMin")
  List<BusStop> getAllBusStopsOnTrack(String trackId);

  @Query("SELECT d FROM Departure AS d WHERE d.busStop.id = ?1 AND d.track.id = ?2")
  List<Departure> getNetDepartureByTrack(Long busStopId, String trackId);

  @Query("SELECT DISTINCT d.track.id FROM Departure d WHERE d.busStop.id = ?1")
  List<Long> getTracksContainsBusStop(Long busStopId);

  @Query("SELECT d.id FROM Departure d WHERE d.busStop.id = ?1 AND d.timeInMin > ?2 AND (d.track.dayTypes LIKE %?3% OR d.track.dayTypes LIKE %?4%)")
  List<Long> getNextDepartures(Long busStopId, Long time, String dayType1, String dayType2);

  @Query("SELECT d.id FROM Departure d WHERE d.busStop.id = ?1 AND d.timeInMin > ?2 AND (d.track.dayTypes LIKE %?3% OR d.track.dayTypes LIKE %?4%) AND d.track.route.id = ?5")
  List<Long> getNextDeparturesByLine(Long busStopId, Long time, String dayType1, String dayType2, Long line);

  @Query("SELECT d FROM Departure d WHERE d.track.id = ?1")
  List<Departure> getByTrack(String trackId);

  @Query("SELECT d FROM Departure d WHERE d.track.route.id = ?1 AND d.symbols = ?2")
  List<Departure> getByRouteAndSymbol(Long routeId, String symbol);
  @Query("SELECT d FROM Departure d WHERE d.track.dayTypes LIKE %?1% AND d.track.route.busLine.version.id = 10 AND d.timeInMin > 1160")
  List<Departure> getDeparturesByDay(String dayName);
}
