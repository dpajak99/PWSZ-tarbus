package com.tarbus.services.impl;

import com.tarbus.models.Track;
import com.tarbus.repositories.jpa.TrackRepository;
import com.tarbus.services.DepartureService;
import com.tarbus.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackServiceImpl implements TrackService {

  private TrackRepository trackRepository;
  private DepartureService departureService;

  @Autowired
  public void setDepartureService(DepartureService departureService) {
    this.departureService = departureService;
  }

  @Autowired
  public TrackServiceImpl(TrackRepository trackRepository) {
    this.trackRepository = trackRepository;
  }


  @Override
  public Iterable<Track> getAll() {
    return trackRepository.findAll();
  }

  @Override
  public List<Track> getAll(Long versionId) {
    return trackRepository.getAll(versionId);
  }

  @Override
  public List<Track> getByRoute(Long routeId) {
    return trackRepository.getByRoute(routeId);
  }

  @Override
  public List<Long> getTracksContainsBusStop(Long busStopId) {
    return departureService.getTracksContainsBusStop(busStopId);
  }

  @Override
  public Object getById(String id) {
    Optional<Track> track = trackRepository.findById(id);
    return track.<Object>map(Track::toJson).orElse(null);
  }

  @Override
  public List<Track> addAll(List<Track> tracks) {
    return (List<Track>) trackRepository.saveAll(tracks);
  }

  @Override
  public void oldToNew() {
//        Iterable<Track> tracks = trackRepository.findAll();
//        List<Track> newTracks = new ArrayList<>();
//        Long id = (long) 1;
//        for(Track track : tracks ) {
//            String[] idParts = track.getOldId().split("-");
//            track.setLp(Integer.parseInt(idParts[1]));
//            track.setId(id);
//            id++;
//            newTracks.add(track);
//        }
//        trackRepository.saveAll(newTracks);
  }

  @Override
  public void deleteAll(List<Track> tracks) {
      trackRepository.deleteAll(tracks);
  }

  @Override
  public Track add(Track track) {
    return trackRepository.save(track);
  }
}
