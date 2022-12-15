package com.tarbus.models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class SoapNote {
  private String description;
  private String note;
  private String routeNumber;
  private String type;


  public SoapNote(Node node) {
    Map<String, String> result = new HashMap<>();
    NodeList nodeList = node.getChildNodes();
    for( int i = 0; i < nodeList.getLength(); i++ ) {
      result.put(nodeList.item(i).getLocalName(), nodeList.item(i).getTextContent());
    }
    this.description = result.get("Description");
    this.note = result.get("Note");
    this.routeNumber = result.get("RouteNumber");
    this.type = result.get("Type");
  }

  public String getDescription() {
    return description;
  }

  public String getNote() {
    return note;
  }

  public String getRouteNumber() {
    return routeNumber;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "SoapNote{" +
      "description='" + description + '\'' +
      ", note='" + note + '\'' +
      ", routeNumber='" + routeNumber + '\'' +
      ", type='" + type + '\'' +
      '}';
  }
}
