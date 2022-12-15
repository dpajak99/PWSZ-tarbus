package com.tarbus.services.impl;

import com.tarbus.models.Route;
import com.tarbus.repositories.jpa.RouteRepository;
import com.tarbus.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RouteServiceImpl implements RouteService {

    private RouteRepository routeRepository;

    @Autowired
    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

  @Override
  public Optional<Route> findById(Long routeId) {
    return routeRepository.findById(routeId);
  }

  @Override
    public List<Route> getAll(Long versionId) {
        return routeRepository.getAll(versionId);
    }

    @Override
    public List<Object> getByLine(Long lineId) {
        List<Route> routes = routeRepository.findByRoute(lineId);
        List<Object> result = new ArrayList<>();
        for(Route route : routes ) {
            result.add(route.toJson());
        }
        return result;
    }

  @Override
  public List<Route> getFullByLine(Long lineId) {
    List<Route> routes = routeRepository.findByRoute(lineId);
    return routes;
  }

    @Override
    public List<Object> getByVersion(Long versionId) {
        List<Route> routes = routeRepository.findByVersion(versionId);
        Map<String, List<Object>> sortedRoutesData = new HashMap<>();
        for(Route route : routes ) {
            String busLineName = route.getBusLine().getName();
            if(!sortedRoutesData.containsKey(busLineName)) {
                sortedRoutesData.put(busLineName, new ArrayList<>());
            }
            sortedRoutesData.get(busLineName).add(route.toJson());
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, List<Object>> entry : sortedRoutesData.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("line", entry.getKey());
            item.put("routes",entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Route> addAll(List<Route> routes) {
        return StreamSupport.stream(routeRepository.saveAll(routes).spliterator(), false)
          .collect(Collectors.toList());
    }

  @Override
  public List<Route> getRoutesFromBusStopAndRoutesId(Long busStopId, List<Long> routesId) {
      List<Route> allRoutes = routeRepository.getRoutesFromBusStop(busStopId);
      List<Route> finalRoutes = new ArrayList<>();
      for(Route route : allRoutes) {
        if( routesId.contains(route.getId())) {
          finalRoutes.add(route);
        }
      }
    return finalRoutes;
  }

  @Override
  public List<Route> getByDetails(String details) {
    return routeRepository.getByDetails(details);
  }

  @Override
  public Route add(Route route) {
    return routeRepository.save(route);
  }
}
