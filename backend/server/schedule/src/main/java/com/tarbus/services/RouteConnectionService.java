package com.tarbus.services;

import com.tarbus.models.RouteConnection;

import java.util.List;

public interface RouteConnectionService {
  List<RouteConnection> getAll(Long versionId);

  List<RouteConnection> getByRoute(Long routeId);

  void updateRouteConnection(List<RouteConnection> routeConnections);

  void deleteRouteConnections(List<RouteConnection> routeConnections);

  void addAll(List<RouteConnection> routeConnections);
  RouteConnection add(RouteConnection route);
}
