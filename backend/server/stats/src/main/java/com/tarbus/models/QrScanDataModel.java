package com.tarbus.models;

import javax.persistence.*;

@Entity
@Table(name = "qr_scans", schema="stats")
public class QrScanDataModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "direct_from")
  private String directFrom;

  @Column(name = "operating_system")
  private String operatingSystem;

  @Column(name = "browser")
  private String browser;

  @Column(name = "bus_stop_id")
  private Long busStopId;

  @Column(name = "bus_line_id")
  private Long busLineId;

  public QrScanDataModel(String directFrom, String operatingSystem, String browser, Long busStopId, Long busLineId) {
    this.directFrom = directFrom;
    this.operatingSystem = operatingSystem;
    this.browser = browser;
    this.busStopId = busStopId;
    this.busLineId = busLineId;
  }

  public QrScanDataModel() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDirectFrom() {
    return directFrom;
  }

  public void setDirectFrom(String directFrom) {
    this.directFrom = directFrom;
  }

  public Long getBusStop() {
    return busStopId;
  }

  public void setBusStop(Long busStop) {
    this.busStopId = busStop;
  }

  public Long getBusLine() {
    return busLineId;
  }

  public void setBusLine(Long busLine) {
    this.busLineId = busLine;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }
}
