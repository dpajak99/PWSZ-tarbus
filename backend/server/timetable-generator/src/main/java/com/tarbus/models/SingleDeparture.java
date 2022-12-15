package com.tarbus.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class SingleDeparture implements Serializable {
  @JsonProperty("id")
  String id;
  @JsonProperty("d")
  String d;
  @JsonProperty("s")
  String s;
  @JsonProperty("trackId")
  String trackId;
  @JsonProperty("isLast")
  boolean isLast;

  public SingleDeparture(String id, String d, String s, boolean isLast) {
    this.id = id;
    this.d = d;
    this.s = s;
    this.isLast = isLast;
  }

  public List<String> getSymbolArray() {
    Set<String> symbolsSet = new HashSet<>(Arrays.asList(s.split(" ")));
    List<String> symbols = new ArrayList<>(symbolsSet);
    List<String> finalSymbols = new ArrayList<>();
    for (String symbol : symbols) {
      if (!symbol.equals("-")) {
        finalSymbols.add(symbol);
      }
    }
    return finalSymbols;
  }

  public int getHourInt() {
    return Integer.parseInt(d.split(":")[0]);
  }

  public int getMinutesInt() {
    return Integer.parseInt(d.split(":")[1]);
  }

  public String getMinutesString() {
    int minutes = getMinutesInt();
    return parseTime(minutes);
  }

  public String getHourString() {
    int hours = getHourInt();
    return parseTime(hours);
  }

  public String getHourString24() {
    int hours = getHourInt();
    if( hours == 0 ) {
      return "24";
    }
    return hours + "";
  }

  public String parseTime(int time) {
    if (time < 10) {
      return "0" + time;
    }
    return time + "";
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SingleDeparture)) return false;
    SingleDeparture that = (SingleDeparture) o;
    return Objects.equal(d, that.d) && Objects.equal(s, that.s);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(d, s);
  }

  public String getSortHash() {
    String hourString = getHourString();
    if(java.util.Objects.equals(hourString, "0") || java.util.Objects.equals(hourString, "00")) {
      hourString = "24";
    }
    String minutesString = getMinutesString();
    return hourString + "" + minutesString + "" + s;
  }
}
