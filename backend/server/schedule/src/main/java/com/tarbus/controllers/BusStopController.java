package com.tarbus.controllers;

import com.tarbus.models.BusStop;
import com.tarbus.services.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class BusStopController {
  private BusStopService busStopService;

  @Autowired
  public void setBusLineService(BusStopService busStopService) {
    this.busStopService = busStopService;
  }


  @GetMapping({"/stops"})
  public ResponseEntity<Object> getAll(@RequestParam(required = false) String name) {
    List<BusStop> allBusStops = busStopService.getAll(name);
    return new ResponseEntity<>(allBusStops, HttpStatus.OK);
  }


  @GetMapping("/stops/{id}")
  public ResponseEntity<Object> getById(@PathVariable Long id) {
    BusStop busStop = busStopService.getById(id);
    return new ResponseEntity<>(busStop, HttpStatus.OK);
  }

  @PostMapping({"/stops"})
  public ResponseEntity<Object> updateAll(@RequestBody() List<BusStop> busStops) {
    for(BusStop busStop : busStops ) {
      System.out.println(busStop);
    }
    busStopService.updateBusStopList(busStops);
    return new ResponseEntity<>(busStops, HttpStatus.OK);
  }

  @PostMapping({"/stops/delete"})
  public ResponseEntity<Object> deleteAll(@RequestBody() List<BusStop> busStops) {
    busStopService.deleteAll(busStops);
    return new ResponseEntity<>("", HttpStatus.OK);
  }

  @DeleteMapping({"/stops/{id}"})
  public ResponseEntity<Object> delete(@PathVariable() Long id) {
    BusStop busStop = new BusStop();
    busStop.setId(id);
    busStopService.delete(busStop);
    return new ResponseEntity<>("", HttpStatus.OK);
  }
}
