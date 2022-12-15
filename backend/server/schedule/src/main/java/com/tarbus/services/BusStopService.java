package com.tarbus.services;

import com.tarbus.models.BusStop;

import java.util.List;

public interface BusStopService {

    List<BusStop> getAll(String name);
    List<BusStop> getAllByVersion(Long versionId);
    List<BusStop> findAllById(List<Long> idList);
    List<BusStop> getBySearchName(String name);

    BusStop getById(Long id);
    BusStop add(BusStop busStop);
    List<BusStop> addAll(List<BusStop> busStops);
    List<BusStop> findByRoute(Long routeId);

    void saveBusStopList(List<BusStop> busStops);
    void updateBusStopList(List<BusStop> busStops);
    void deleteAll(List<BusStop> busStops);
    void delete(BusStop busStop);
}
