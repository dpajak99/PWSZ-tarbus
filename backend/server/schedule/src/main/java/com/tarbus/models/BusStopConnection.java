package com.tarbus.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "bus_stops_connections", schema = "schedule")
public class BusStopConnection implements DatabaseObject {
  @EmbeddedId
  private BusStopConnectionId busStopConnectionId;

  @Column(name = "distance")
  private Float distance;
  @Column(name = "track_shape")
  private String coordsList;

  public Map<String, Object> toShort() {
    Map<String, Object> result = new HashMap<>();
    result.put("fromId", busStopConnectionId.getFromBusStopId().getId());
    result.put("fromName", busStopConnectionId.getFromBusStopId().getName());
    result.put("toId", busStopConnectionId.getToBusStopId().getId());
    result.put("toName", busStopConnectionId.getToBusStopId().getName());
    return result;
  }

  public static List<Object> toShort(List<BusStopConnection> list) {
    List<Object> result = new ArrayList<>();
    for (BusStopConnection connection : list) {
      result.add(connection.toShort());
    }
    return result;
  }

  double getDistanceBetweenStops() {
    BusStop firstBusStop = busStopConnectionId.getFromBusStopId();
    BusStop lastBusStop = busStopConnectionId.getToBusStopId();
    
    if( !firstBusStop.hasCoords() || !lastBusStop.hasCoords()) {
      return -1;
    }
    
    // √[(x₂ - x₁)² + (y₂ - y₁)²]
    return Math.sqrt(Math.pow(lastBusStop.getLat() - firstBusStop.getLat(), 2) + Math.pow(lastBusStop.getLng() - firstBusStop.getLng(), 2));
  }

  public BusStopConnection() {
  }

  public BusStopConnectionId getBusStopConnectionId() {
    return busStopConnectionId;
  }

  public void setBusStopConnectionId(BusStopConnectionId busStopConnectionId) {
    this.busStopConnectionId = busStopConnectionId;
  }

  public Float getDistance() {
    return distance;
  }

  public void setDistance(Float distance) {
    this.distance = distance;
  }

  public String getCoordsList() {
    return coordsList;
  }

  public void setCoordsList(String coordsList) {
    this.coordsList = coordsList;
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "distance":
        return distance;
      case "track_shape":
        return coordsList;
      case "from_bus_stop":
        return busStopConnectionId.getFromBusStopId().getId();
      case "to_bus_stop":
        return busStopConnectionId.getToBusStopId().getId();
    }
    return null;
  }
}
