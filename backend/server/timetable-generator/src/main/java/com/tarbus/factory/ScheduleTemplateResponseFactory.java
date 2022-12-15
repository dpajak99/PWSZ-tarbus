package com.tarbus.factory;

import com.tarbus.models.jpa.CompanyScheduleTemplate;
import com.tarbus.models.jpa.ScheduleTemplate;

import java.util.*;

public class ScheduleTemplateResponseFactory {

  public static Map<String, Object> prepareAvailableTemplatesFactory(CompanyScheduleTemplate companySettings, List<ScheduleTemplate> schedules) {
    // TODO
    Map<String, Object> result = new HashMap<>();
    List<Map<String, Object>> templatesOwned = new ArrayList<>();
    for( ScheduleTemplate scheduleTemplate : schedules ) {
      List<String> status = new ArrayList<>(Collections.singletonList("OWNED"));
      Map<String, Object> item = scheduleTemplate.toJson();
//      if(scheduleTemplate.getId().equals(companySettings.getActiveTemplate())) {
//        status.add("ACTIVE");
//      }
      item.put("status", status);
      templatesOwned.add(item);
    }
    result.put("templatesOwned", templatesOwned);
    result.put("templatesFree", templatesOwned);
    result.put("templatesAvaliable", templatesOwned);
    return result;
  }
}
