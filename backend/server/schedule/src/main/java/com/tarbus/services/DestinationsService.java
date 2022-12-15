package com.tarbus.services;

import com.tarbus.models.Destination;

import java.util.List;

public interface DestinationsService {

  List<Destination> getAll(Long versionId);

  List<Destination> getAllByRouteLong(Long routeId);
  List<Destination> getAllByRouteShort(Long routeId);

  List<Destination> getAllByRouteAndSymbol(Long routeId, String symbol);

  List<Destination> addAll(List<Destination> destinations);

  Object getByVersion(Long versionId);

  void deleteAll(List<Destination> destinations);

  void delete(Destination destination);

  List<String> getShortestDestinationFromBusStop(Long busStopId);

  Destination add(Destination destination);
}
