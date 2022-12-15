package com.tarbus.controllers;

import com.tarbus.models.Departure;
import com.tarbus.services.DepartureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class DepartureController {
  private DepartureService departureService;

  @Autowired
  public void setDepartureService(DepartureService departureService) {
    this.departureService = departureService;
  }

  @GetMapping({"/departures"})
  public ResponseEntity<Object> getBusStopTimetable(@RequestParam(name = "busStopId") Long busStopId, @RequestParam(name = "routeId") Long routeId) {
    Map<String, Object> schedule = departureService.getBusStopTimetable(busStopId, routeId);
    return new ResponseEntity<>(schedule, HttpStatus.OK);
  }

  @GetMapping({"/departures/next"})
  public ResponseEntity<Object> getNextDepartures(@RequestParam(name = "versionId") Long versionId, @RequestParam(name = "busStopId") Long busStopId, @RequestParam(name = "count") int count) {
    List<Departure> departures = departureService.getNextDepartures(versionId, busStopId, count);
    return new ResponseEntity<>(departures, HttpStatus.OK);
  }

  @GetMapping("/departures/route")
  public ResponseEntity<Object> getRouteDepartures(@RequestParam(name = "routeId") Long routeId) {
    Map<String, Object> routeDepartures = departureService.getRouteDepartures(routeId);
    return new ResponseEntity<>(routeDepartures, HttpStatus.OK);
  }

  @GetMapping("/departures/{id}")
  public ResponseEntity<Object> getDepartureById(@PathVariable Long id) {
    Departure departure = departureService.getById(id);
    return new ResponseEntity<>(departure, HttpStatus.OK);
  }

  @PostMapping("/departures")
  public ResponseEntity<Object> updateDepartures(@RequestBody List<Departure> departures) {
    departureService.update(departures);
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  @PostMapping("/departures/delete")
  public ResponseEntity<Object> deleteAll(@RequestBody List<Departure> departures) {
    departureService.deleteAll(departures);
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  @DeleteMapping("/departures")
  public ResponseEntity<Object> delete(@RequestBody Long id) {
    Departure departure = new Departure();
    departure.setId(id);
    departureService.delete(departure);
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }
}
