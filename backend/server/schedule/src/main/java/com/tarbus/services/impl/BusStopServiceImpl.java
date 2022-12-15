package com.tarbus.services.impl;

import com.tarbus.models.BusStop;
import com.tarbus.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.repositories.jpa.BusStopRepository;
import com.tarbus.services.BusStopService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusStopServiceImpl implements BusStopService {

  private BusStopRepository busStopRepository;
  private RouteService routeService;

  @Autowired
  public void setRouteService(RouteService routeService) {
    this.routeService = routeService;
  }

  @Autowired
  public BusStopServiceImpl(BusStopRepository busStopRepository) {
    this.busStopRepository = busStopRepository;
  }


  @Override
  public List<BusStop> getAll(String name) {
    List<BusStop> busStops = new ArrayList<>();
    if (name == null)
      busStops.addAll(busStopRepository.findAll());
    else
      busStops.addAll(busStopRepository.findByNameContaining(name));

    return busStops;
  }

  @Override
  public List<BusStop> getAllByVersion(Long versionId) {
    return busStopRepository.getAllByVersion(versionId);
  }

  @Override
  public List<BusStop> findAllById(List<Long> idList) {
    return busStopRepository.findAllById(idList);
  }

  @Override
  public List<BusStop> getBySearchName(String name) {
    return busStopRepository.getBySearchName(name);
  }

  @Override
  public BusStop getById(Long id) {
    return busStopRepository.findById(id).orElse(null);
  }

  @Override
  public BusStop add(BusStop busStop) {
    return busStopRepository.save(busStop);
  }

  @Override
  public List<BusStop> addAll(List<BusStop> busStops) {
    return busStopRepository.saveAll(busStops);
  }

  @Override
  public List<BusStop> findByRoute(Long routeId) {
    return  busStopRepository.findByRoute(routeId);
  }

  @Override
  public void saveBusStopList(List<BusStop> busStops) {
    busStopRepository.saveAll(busStops);
  }

  @Override
  public void updateBusStopList(List<BusStop> busStops) {
    busStopRepository.saveAll(busStops);
  }

  @Override
  public void deleteAll(List<BusStop> busStops) {
    busStopRepository.deleteAll(busStops);
  }

  @Override
  public void delete(BusStop busStop) {
    busStopRepository.delete(busStop);
  }
}
