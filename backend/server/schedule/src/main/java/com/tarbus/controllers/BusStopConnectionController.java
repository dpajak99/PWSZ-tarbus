package com.tarbus.controllers;

import com.tarbus.models.BusStopConnection;
import com.tarbus.services.BusStopConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class BusStopConnectionController {
  private BusStopConnectionService busStopConnectionService;

  @Autowired
  public void setBusStopConnectionService(BusStopConnectionService busStopConnectionService) {
    this.busStopConnectionService = busStopConnectionService;
  }

  @GetMapping({"/stopConnections"})
  public ResponseEntity<?> getConnections(@RequestParam(name = "from", required = false) Long from, @RequestParam(name = "to", required = false) Long to) {
    List<BusStopConnection> notParsedResultArray;
    if (from == null && to == null) {
      notParsedResultArray = busStopConnectionService.getAll();
    } else if (from == null) {
      notParsedResultArray = busStopConnectionService.getAllTo(to);
    } else if (to == null) {
      notParsedResultArray = busStopConnectionService.getAllFrom(from);
    } else {
      BusStopConnection connection = busStopConnectionService.getSingleConnection(from, to);
      return new ResponseEntity<>(connection, HttpStatus.OK);
    }
    return new ResponseEntity<>(notParsedResultArray, HttpStatus.OK);
  }


  @GetMapping({"/stopConnections/empty"})
  public ResponseEntity<Object> getEmptyConnections() {
    List<BusStopConnection> connections = busStopConnectionService.getEmptyConnections();
    return new ResponseEntity<>(connections, HttpStatus.OK);
  }

  @GetMapping({"/stopConnections/findEmpty"})
  public ResponseEntity<Object> findEmptyConnections() {
    int newConnections = busStopConnectionService.fetchEmptyConnections();
    return new ResponseEntity<>(newConnections, HttpStatus.OK);
  }

  @PostMapping({"/stopConnections"})
  public ResponseEntity<Object> updateConnections(@RequestBody List<BusStopConnection> connections) {
    busStopConnectionService.updateConnections(connections);
    return new ResponseEntity<>(connections, HttpStatus.OK);
  }

  @PutMapping({"/stopConnections"})
  public ResponseEntity<Object> updateConnection(@RequestBody BusStopConnection connection) {
    BusStopConnection fullConnection = busStopConnectionService.getSingleConnection(connection.getBusStopConnectionId().getFromBusStopId().getId(), connection.getBusStopConnectionId().getToBusStopId().getId());
    if( connection.getDistance() != null ) {
      fullConnection.setDistance(connection.getDistance());
    }
    if( connection.getCoordsList() != null ) {
      fullConnection.setCoordsList(connection.getCoordsList());
    }
    BusStopConnection result = busStopConnectionService.save(fullConnection);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping({"/stopConnections/delete"})
  public ResponseEntity<Object> updateConnection(@RequestBody List<BusStopConnection> connections) { ;
    busStopConnectionService.deleteAll(connections);
    return new ResponseEntity<>(connections, HttpStatus.OK);
  }
}
