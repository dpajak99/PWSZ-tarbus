package com.tarbus.controllers;

import com.tarbus.models.Track;
import com.tarbus.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class TrackController {
    private TrackService trackService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping({"/tracks"})
    public ResponseEntity<List<Object>> getAll(@RequestParam("versionId") Long versionId) {
        List<Track> tracks = trackService.getAll(versionId);
        List<Object> result = Track.toJson(tracks);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<Object> getTrackById(@PathVariable String id) {
        Object track = trackService.getById(id);
        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @PostMapping("/tracks")
    public ResponseEntity<Object> update(@RequestBody List<Track> tracks) {
        trackService.addAll(tracks);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

  @PostMapping("/tracks/delete")
  public ResponseEntity<Object> deleteAll(@RequestBody List<Track> tracks) {
    trackService.deleteAll(tracks);
    return new ResponseEntity<>(tracks, HttpStatus.OK);
  }
}
