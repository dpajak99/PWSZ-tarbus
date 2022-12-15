package com.tarbus.controllers;

import com.tarbus.models.RouteConnection;
import com.tarbus.services.RouteConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin()
@RequestMapping("/api/schedule/")
public class RouteConnectionController {
    private RouteConnectionService routeConnectionService;

    @Autowired
    public void setRouteService(RouteConnectionService routeConnectionService) {
        this.routeConnectionService = routeConnectionService;
    }

    @GetMapping({"/routeConnections"})
    public ResponseEntity<List<Object>> getAll(@RequestParam(name = "routeId") Long routeId) {
        List<RouteConnection> routeConnections = routeConnectionService.getByRoute(routeId);
        List<Object> result = RouteConnection.toJson(routeConnections);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping({"/routeConnections"})
    public ResponseEntity<Object> updateAll(@RequestBody List<RouteConnection> routeConnections) {
        routeConnectionService.updateRouteConnection(routeConnections);
        return new ResponseEntity<>(routeConnections, HttpStatus.OK);
    }

    @PostMapping({"/routeConnections/delete"})
    public ResponseEntity<Object> deleteAll(@RequestBody List<RouteConnection> routeConnections) {
        routeConnectionService.deleteRouteConnections(routeConnections);
        return new ResponseEntity<>(routeConnections, HttpStatus.OK);
    }
}
