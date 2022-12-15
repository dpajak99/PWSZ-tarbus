package com.tarbus.controllers;

import com.tarbus.models.Destination;
import com.tarbus.models.DestinationId;
import com.tarbus.models.Route;
import com.tarbus.services.DestinationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class DestinationController {
  private DestinationsService destinationsService;

  @Autowired
  public void setDestinationsService(DestinationsService destinationsService) {
    this.destinationsService = destinationsService;
  }


  @GetMapping({"/destinations"})
  public ResponseEntity<Object> getAll(@RequestParam("routeId") Long routeId, @RequestParam(value = "symbols", required = false) String symbol) {
    List<Destination> destinations;
    if (symbol == null) {
      destinations = destinationsService.getAllByRouteLong(routeId);
    } else {
      destinations = destinationsService.getAllByRouteAndSymbol(routeId, symbol);
    }

    List<Object> result = Destination.toShort(destinations);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping({"/destinations/byVersion"})
  public ResponseEntity<Object> getByVersion(@RequestParam("versionId") Long versionId) {
    Object destinations = destinationsService.getByVersion(versionId);
    return new ResponseEntity<>(destinations, HttpStatus.OK);
  }

  @PostMapping({"/destinations"})
  public ResponseEntity<Object> update(@RequestBody() List<Map<String, Object>> destination) {
    List<Destination> parsed = new ArrayList<>();
    for (Map<String, Object> item : destination) {
      parsed.add(new Destination(new DestinationId(new Route(((Number) item.get("routeId")).longValue()), (String) item.get("symbols")), (String) item.get("boardName"), (String) item.get("scheduleName")));
    }
    List<Destination> destinations = destinationsService.addAll(parsed);

    List<Object> result = Destination.toShort(destinations);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping({"/destinations/delete"})
  public ResponseEntity<Object> deleteAll(@RequestBody() List<Map<String, Object>> destination) {
    List<Destination> parsed = new ArrayList<>();
    for (Map<String, Object> item : destination) {
      parsed.add(new Destination(new DestinationId(new Route(((Number) item.get("routeId")).longValue()), (String) item.get("symbols")), (String) item.get("boardName"), (String) item.get("scheduleName")));
    }
    destinationsService.deleteAll(parsed);
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }
}
