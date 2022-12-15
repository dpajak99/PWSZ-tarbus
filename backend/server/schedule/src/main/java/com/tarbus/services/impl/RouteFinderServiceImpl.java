package com.tarbus.services.impl;

import com.tarbus.models.BusStop;
import com.tarbus.models.Departure;
import com.tarbus.models.routefinder.BusStopNode;
import com.tarbus.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteFinderServiceImpl implements RouteFinderService {

  @Autowired
  private DepartureService departureService;
  final 
  @Override
  public Object find() {
    Map<String, List<BusStopNode>> busStopNodes = new HashMap<>();
    Map<String, List<Departure>> departuresByDay = departureService.getDeparturesByDay("RO");
    int nodeNumber = 0;
    for( List<Departure> departures : departuresByDay.values()) {
      List<BusStopNode> trackBusStopNodes = new ArrayList<>();
      for( int i = 0; i < departures.size(); i++) {
        Departure departure = departures.get(i);
        Departure nextDeparture = null;
        if( i < departures.size() - 1) {
          nextDeparture = departures.get(i + 1);
        }
        double distance = 0;
        if( nextDeparture != null ) {
          BusStop firstBusStop = departure.getBusStop();
          BusStop lastBusStop = nextDeparture.getBusStop();
          distance = Math.sqrt(Math.pow(lastBusStop.getLat() - firstBusStop.getLat(), 2) + Math.pow(lastBusStop.getLng() - firstBusStop.getLng(), 2));
        }
        BusStopNode busStopNode = BusStopNode.builder()
          .nodeNumber(nodeNumber)
          .distance(distance)
          .firstBustStopId(departure.getBusStop().getId())
          .lastBustStopId(nextDeparture != null ? nextDeparture.getBusStop().getId() : null)
          .lineId(departure.getTrack().getRoute().getBusLine().getId())
          .time(departure.getTimeInMin(), nextDeparture != null ? nextDeparture.getTimeInMin() : departure.getTimeInMin())
          .build();
        trackBusStopNodes.add(busStopNode);
        nodeNumber += 1;
      }
      busStopNodes.put(departures.get(0).getTrack().getId(), trackBusStopNodes);
    }

    return busStopNodes;
  }
}
