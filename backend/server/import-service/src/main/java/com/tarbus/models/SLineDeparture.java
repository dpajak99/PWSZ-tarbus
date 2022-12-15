package com.tarbus.models;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SLineDeparture {
  private String th;
  private String tm;
  private String uw;
  private String courseId;
  private String uid;
  private List<SDepartureSymbol> symbols;

  public SLineDeparture(Node node) {
    this.th = node.getAttributes().getNamedItem("th").getTextContent();
    this.tm = node.getAttributes().getNamedItem("tm").getTextContent();
    this.uw = node.getAttributes().getNamedItem("uw").getTextContent();
    this.courseId = node.getAttributes().getNamedItem("id_kursu").getTextContent();
    this.uid = node.getAttributes().getNamedItem("uid").getTextContent();
    this.symbols = new ArrayList<>();
    for( int i = 0; i < node.getChildNodes().getLength(); i++ ) {
      this.symbols.add(new SDepartureSymbol(node.getChildNodes().item(i)));
    }
  }

  public String getTh() {
    return th;
  }

  public String getTm() {
    return tm;
  }

  public String getUw() {
    return uw;
  }

  public String getCourseId() {
    return courseId;
  }

  public String getUid() {
    return uid;
  }

  public int getTimeInMin() {
    return Integer.parseInt(th) * 60 + Integer.parseInt(tm);
  }

  public List<SDepartureSymbol> getSymbols() {
    return symbols;
  }

  @Override
  public String toString() {
    return "SLineDeparture{" +
      "th='" + th + '\'' +
      ", tm='" + tm + '\'' +
      ", uw='" + uw + '\'' +
      ", courseId='" + courseId + '\'' +
      ", uid='" + uid + '\'' +
      ", symbols=" + symbols +
      '}';
  }
}
