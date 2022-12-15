package com.tarbus.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "schedule_calendar", schema="schedule")
public class ScheduleCalendar implements DatabaseObject {
    @Id
    @Column(name = "date")
    private String date;
    @Column(name = "day_types")
    private String dayTypes;

    public ScheduleCalendar() {}

    public ScheduleCalendar(String date, String dayTypes) {
        this.date = date;
        this.dayTypes = dayTypes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayTypes() {
        return dayTypes;
    }

    public void setDayTypes(String dayTypes) {
        this.dayTypes = dayTypes;
    }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "date":
        List<String> dateArray = Arrays.asList(date.split("-"));
        return dateArray.get(2) + "-" + dateArray.get(1) + "-" + dateArray.get(0);
      case "day_types":
        return dayTypes;
    }
    return null;
  }
}
