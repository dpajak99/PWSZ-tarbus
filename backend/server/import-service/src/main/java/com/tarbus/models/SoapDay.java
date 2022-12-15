package com.tarbus.models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class SoapDay {
  private String dayId;
  private String dayName;


  public SoapDay(Node node) {
    Map<String, String> result = new HashMap<>();
    NodeList nodeList = node.getChildNodes();
    for( int i = 0; i < nodeList.getLength(); i++ ) {
      result.put(nodeList.item(i).getLocalName(), nodeList.item(i).getTextContent());
    }
    this.dayId = result.get("DayId");
    this.dayName = result.get("DayName");
  }

  public String getDayId() {
    return dayId;
  }

  public String getDayName() {
    return dayName;
  }

  @Override
  public String toString() {
    return "SoapDay{" +
      "dayId='" + dayId + '\'' +
      ", dayName='" + dayName + '\'' +
      '}';
  }
}
