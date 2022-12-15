package com.tarbus.controllers;

import com.tarbus.models.BusLine;
import com.tarbus.services.BusLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class BusLineController {
    private BusLineService busLineService;

    @Autowired
    public void setBusLineService(BusLineService busLineService) {
        this.busLineService = busLineService;
    }


    @GetMapping({"/lines"})
    public ResponseEntity<List<BusLine>> getAll(@RequestParam(value="versionId", required = false) Long versionId) {
        List<BusLine> allBusLines = busLineService.getAll(versionId);
        allBusLines.sort(Comparator.comparing(BusLine::getLp));
        return new ResponseEntity<>(allBusLines, HttpStatus.OK);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<BusLine> getById(@PathVariable Long id) {
        BusLine busLine = busLineService.getById(id);
        return new ResponseEntity<>(busLine, HttpStatus.OK);
    }

    @PostMapping("/lines")
    public ResponseEntity<List<BusLine>> updateLines(@RequestBody List<BusLine> busLines) {
        busLineService.saveLinesList(busLines);
        return new ResponseEntity<>(busLines, HttpStatus.OK);
    }

    @PostMapping("/lines/delete")
    public ResponseEntity<List<BusLine>> deleteLines(@RequestBody List<BusLine> busLines) {
        busLineService.deleteLinesList(busLines);
        return new ResponseEntity<>(busLines, HttpStatus.OK);
    }

    @DeleteMapping("/lines/delete/{id}")
    public ResponseEntity<Long> deleteSingleLine(@PathVariable Long id) {
        busLineService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
