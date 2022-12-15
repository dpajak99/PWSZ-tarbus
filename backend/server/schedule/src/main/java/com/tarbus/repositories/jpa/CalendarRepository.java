package com.tarbus.repositories.jpa;


import com.tarbus.models.ScheduleCalendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CalendarRepository extends CrudRepository<ScheduleCalendar, Long> {
  @Query("SELECT c FROM ScheduleCalendar c WHERE c.date = ?1")
  ScheduleCalendar findByDate(String date);
}
