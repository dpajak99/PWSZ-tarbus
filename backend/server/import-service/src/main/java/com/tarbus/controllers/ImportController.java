package com.tarbus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tarbus.services.ImportService;


@RestController
@CrossOrigin
@RequestMapping("/api/import/")
public class ImportController {
  private ImportService importService;

  @Autowired
  public void setImportService(ImportService importService) {
    this.importService = importService;
  }


  @GetMapping({"/importSchedule"})
  public ResponseEntity<?> importSchedule() {
    String result = importService.fetchMpkSchedule();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
