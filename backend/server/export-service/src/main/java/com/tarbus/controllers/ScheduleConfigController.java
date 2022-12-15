package com.tarbus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tarbus.services.ScheduleConfigService;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/schedule-config/")
public class ScheduleConfigController {
  private ScheduleConfigService scheduleConfigService;

  @Autowired
  public void setScheduleConfigService(ScheduleConfigService scheduleConfigService) {
    this.scheduleConfigService = scheduleConfigService;
  }


  @PostMapping({"/generateDatabaseInfo"})
  public ResponseEntity<?> generateDatabaseInfo() throws RuntimeException {
    Map<String, Object> result = scheduleConfigService.generateDatabaseInfo();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
