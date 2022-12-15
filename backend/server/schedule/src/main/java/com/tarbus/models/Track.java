package com.tarbus.models;


import net.minidev.json.JSONObject;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tracks", schema="schedule")
public class Track implements DatabaseObject {
  @Id
  @Column(name = "id", unique = true, nullable = false)
  private String id;
  @Column(name = "lp")
  private Long lp;
  @Column(name = "day_string")
  private String dayString;
  @Column(name = "day_types")
  private String dayTypes;
  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "route_id", referencedColumnName = "id")
  private Route route;

  public Track() {
  }

  public Track(String id, Long lp, String dayString, String dayTypes, Route route) {
    this.id = id;
    this.lp = lp;
    this.dayString = dayString;
    this.dayTypes = dayTypes;
    this.route = route;
  }

  public static List<Object> toJson(List<Track> list) {
    List<Object> result = new ArrayList<>();
    for (Track track : list) {
      result.add(track.toJson());
    }
    return result;
  }

  public JSONObject toJson() {
    Map<String, Object> map = new HashMap<>();
    map.put("trackId", id);
    map.put("trackLp", lp);
    map.put("trackDayString", dayString);
    map.put("trackDayTypes", dayTypes);
    return new JSONObject(map);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getLp() {
    return lp;
  }

  public void setLp(Long lp) {
    this.lp = lp;
  }

  public String getDayString() {
    return dayString;
  }

  public void setDayString(String dayString) {
    this.dayString = dayString;
  }

  public String getDayTypes() {
    return dayTypes;
  }

  public void setDayTypes(String dayTypes) {
    this.dayTypes = dayTypes;
  }

  public Route getRoute() {
    return route;
  }

  public void setRoute(Route route) {
    this.route = route;
  }

  @PrePersist
  public void autofill() {
    this.setId(UUID.randomUUID().toString());
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "lp":
        return lp;
      case "day_string":
        return dayString;
      case "day_types":
        return dayTypes;
      case "route_id":
        return route.getId();
    }
    return null;
  }

  @Override
  public String toString() {
    return "Track{" +
      "id='" + id + '\'' +
      ", lp=" + lp +
      ", dayString='" + dayString + '\'' +
      ", dayTypes='" + dayTypes + '\'' +
      ", route=" + route +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Track)) return false;
    Track track = (Track) o;
    return id.equals(track.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
