package com.tarbus.services;

import com.tarbus.models.Route;

import java.util.List;
import java.util.Optional;

public interface RouteService {
  Optional<Route> findById(Long routeId);

  List<Route> getAll(Long versionId);

  List<Object> getByLine(Long lineId);

  List<Route> getFullByLine(Long lineId);

  List<Object> getByVersion(Long versionId);

  List<Route> addAll(List<Route> routes);

  List<Route> getRoutesFromBusStopAndRoutesId(Long busStopId, List<Long> routesId );

  List<Route> getByDetails(String details);

  Route add(Route route);
}
