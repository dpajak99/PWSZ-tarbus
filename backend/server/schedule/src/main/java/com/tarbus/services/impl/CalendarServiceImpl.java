package com.tarbus.services.impl;

import com.tarbus.models.ScheduleCalendar;
import com.tarbus.repositories.jpa.CalendarRepository;
import com.tarbus.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarServiceImpl(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }


    @Override
    public List<ScheduleCalendar> getAll() {
        return StreamSupport.stream(calendarRepository.findAll().spliterator(), false)
          .collect(Collectors.toList());
    }

    @Override
    public ScheduleCalendar getToday() {
        Date nowDate = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dt.format(nowDate);
        return calendarRepository.findByDate(dateString);
    }

  @Override
  public ScheduleCalendar getByDate(String dateString) {
    return calendarRepository.findByDate(dateString);
  }
}
