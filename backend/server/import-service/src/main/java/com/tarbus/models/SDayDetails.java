package com.tarbus.models;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SDayDetails {
  private String desc;
  private String type;
  private String actual;
  private List<SLineDetails> routes;

  public SDayDetails(Node node) {
    this.desc = node.getAttributes().getNamedItem("desc").getTextContent();
    this.type = node.getAttributes().getNamedItem("type").getTextContent();
    this.actual = node.getAttributes().getNamedItem("actual").getTextContent();
    this.routes = new ArrayList<>();
    for( int i = 0; i < node.getChildNodes().getLength(); i++ ) {
      this.routes.add(new SLineDetails(node.getChildNodes().item(i)));
    }
  }

  public String getDesc() {
    return desc;
  }

  public String getType() {
    return type;
  }

  public String getActual() {
    return actual;
  }

  public List<SLineDetails> getRoutes() {
    return routes;
  }

  @Override
  public String toString() {
    return "SDayDetails{" +
      "desc='" + desc + '\'' +
      ", type='" + type + '\'' +
      ", actual='" + actual + '\'' +
      '}';
  }
}
