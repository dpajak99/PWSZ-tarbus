package com.tarbus.controllers;

import com.tarbus.models.BusLine;
import com.tarbus.models.Route;
import com.tarbus.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class RouteController {
  private RouteService routeService;

  @Autowired
  public void setRouteService(RouteService routeService) {
    this.routeService = routeService;
  }

  @GetMapping({"/routes/{id}"})
  public ResponseEntity<Object> getRouteDetails(@PathVariable() Long id) {
    Optional<Route> route = routeService.findById(id);
    if( route.isPresent() ) {
      return new ResponseEntity<>(route.get(), HttpStatus.OK);
    }
    throw new RuntimeException();
  }

  @GetMapping({"/routes"})
    public ResponseEntity<Object> getAllRoutes(@RequestParam(name = "versionId", required = false) Long versionId, @RequestParam(name = "busLineId", required = false) Long busLineId) {
      List<Object> routes = new ArrayList<>();
        if(versionId != null ) {
          routes = routeService.getByVersion(versionId);
        } else if( busLineId != null ) {
          routes = routeService.getByLine(busLineId);
        } else {
          return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }
}
