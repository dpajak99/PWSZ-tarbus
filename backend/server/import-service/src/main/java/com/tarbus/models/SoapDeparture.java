package com.tarbus.models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class SoapDeparture {
  private final int busStopId;
  private final int busStopNo;
  private final String driverNotes;
  private final String passengerNotes;
  private final String time;

  private final String dayId;
  private final String courseId;
  private final String direction;
  private final String globalDriverNotes;


  public SoapDeparture(Node node, String dayId, String courseId, String direction, String globalDriverNotes) {
    Map<String, String> result = new HashMap<>();
    NodeList nodeList = node.getChildNodes();
    for( int i = 0; i < nodeList.getLength(); i++ ) {
      result.put(nodeList.item(i).getLocalName(), nodeList.item(i).getTextContent());
    }
    this.busStopId = parseInt(result.get("BusStopId"));
    this.busStopNo = parseInt(result.get("BusStopNo"));
    this.driverNotes = result.get("DriverNotes");
    this.passengerNotes = result.get("PassengNotes");
    this.time = result.get("Time");

    this.dayId = dayId;
    this.courseId = courseId;
    this.direction = direction;
    this.globalDriverNotes = globalDriverNotes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SoapDeparture)) return false;
    SoapDeparture that = (SoapDeparture) o;
    return busStopNo == that.busStopNo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(busStopNo);
  }

  public int getBusStopId() {
    return busStopId;
  }

  public int getBusStopNo() {
    return busStopNo;
  }

  public String getDriverNotes() {
    return driverNotes;
  }

  public String getPassengerNotes() {
    return passengerNotes;
  }

  public String getTime() {
    return time;
  }

  public String getDayId() {
    return dayId;
  }

  public String getCourseId() {
    return courseId;
  }

  public String getDirection() {
    return direction;
  }

  public String getGlobalDriverNotes() {
    return globalDriverNotes;
  }

  @Override
  public String toString() {
    return "SoapDeparture{" +
      "busStopId=" + busStopId +
      ", busStopNo=" + busStopNo +
      ", driverNotes='" + driverNotes + '\'' +
      ", passengerNotes='" + passengerNotes + '\'' +
      ", time='" + time + '\'' +
      '}';
  }
}
