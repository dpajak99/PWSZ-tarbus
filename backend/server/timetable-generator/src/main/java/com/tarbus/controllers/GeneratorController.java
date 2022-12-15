package com.tarbus.controllers;

import com.tarbus.factory.ScheduleTemplateResponseFactory;
import com.tarbus.models.jpa.CompanyScheduleTemplateId;
import com.tarbus.models.jpa.ScheduleTemplate;
import com.tarbus.models.TemplateSettings;
import com.tarbus.repositories.jpa.UserRepository;
import com.tarbus.services.CompanyService;
import com.tarbus.services.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/generator/")
public class GeneratorController {
  private GeneratorService generatorService;
  private CompanyService companyService;
  @Autowired
  UserRepository userRepository;

  @Autowired
  public void setGeneratorService(GeneratorService generatorService) {
    this.generatorService = generatorService;
  }

  @Autowired
  public void setCompanyService(CompanyService companyService) {
    this.companyService = companyService;
  }

// TODO
//  @GetMapping({"/availableTemplates"})
//  public ResponseEntity<?> getAvailableTemplates(@RequestParam() Long versionId) {
//    List<ScheduleTemplate> schedules = generatorService.getAvailableTemplates();
//    CompanyScheduleTemplateId companySettings = companyService.getCompanyVersionSettings(versionId);
//    Map<String, Object> response = ScheduleTemplateResponseFactory.prepareAvailableTemplatesFactory(companySettings, schedules);
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }

  @PostMapping({"/generateSchedule"})
  public ResponseEntity<?> generateSchedule(@RequestBody TemplateSettings templateSettings) {
    Object files =  generatorService.generateSchedule(templateSettings);
    if( files != null ) {
      return new ResponseEntity<>(files, HttpStatus.OK);
    }
    return new ResponseEntity<>("Cannot create schedules", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
