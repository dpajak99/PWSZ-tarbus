package com.tarbus.services.impl;

import com.tarbus.models.RouteConnection;
import com.tarbus.repositories.jpa.RouteConnectionRepository;
import com.tarbus.services.RouteConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteConnectionServiceImpl implements RouteConnectionService {

    private RouteConnectionRepository routeConnectionRepository;

    @Autowired
    public RouteConnectionServiceImpl(RouteConnectionRepository routeConnectionRepository) {
        this.routeConnectionRepository = routeConnectionRepository;
    }

    @Override
    public List<RouteConnection> getAll(Long versionId) {
        return routeConnectionRepository.getAll(versionId);
    }

    @Override
    public List<RouteConnection> getByRoute(Long routeId) {
        return routeConnectionRepository.findByRoute(routeId);
    }

    @Override
    public void updateRouteConnection(List<RouteConnection> routeConnections) {
        routeConnectionRepository.saveAll(routeConnections);
    }

    @Override
    public void deleteRouteConnections(List<RouteConnection> routeConnections) {
        routeConnectionRepository.deleteAll(routeConnections);
    }

    @Override
    public void addAll(List<RouteConnection> routeConnections) {
        routeConnectionRepository.saveAll(routeConnections);
    }

  @Override
  public RouteConnection add(RouteConnection route) {
    return routeConnectionRepository.save(route);
  }
}
