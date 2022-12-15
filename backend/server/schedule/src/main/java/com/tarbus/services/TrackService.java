package com.tarbus.services;

import com.tarbus.models.Track;

import java.util.List;

public interface TrackService {

    Iterable<Track> getAll();

    List<Track> getAll(Long versionId);

    List<Track> getByRoute(Long routeId);
    List<Long> getTracksContainsBusStop(Long busStopId);

    Object getById(String id);

  List<Track> addAll(List<Track> tracks);
    void oldToNew();
    void deleteAll(List<Track> tracks);
    Track add(Track track);
}
