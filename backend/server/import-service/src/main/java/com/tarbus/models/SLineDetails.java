package com.tarbus.models;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SLineDetails {
  private String nr;
  private String nrk;
  private String dir;
  private String vehType;
  private String vehSymbol;
  private String vehNote;
  private String desc;
  private List<SLineDeparture> departures;

  public SLineDetails(Node node) {
    this.nr = node.getAttributes().getNamedItem("nr").getTextContent();
    this.nrk = node.getAttributes().getNamedItem("nrk").getTextContent();
    this.dir = node.getAttributes().getNamedItem("dir").getTextContent();
    this.vehType = node.getAttributes().getNamedItem("vehType").getTextContent();
    this.vehSymbol = node.getAttributes().getNamedItem("vehSymbol").getTextContent();
    this.vehNote = node.getAttributes().getNamedItem("vehNote").getTextContent();
    this.desc = node.getAttributes().getNamedItem("desc").getTextContent();
    this.departures = new ArrayList<>();
    for( int i = 0; i < node.getChildNodes().getLength(); i++ ) {
      this.departures.add(new SLineDeparture(node.getChildNodes().item(i)));
    }
  }

  public String getNr() {
    return nr;
  }

  public String getNrk() {
    return nrk;
  }

  public String getDir() {
    return dir;
  }

  public String getVehType() {
    return vehType;
  }

  public String getVehSymbol() {
    return vehSymbol;
  }

  public String getVehNote() {
    return vehNote;
  }

  public String getDesc() {
    return desc;
  }

  public List<SLineDeparture> getDepartures() {
    return departures;
  }

  @Override
  public String toString() {
    return "SLineDetails{" +
      "nr='" + nr + '\'' +
      ", nrk='" + nrk + '\'' +
      ", dir='" + dir + '\'' +
      ", vehType='" + vehType + '\'' +
      ", vehSymbol='" + vehSymbol + '\'' +
      ", vehNote='" + vehNote + '\'' +
      ", desc='" + desc + '\'' +
      ", departures=" + departures +
      '}';
  }
}
