package com.tarbus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tarbus.services.ExportService;


@RestController
@CrossOrigin
@RequestMapping("/api/export/")
public class ExportController {
  private ExportService exportService;

  @Autowired
  public void setExportService(ExportService exportService) {
    this.exportService = exportService;
  }


  @GetMapping({"/exportByVersion"})
  public ResponseEntity<?> exportByVersion(@RequestParam(value="versionId") Long versionId) {
    String response = exportService.exportByVersion(versionId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping({"/exportAll"})
  public ResponseEntity<?> exportAll() {
    String response = exportService.exportAll();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
