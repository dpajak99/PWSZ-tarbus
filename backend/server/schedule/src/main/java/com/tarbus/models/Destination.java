package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "destinations", schema = "schedule")
public class Destination implements DatabaseObject {
  @EmbeddedId
  private DestinationId destinationId;

  @Column(name = "direction_board_name")
  private String boardName;

  @Column(name = "schedule_name")
  private String scheduleName;


  public Destination(DestinationId destinationId, String boardName, String scheduleName) {
    this.destinationId = destinationId;
    this.boardName = boardName;
    this.scheduleName = scheduleName;
  }


  public static List<Object> toShort(List<Destination> list) {
    List<Object> result = new ArrayList<>();
    for (Destination destination : list) {
      result.add(destination.toShort());
    }
    return result;
  }

  public Object toTimetableJson() {
    return scheduleName;
  }


  public Map<String, Object> toShort() {
    Map<String, Object> result = new HashMap<>();
    result.put("symbols", destinationId.getSymbols());
    result.put("boardName", getBoardName());
    result.put("scheduleName", getScheduleName());
    return result;
  }

  @Override
  public String toString() {
    return "Destination{" + destinationId.getSymbols() + " - " + scheduleName +
      '}';
  }


  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "route_id":
        return destinationId.getId().getId();
      case "symbol":
        return destinationId.getSymbols();
      case "direction_board_name":
        return boardName;
      case "schedule_name":
        return scheduleName;
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Destination)) return false;
    Destination that = (Destination) o;
    return Objects.equals(destinationId, that.destinationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(destinationId);
  }
}
