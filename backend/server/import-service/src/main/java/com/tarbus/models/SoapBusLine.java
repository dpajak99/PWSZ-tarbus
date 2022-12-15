package com.tarbus.models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class SoapBusLine {
  private int id;
  private String number;


  public SoapBusLine(Node node) {
    Map<String, String> result = new HashMap<>();
    NodeList nodeList = node.getChildNodes();
    for( int i = 0; i < nodeList.getLength(); i++ ) {
      result.put(nodeList.item(i).getLocalName(), nodeList.item(i).getTextContent());
    }
    this.id = parseInt(result.get("Id"));
    this.number = result.get("RouteNumber");
  }

  public int getId() {
    return id;
  }

  public String getNumber() {
    return number;
  }



  @Override
  public String toString() {
    return "SoapBusLine{" +
      "id=" + id +
      ", number='" + number + '\'' +
      '}';
  }
}
