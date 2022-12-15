package com.tarbus.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "bus_stops", schema="schedule")
public class BusStop implements DatabaseObject {
  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "search_name")
  private String searchName;
  @Column(name = "name")
  private String name;
  @Column(name = "destinations")
  private String destinations;
  @Column(name = "city")
  private String city;
  @Column(name = "road_type")
  private String roadType;
  @Column(name = "lat")
  private Double lat;
  @Column(name = "lng")
  private Double lng;
  @Column(name = "external_id")
  private Long externalId;

  public BusStop(Long id, String searchName, String name, String destinations, String city, String roadType, Double lat, Double lng, Long externalId) {
    this.id = id;
    this.searchName = searchName;
    this.name = name;
    this.destinations = destinations;
    this.city = city;
    this.roadType = roadType;
    this.lat = lat;
    this.lng = lng;
    this.externalId = externalId;
  }

  public BusStop(Long id, String name, String destinations, Double lat, Double lng) {
    this.id = id;
    this.name = name;
    this.destinations = destinations;
    this.lat = lat;
    this.lng = lng;
  }
  
  boolean hasCoords() {
    return this.lng != null && this.lat != null;
  }

  public BusStop() {
  }

  public BusStop(Long id) {
    this.id = id;
  }

  public Long getExternalId() {
    return externalId;
  }

  public void setExternalId(Long externalId) {
    this.externalId = externalId;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDestinations() {
    return destinations;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRoadType() {
    return roadType;
  }

  public void setRoadType(String roadType) {
    this.roadType = roadType;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }


  public Double getLat() {
    return lat;
  }

  public String getSearchName() {
    return searchName;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public Double getLng() {
    return lng;
  }

  public void setDestinations(String destinations) {
    this.destinations = destinations;
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "search_name":
        return searchName;
      case "name":
        return name;
      case "destinations":
        return destinations;
      case "lat":
        return lat;
      case "lng":
        return lng;
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BusStop)) return false;
    BusStop busStop = (BusStop) o;
    return id.equals(busStop.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "BusStop{" +
      "id=" + id +
      ", searchName='" + searchName + '\'' +
      ", name='" + name + '\'' +
      ", destinations='" + destinations + '\'' +
      ", city='" + city + '\'' +
      ", roadType='" + roadType + '\'' +
      ", lat=" + lat +
      ", lng=" + lng +
      '}';
  }
}
