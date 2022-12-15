package com.tarbus.models;

import org.w3c.dom.Node;

import java.util.List;

public class SDepartureSymbol {
  private String ozn;
  private String oznDesc;
  private List<SDepartureSymbol> symbols;

  public SDepartureSymbol(Node node) {
    this.ozn = node.getAttributes().getNamedItem("ozn").getTextContent();
    this.oznDesc = node.getAttributes().getNamedItem("oznDesc").getTextContent();
  }

  public String getOzn() {
    return ozn;
  }

  public String getOznDesc() {
    return oznDesc;
  }

  public List<SDepartureSymbol> getSymbols() {
    return symbols;
  }

  @Override
  public String toString() {
    return "SDepartureSymbol{" +
      "ozn='" + ozn + '\'' +
      ", oznDesc='" + oznDesc + '\'' +
      ", symbols=" + symbols +
      '}';
  }
}
