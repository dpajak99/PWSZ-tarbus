package com.tarbus.models;

import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class SoapRouteVariant {
  private String lineName;
  private String destination;
  private String description;
  private String direction;
  private String wt;
  private String t;


  public SoapRouteVariant(Node node) {
    this.lineName = node.getAttributes().getNamedItem("l").getTextContent();
    this.destination = node.getAttributes().getNamedItem("o").getTextContent();
    this.description = node.getAttributes().getNamedItem("o2").getTextContent();
    this.direction = node.getAttributes().getNamedItem("d").getTextContent();
    this.wt = node.getAttributes().getNamedItem("wt").getTextContent();
    this.t = node.getAttributes().getNamedItem("t").getTextContent();
  }

  public String getLineName() {
    return lineName;
  }

  public String getDestination() {
    return destination;
  }

  public String getDescription() {
    return description;
  }

  public String getDirection() {
    return direction;
  }

  public String getWt() {
    return wt;
  }

  public String getT() {
    return t;
  }

  @Override
  public String toString() {
    return "SoapRouteVariant{" +
      "lineName='" + lineName + '\'' +
      ", destination='" + destination + '\'' +
      ", description='" + description + '\'' +
      ", direction='" + direction + '\'' +
      ", wt='" + wt + '\'' +
      ", t='" + t + '\'' +
      '}';
  }
}
