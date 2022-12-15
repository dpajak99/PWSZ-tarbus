package com.tarbus.repositories.jpa;

import com.tarbus.models.jpa.ScheduleTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleTemplateRepository extends CrudRepository<ScheduleTemplate, String> {
  @Query("SELECT st FROM ScheduleTemplate st")
  List<ScheduleTemplate> getAvailableTemplates();
}
