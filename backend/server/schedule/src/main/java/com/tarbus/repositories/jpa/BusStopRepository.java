package com.tarbus.repositories.jpa;


import com.tarbus.models.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {
  List<BusStop> findByNameContaining(String name);

  @Query("SELECT distinct d.busStop FROM Departure as d WHERE d.track.route.busLine.version.id = ?1")
  List<BusStop> getAllByVersion(Long versionId);

  @Query("SELECT bs FROM BusStop bs WHERE bs.searchName = ?1")
  List<BusStop> getBySearchName(String name);

  @Query("SELECT DISTINCT d.busStop FROM Departure d WHERE d.track.route.id = ?1")
  List<BusStop> findByRoute(Long routeId);
}
