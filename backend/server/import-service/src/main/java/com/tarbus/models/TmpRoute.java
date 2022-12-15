package com.tarbus.models;

import java.util.*;

public class TmpRoute {
  // T or P
  private String direction;
  private Route mainRoute;
  private Map<String, String> matchRouteNames;
  public Set<RouteConnection> lineConnections;
  public Set<Integer> lineConnectionsBusStop;
  public TmpRoute(String direction, BusLine busLine, List<SoapRouteVariant> routeVariants) {
    this.lineConnections = new HashSet<>();
    this.lineConnectionsBusStop = new HashSet<>();
    this.direction = direction;
    this.matchRouteNames = new HashMap<>();
    for (SoapRouteVariant route : routeVariants) {
      if(Objects.equals(route.getDirection(), direction)) {
        addDestination(route.getDestination());
      }
    }
    String routeName = new ArrayList<>(this.matchRouteNames.keySet()).get(0);
    this.mainRoute = new Route(null, routeName, routeName, routeName, busLine);
  }

  public void addDestination(String destination) {
    String symbolCount = getSymbolByCount();
    if( !matchRouteNames.containsKey(destination)) {
      this.matchRouteNames.put(destination, symbolCount);
    }
  }

  public String getMatchedSymbolIfExists(String routeName) {
    List<String> routes = new ArrayList<>(this.matchRouteNames.keySet());
    for( String route : routes ) {
      if(normalizeRouteName(route).equals(normalizeRouteName(routeName))) {
        return matchRouteNames.get(route);
      }
    }
    return null;
  }

  public List<Destination> getDestinations() {
    List<Destination> destinations = new ArrayList<>();
      for( String routeName : matchRouteNames.keySet()) {
      destinations.add(new Destination(
        new DestinationId(this.mainRoute, matchRouteNames.get(routeName)),
        routeName,
        matchRouteNames.get(routeName))
      );
    }
    return destinations;
  }

  private String getSymbolByCount() {
    StringBuilder builder = new StringBuilder("-");
    for( String routeName : matchRouteNames.keySet()) {
      System.out.println(routeName + " | " + matchRouteNames.get(routeName));
    }
    for (int i = 0; i < new ArrayList<>(matchRouteNames.keySet()).size(); i++) {
      System.out.println(i);
      builder.append("-");
    }
    return builder.toString();
  }

  public static String normalizeRouteName(String routeName) {
    return routeName.toLowerCase().replaceAll(" ", "").replaceAll("\\\\", "").replaceAll("/", "").replaceAll("-", "");
  }

  public String getDirection() {
    return direction;
  }

  public Route getMainRoute() {
    return mainRoute;
  }

  public Map<String, String> getMatchRouteNames() {
    return matchRouteNames;
  }

  public Set<RouteConnection> getLineConnections() {
    return lineConnections;
  }

  public Set<Integer> getLineConnectionsBusStop() {
    return lineConnectionsBusStop;
  }


  public void setMainRoute(Route mainRoute) {
    this.mainRoute = mainRoute;
  }

  @Override
  public String toString() {
    return "TmpRoute{" +
      "direction='" + direction + '\'' +
      ", mainRoute=" + mainRoute +
      ", matchRouteNames=" + matchRouteNames +
      ", lineConnections=" + lineConnections +
      ", lineConnectionsBusStop=" + lineConnectionsBusStop +
      '}';
  }
}
