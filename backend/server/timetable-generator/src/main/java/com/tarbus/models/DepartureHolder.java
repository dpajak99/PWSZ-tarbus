package com.tarbus.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class DepartureHolder implements Serializable {
  List<SingleDeparture> emptyList = new ArrayList<>();
  @JsonProperty("RO")
  List<SingleDeparture> ro;
  @JsonProperty("WS")
  List<SingleDeparture> ws;
  @JsonProperty("SW")
  List<SingleDeparture> sw;

  
  public DepartureHolder(boolean empty) {
    ro = emptyList;
    ws = emptyList;
    sw = emptyList;
  }

  public DepartureHolder(List<SingleDeparture> ro, List<SingleDeparture> ws, List<SingleDeparture> sw) {
    this.ro = ro;
    this.ws = ws;
    this.sw = sw;
    sort();
  }

  public void sort() {
    ro.sort(Comparator.comparing(SingleDeparture::getSortHash));
    ws.sort(Comparator.comparing(SingleDeparture::getSortHash));
    sw.sort(Comparator.comparing(SingleDeparture::getSortHash));
  }

  public List<SingleDeparture> getMerged() {
    Set<SingleDeparture> merged = new HashSet<>();
    merged.addAll(ro);
    merged.addAll(ws);
    merged.addAll(sw);
    return new ArrayList<>(merged);
  }

  public boolean isEmpty() {
    return ro.isEmpty() && ws.isEmpty() && sw.isEmpty();
  }

  public List<SingleDeparture> getWeekendDepartures() {
    HashSet<SingleDeparture> singleDeparturesSet = new HashSet<>();
    singleDeparturesSet.addAll(ws);
    singleDeparturesSet.addAll(sw);
    List<SingleDeparture> singleDeparturesList = new ArrayList<>(singleDeparturesSet);
    singleDeparturesList.sort((o1, o2) -> {
      if(o1.getHourInt() == o2.getHourInt()) {
        return o1.getMinutesInt() - o2.getMinutesInt();
      }
      return o1.getHourInt() - o2.getHourInt();
    });
    return singleDeparturesList;
  }

  public boolean isListHasTime(String dayType, int hour) {
    List<SingleDeparture> list = new ArrayList<>();
    switch(dayType) {
      case "RO":
        list = ro;
        break;
      case "WS":
        list = ws;
        break;
      case "SW":
        list = sw;
        break;
    }
    for( SingleDeparture sd : list) {
      if( sd.getD().contains(":") && Integer.parseInt(sd.getD().split(":")[0])  == hour) {
        return true;
      }
    }
    return false;
  }
}
