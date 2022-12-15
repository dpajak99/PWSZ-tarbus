package com.tarbus.services;

import com.tarbus.models.BusStopConnection;

import java.util.List;

public interface BusStopConnectionService {
  BusStopConnection getSingleConnection(Long from, Long to);
  BusStopConnection save(BusStopConnection connection);
  void delete(BusStopConnection connection);
  void deleteAll(List<BusStopConnection> connections);
  int fetchEmptyConnections();
  void updateConnections(List<BusStopConnection> busStopConnections);
  Object generateGraphFiles();
  List<BusStopConnection> getEmptyConnections();
  List<BusStopConnection> getAll();
  List<BusStopConnection> getAllTo(Long to);
  List<BusStopConnection> getAllFrom(Long from);
}
