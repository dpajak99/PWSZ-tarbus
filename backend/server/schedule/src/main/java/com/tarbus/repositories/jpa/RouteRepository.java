package com.tarbus.repositories.jpa;

import com.tarbus.models.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route, Long> {

    @Query("SELECT r FROM Route as r WHERE r.busLine.version.id = ?1")
    List<Route> getAll(Long versionId);

    @Query("SELECT r FROM Route as r WHERE r.busLine.id = ?1")
    List<Route> findByRoute(Long routeId);

    @Query("SELECT r FROM Route as r WHERE r.busLine.version.id = ?1")
    List<Route> findByVersion(Long versionId);

    @Query("SELECT DISTINCT d.track.route FROM Departure d WHERE d.busStop.id = ?1")
    List<Route> getRoutesFromBusStop(Long busStopId);

    @Query("SELECT r FROM Route r WHERE r.details = ?1")
    List<Route> getByDetails(String details);
}
