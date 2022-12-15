package com.tarbus.services;

import com.tarbus.models.jpa.ScheduleTemplate;
import com.tarbus.models.TemplateSettings;

import java.util.List;

public interface GeneratorService {
  Object generateSchedule(TemplateSettings tData);
  List<ScheduleTemplate> getAvailableTemplates();
}
