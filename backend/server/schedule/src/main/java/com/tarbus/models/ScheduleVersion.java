package com.tarbus.models;

import net.minidev.json.JSONObject;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "schedule_versions", schema="schedule")
public class ScheduleVersion implements DatabaseObject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  @Column(name = "short_name")
  private String shortName;

  @Column(name = "date_start")
  private String dateStart;

  @Column(name = "date")
  private String date;

  @Column(name = "subscribe_code")
  private String subscribeCode;

  @Column(name = "status")
  private String status;

  @Column(name = "active_status")
  private String activeStatus;

  @Column(name = "is_external")
  private boolean isExternal;

  public Object toJson() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("date", date);
    map.put("companyId", company.getId());
    map.put("companyName", company.getName());
    map.put("dateStart", dateStart);
    map.put("shortName", shortName);
    map.put("subscribeCode", subscribeCode);
    map.put("status", status);
    map.put("active_status", activeStatus);
    return new JSONObject(map);
  }

  public ScheduleVersion() {
  }

  public ScheduleVersion(Long id, Company company, String shortName, String dateStart, String date, String subscribeCode, String status, String activeStatus) {
    this.id = id;
    this.company = company;
    this.shortName = shortName;
    this.dateStart = dateStart;
    this.date = date;
    this.subscribeCode = subscribeCode;
    this.status = status;
    this.activeStatus = activeStatus;
  }

  public ScheduleVersion(Map<String, Object> json) {
    this.id = null;
    Company company = new Company();
    company.setId((long) (int) json.get("companyId"));
    this.company = company;
    this.shortName = (String) json.get("shortName");
    this.dateStart = (String) json.get("dateStart");
    this.date = (String) json.get("date");
    this.status = "creating";
  }

  public ScheduleVersion(Long id, Company company, String shortName, String dateStart, String date, String subscribeCode, String status) {
    this.id = id;
    this.company = company;
    this.shortName = shortName;
    this.dateStart = dateStart;
    this.date = date;
    this.subscribeCode = subscribeCode;
    this.status = status;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getDateStart() {
    return dateStart;
  }

  public void setDateStart(String dateStart) {
    this.dateStart = dateStart;
  }

  public void setSubscribeCode(String subscribeCode) {
    this.subscribeCode = subscribeCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public String getSubscribeCode() {
    return subscribeCode;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Company getCompany() {
    return company;
  }

  public String getActiveStatus() {
    return activeStatus;
  }

  public void setActiveStatus(String activeStatus) {
    this.activeStatus = activeStatus;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "company_id":
        return company.getId();
      case "date":
        return date;
      case "subscribe_code":
        return subscribeCode;
      case "is_external":
        return isExternal;
    }
    return null;
  }

  @Override
  public String toString() {
    return "ScheduleVersion{" +
      "id=" + id +
      ", company=" + company +
      ", shortName='" + shortName + '\'' +
      ", dateStart='" + dateStart + '\'' +
      ", date='" + date + '\'' +
      ", subscribeCode='" + subscribeCode + '\'' +
      ", status='" + status + '\'' +
      '}';
  }
}
