package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "routes_connections", schema = "schedule")
public class RouteConnection implements DatabaseObject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "is_optional")
  private Long isOptional;
  @Column(name = "lp")
  private Long lp;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "route_id", referencedColumnName = "id")
  private Route route;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "bus_stop_id", referencedColumnName = "id")
  private BusStop busStop;

  @Column(name = "description")
  private String description;


  public RouteConnection(Long id, Long isOptional, Long lp, Route route, BusStop busStop) {
    this.id = id;
    this.isOptional = isOptional;
    this.lp = lp;
    this.route = route;
    this.busStop = busStop;
  }

  public Map<String, Object> toJson() {
    Map<String, Object> map = new HashMap<>();

    map.put("id", id);
    map.put("lp", lp);
    map.put("routeId", route.getId());
    map.put("isOptional", isOptional);
    map.put("busStopId", busStop.getId());
    map.put("busStopName", busStop.getName());
    map.put("busStopLat", busStop.getLat());
    map.put("busStopLng", busStop.getLng());
    return map;
  }

  public static List<Object> toJson(List<RouteConnection> list) {
    List<Object> result = new ArrayList<>();
    for (RouteConnection routeConnection : list) {
      result.add(routeConnection.toJson());
    }
    return result;
  }

  @Override
  public String toString() {
    return "RouteConnection{" +
      "id=" + id +
      ", isOptional=" + isOptional +
      ", lp=" + lp +
      ", route=" + route +
      ", busStop=" + busStop +
      '}';
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "is_optional":
        return isOptional;
      case "lp":
        return lp;
      case "route_id":
        return route.getId();
      case "bus_stop_id":
        return busStop.getId();
    }
    return null;
  }
}
