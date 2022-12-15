package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "routes", schema = "schedule")
public class Route implements DatabaseObject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "destination_name")
  private String name;
  @Column(name = "destination_desc")
  private String destinationDescription;

  @Column(name = "comments")
  private String comments;
  @Column(name = "details")
  private String details;


  @Column(name = "date_start")
  private String dateStart;

  @Column(name = "date_end")
  private String dateEnd;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "bus_line_id", referencedColumnName = "id")
  private BusLine busLine;


  public Route(Long id) {
    this.id = id;
  }

  public Route(Long id, String name, String destinationDescription, String details, BusLine busLine) {
    this.id = id;
    this.name = name;
    this.destinationDescription = destinationDescription;
    this.details = details;
    this.busLine = busLine;
  }

  public String joinDestinationDescription(String delimiter) {
    String[] descriptionAsArray = destinationDescription.split(",");
    return String.join(delimiter, descriptionAsArray);
  }

  public Map<String, Object> toJson() {
    Map<String, Object> map = new HashMap<>();
    map.put("routeId", id);
    map.put("routeName", name);
    map.put("routeDesc", destinationDescription);
    map.put("routeDetails", details);
    return map;
  }


  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "destination_name":
        return name;
      case "destination_desc":
        return destinationDescription;
      case "details":
        return details;
      case "bus_line_id":
        return busLine.getId();
    }
    return null;
  }

  @Override
  public String toString() {
    return "Route{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + destinationDescription + '\'' +
      ", details='" + details + '\'' +
      ", busLine=" + busLine +
      '}';
  }
}
