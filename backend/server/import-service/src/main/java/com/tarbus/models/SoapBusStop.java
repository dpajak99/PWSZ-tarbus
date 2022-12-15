package com.tarbus.models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class SoapBusStop {
  private final int busStopId;
  private final double lat;
  private final double lng;
  private final String name;
  private final int number;


  public SoapBusStop(Node node) {
    Map<String, String> result = new HashMap<>();
    NodeList nodeList = node.getChildNodes();
    for( int i = 0; i < nodeList.getLength(); i++ ) {
      result.put(nodeList.item(i).getLocalName(), nodeList.item(i).getTextContent());
    }
    this.busStopId = parseInt(result.get("BusStopId"));
    this.lat = parseDouble(result.get("Lat"));
    this.lng = parseDouble(result.get("Lng"));
    this.name = result.get("Name");
    this.number = parseInt(result.get("Number"));
  }

  public int getBusStopId() {
    return busStopId;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public String getName() {
    return name;
  }

  public int getNumber() {
    return number;
  }



  @Override
  public String toString() {
    return "SoapBusStop{" +
      "busStopId=" + busStopId +
      ", lat=" + lat +
      ", lng=" + lng +
      ", name='" + name + '\'' +
      ", number=" + number +
      '}';
  }
}
