package com.tarbus.models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class TimeTableInfo {
  private final int errorCode;
  private final int timeTableVersion;
  private final String timeTableDate;
  private final int timeTableGenerationNumber;

  public TimeTableInfo(Node node) {
    Map<String, String> result = new HashMap<>();
    NodeList nodeList = node.getChildNodes();
    for( int i = 0; i < nodeList.getLength(); i++ ) {
      result.put(nodeList.item(i).getLocalName(), nodeList.item(i).getTextContent());
    }
    this.errorCode = parseInt(result.get("ErrCode"));
    this.timeTableVersion = parseInt(result.get("TimeTableVersion"));
    this.timeTableDate = result.get("TimeTableDate");
    this.timeTableGenerationNumber = parseInt(result.get("TimeTableGenerationNumber"));
  }

  public int getErrorCode() {
    return errorCode;
  }

  public int getTimeTableVersion() {
    return timeTableVersion;
  }

  public String getTimeTableDate() {
    return timeTableDate;
  }

  public int getTimeTableGenerationNumber() {
    return timeTableGenerationNumber;
  }

  @Override
  public String toString() {
    return "TimeTableInfo{" +
      "errorCode=" + errorCode +
      ", timeTableVersion=" + timeTableVersion +
      ", timeTableDate='" + timeTableDate + '\'' +
      ", timeTableGenerationNumber=" + timeTableGenerationNumber +
      '}';
  }
}
