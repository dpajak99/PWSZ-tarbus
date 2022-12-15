package com.tarbus.services.impl;

import com.tarbus.models.BusStop;
import com.tarbus.models.BusStopConnection;
import com.tarbus.models.BusStopConnectionId;
import com.tarbus.models.Track;
import com.tarbus.repositories.jpa.BusStopConnectionRepository;
import com.tarbus.services.BusStopConnectionService;
import com.tarbus.services.BusStopService;
import com.tarbus.services.DepartureService;
import com.tarbus.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusStopConnectionServiceImpl implements BusStopConnectionService {

  private BusStopConnectionRepository busStopConnectionRepository;
  private TrackService trackService;
  private DepartureService departureService;
  private BusStopService busStopService;

  @Autowired
  public BusStopConnectionServiceImpl(BusStopConnectionRepository busStopConnectionRepository) {
    this.busStopConnectionRepository = busStopConnectionRepository;
  }

  @Autowired
  public void setDepartureService(DepartureService departureService) {
    this.departureService = departureService;
  }

  @Autowired
  public void setBusStopService(BusStopService busStopService) {
    this.busStopService = busStopService;
  }

  @Autowired
  public void setTrackService(TrackService trackService) {
    this.trackService = trackService;
  }

  @Override
  public void updateConnections(List<BusStopConnection> busConnections) {
    busStopConnectionRepository.saveAll(busConnections);
  }

  @Override
  public Object generateGraphFiles() {
    Iterable<BusStopConnection> allConnections = busStopConnectionRepository.findAll();
    List<BusStop> allBusStops = busStopService.getAll(null);

    StringBuilder busxy = new StringBuilder();
    StringBuilder busPair = new StringBuilder();

    for (BusStopConnection connection : allConnections) {
      Long fromId = connection.getBusStopConnectionId().getFromBusStopId().getId();
      Long toId = connection.getBusStopConnectionId().getToBusStopId().getId();
      busPair
        .append(fromId)
        .append(",")
        .append(toId)
        .append(",")
        .append(100)
        .append("\n");
    }

    for (BusStop stop : allBusStops) {
      busxy
        .append(stop.getId())
        .append(",")
        .append(Math.round((stop.getLat() * 100000 - 4989641)) / 90)
        .append(",")
        .append(Math.round((stop.getLng() * 100000 - 2088698)) / 90)
        .append("\n");
    }

    File fileBusxy = new File("busxy.txt");
    File fileBusPair = new File("busPair.txt");
    try {
      OutputStream osBusxy = new FileOutputStream(fileBusxy);
      osBusxy.write(busxy.toString().getBytes(StandardCharsets.UTF_8));
      osBusxy.close();
      OutputStream osBusPair = new FileOutputStream(fileBusPair);
      osBusPair.write(busPair.toString().getBytes(StandardCharsets.UTF_8));
      osBusPair.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "Ok";
  }

  @Override
  public List<BusStopConnection> getEmptyConnections() {
    return busStopConnectionRepository.findEmptyConnections();
  }

  @Override
  public List<BusStopConnection> getAll() {
    return (List<BusStopConnection>) busStopConnectionRepository.findAll();
  }

  @Override
  public List<BusStopConnection> getAllTo(Long to) {
    return busStopConnectionRepository.findAllTo(to);
  }

  @Override
  public List<BusStopConnection> getAllFrom(Long from) {
    return busStopConnectionRepository.findAllFrom(from);
  }

  @Override
  public BusStopConnection getSingleConnection(Long from, Long to) {
    return busStopConnectionRepository.findSingleConnection(from, to);
  }

  @Override
  public BusStopConnection save(BusStopConnection connection) {
    return busStopConnectionRepository.save(connection);
  }

  @Override
  public void deleteAll(List<BusStopConnection> connections) {
    busStopConnectionRepository.deleteAll(connections);
  }

  @Override
  public void delete(BusStopConnection connection) {
    busStopConnectionRepository.delete(connection);
  }

  @Override
  public int fetchEmptyConnections() {
    Iterable<Track> tracks = trackService.getAll();
    List<BusStopConnection> emptyConnections = findEmptyConnections(tracks);
    updateConnections(emptyConnections);
    return emptyConnections.size();
  }

  boolean busStopPairContainsPair(List<long[]> list, long[] pair) {
    for( long[] item : list ) {
      if( item[0] == pair[0] && item[1] == pair[1]) {
        return true;
      }
    }
    return false;
  }

  List<BusStopConnection> findEmptyConnections(Iterable<Track> tracks) {
    List<BusStopConnection> result = new ArrayList<>();
    List<long[]> notConnectedBusStops = new ArrayList<>();
    /// For all tracks
    for (Track track : tracks) {
      // Find all bus stops
      List<BusStop> busStops = departureService.getAllBusStopsOnTrack(track.getId());
      // Get bus stop connections
      for (int i = 0; i < busStops.size() - 1; i++) {

        BusStop fromBusStop = busStops.get(i);
        BusStop toBusStop = busStops.get(i + 1);

        long[] stopPair = {fromBusStop.getId(), toBusStop.getId()};

        // Check if arleady checked this connection
        if (!busStopPairContainsPair(notConnectedBusStops, stopPair)) {
          // Add to checked connections
          notConnectedBusStops.add(stopPair);
          // Check if connection is set
          Long connectionsCount = busStopConnectionRepository.connectionExist(stopPair[0], stopPair[1]);
          if (connectionsCount == 0) {
            // Add to not existed connections array
            BusStopConnection notExistedConnection = new BusStopConnection();
            notExistedConnection.setBusStopConnectionId(new BusStopConnectionId(fromBusStop, toBusStop));
            result.add(notExistedConnection);
          }
        }
      }
    }
    return result;
  }
}
