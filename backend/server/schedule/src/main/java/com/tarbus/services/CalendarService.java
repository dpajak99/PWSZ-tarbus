package com.tarbus.services;

import com.tarbus.models.ScheduleCalendar;

import java.util.List;

public interface CalendarService {

    List<ScheduleCalendar> getAll();
    ScheduleCalendar getToday();
    ScheduleCalendar getByDate(String dateString);
}
