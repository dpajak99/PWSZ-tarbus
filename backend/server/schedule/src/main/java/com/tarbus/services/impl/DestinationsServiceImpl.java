package com.tarbus.services.impl;

import com.tarbus.models.Destination;
import com.tarbus.repositories.jpa.DestinationsRepository;
import com.tarbus.services.DestinationsService;
import com.tarbus.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DestinationsServiceImpl implements DestinationsService {

  private DestinationsRepository destinationsRepository;

  private RouteService routeService;

  @Autowired
  public void setRouteService(RouteService routeService) {
    this.routeService = routeService;
  }

  @Autowired
  public DestinationsServiceImpl(DestinationsRepository destinationsRepository) {
    this.destinationsRepository = destinationsRepository;
  }

  @Override
  public List<Destination> getAll(Long versionId) {
    return destinationsRepository.getAll(versionId);
  }

  @Override
  public List<Destination> getAllByRouteLong(Long routeId) {
    return destinationsRepository.findByRouteLong(routeId);
  }

  @Override
  public List<Destination> getAllByRouteShort(Long routeId) {
    return destinationsRepository.findByRouteShort(routeId);
  }

  @Override
  public List<Destination> getAllByRouteAndSymbol(Long routeId, String symbol) {
    return destinationsRepository.findByRouteAndSymbol(routeId, symbol);
  }

  @Override
  public List<Destination> addAll(List<Destination> destinations) {
    return (List<Destination>) destinationsRepository.saveAll(destinations);
  }

  @Override
  public Object getByVersion(Long versionId) {
    List<Object> result = new ArrayList<>();
    List<Object> allRoutes = routeService.getByVersion(versionId);
    for( Object lineDetails : allRoutes ) {
      Map<String, Object> _lineDetails = (Map<String, Object>) lineDetails;
      List<Map<String, Object>> routes = (List<Map<String, Object>>) _lineDetails.get("routes");
      List<Object> routesListResult = new ArrayList<>();
      for(Map<String, Object> route : routes ) {
        Long id = (Long) route.get("routeId");
        String name = (String) route.get("routeName");
        List<Destination> destinationsFull = getAllByRouteLong(id);
        List<Object> destinations = new ArrayList<>();
        for(Destination destination : destinationsFull) {
          destinations.add(destination.toShort());
        }
        Map<String, Object> routeDestinations = new HashMap<>();
        routeDestinations.put("routeId", id);
        routeDestinations.put("routeName", name);
        routeDestinations.put("destinations", destinations);
        routesListResult.add(routeDestinations);
      }
      Map<String, Object> lineResult = new HashMap<>();
      lineResult.put("line", _lineDetails.get("line"));
      lineResult.put("routes", routesListResult);
      result.add(lineResult);
    }
    return result;
  }


  @Override
  public void deleteAll(List<Destination> destinations) {
    destinationsRepository.deleteAll(destinations);
  }

  @Override
  public void delete(Destination destination) {
    destinationsRepository.delete(destination);
  }

  @Override
  public List<String> getShortestDestinationFromBusStop(Long busStopId) {
    return destinationsRepository.getShortestDestinationFromBusStop(busStopId);
  }

  @Override
  public Destination add(Destination destination) {
    return destinationsRepository.save(destination);
  }
}
