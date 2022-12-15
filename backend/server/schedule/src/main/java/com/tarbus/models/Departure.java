package com.tarbus.models;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "departures", schema="schedule")
public class Departure implements DatabaseObject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "bus_stop_id", referencedColumnName = "id", nullable = false)
  private BusStop busStop;

  @Column(name = "bus_stop_no")
  private Long busStopNo;

  @Column(name = "external_uuid")
  private String externalUuid;

  @Column(name = "time_string")
  private String time;

  @Column(name = "is_last_departure")
  private Boolean isLastDeparture;

  @Column(name = "time_in_min")
  private Long timeInMin;

  @Column(name = "symbols")
  private String symbols;

  @OneToOne()
  @JoinColumn(name = "track_id", referencedColumnName = "id")
  private Track track;

  public Departure() {
  }

  public Departure(Long id, BusStop busStop, Long busStopNo, String externalUuid, String time, Boolean isLastDeparture, Long timeInMin, String symbols, Track track) {
    this.id = id;
    this.busStop = busStop;
    this.busStopNo = busStopNo;
    this.externalUuid = externalUuid;
    this.time = time;
    this.isLastDeparture = isLastDeparture;
    this.timeInMin = timeInMin;
    this.symbols = symbols;
    this.track = track;
  }

  public Long getBusStopNo() {
    return busStopNo;
  }

  public Boolean getLastDeparture() {
    return isLastDeparture;
  }

  public Map<String, Object> toTimetableJson() {
    Map<String, Object> result = new HashMap<>();
    result.put("id", id);
    result.put("d", time);
    result.put("s", symbols);
    result.put("isLast", isLastDeparture);
    result.put("trackId", track.getId());
    return result;
  }

  public static Map<String, Object> getEmptyJson() {
    Map<String, Object> result = new HashMap<>();
    result.put("id", null);
    result.put("d", "-");
    result.put("s", "-");
    result.put("isLast", false);
    result.put("trackId", null);
    return result;
  }


  public void setId(Long id) {
    this.id = id;
  }

  public void setBusStop(BusStop busStop) {
    this.busStop = busStop;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getExternalUuid() {
    return externalUuid;
  }

  public void setExternalUuid(String externalUuid) {
    this.externalUuid = externalUuid;
  }

  public void setTimeInMin(Long timeInMin) {
    this.timeInMin = timeInMin;
  }

  public void setSymbols(String symbols) {
    this.symbols = symbols;
  }

  public void setTrack(Track track) {
    this.track = track;
  }

  public Long getId() {
    return id;
  }

  public BusStop getBusStop() {
    return busStop;
  }

  public String getTime() {
    return time;
  }

  public Long getTimeInMin() {
    return timeInMin;
  }

  public String getSymbols() {
    return symbols;
  }

  public Track getTrack() {
    return track;
  }

  public Boolean getIsLastDeparture() {
    return isLastDeparture;
  }

  public void setIsLastDeparture(Boolean isLastDeparture) {
    this.isLastDeparture = isLastDeparture;
  }



  @Override
  public String toString() {
    return "Departure{" +
      "id=" + id +
      ", busStopId=" + busStop.getId() +
      ", time='" + time + '\'' +
      ", timeInMin=" + timeInMin +
      ", symbols='" + symbols + '\'' +
      ", track=" + track +
      '}';
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "bus_stop_id":
        return busStop.getId();
      case "time_string":
        return time;
      case "time_in_min":
        return timeInMin;
      case "symbols":
        return symbols;
      case "track_id":
        return track.getId();
      case "is_last_departure":
        return isLastDeparture;
      case "external_uuid":
        return externalUuid;

      // OLD VERSION
      case "bus_stop_lp":
        return 0;
      case "bus_line_id":
        return track.getRoute().getBusLine().getId();
    }
    return null;
  }

  public void setBusStopNo(Long busStopNo) {
    this.busStopNo = busStopNo;
  }

  public void setLastDeparture(Boolean lastDeparture) {
    isLastDeparture = lastDeparture;
  }
}
