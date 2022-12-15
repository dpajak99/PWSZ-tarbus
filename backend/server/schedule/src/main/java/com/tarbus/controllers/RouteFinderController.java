package com.tarbus.controllers;

import com.tarbus.services.RouteFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class RouteFinderController {
  @Autowired
  private RouteFinderService routeFinderService;

  @GetMapping({"/routefinder"})
  public ResponseEntity<?> getAll(@RequestParam(value="versionId", required = false) Long versionId) {
    Object routeFinderResponse = routeFinderService.find();
    return new ResponseEntity<>(routeFinderResponse, HttpStatus.OK);
  }
}
