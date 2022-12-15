package com.tarbus.services.impl;

import com.tarbus.models.*;
import com.tarbus.repositories.jpa.VersionRepository;
import com.tarbus.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.models.*;
import com.tarbus.services.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VersionServiceImpl implements VersionService {
  private VersionRepository versionRepository;
  private BusLineService busLineService;
  private RouteService routeService;
  private RouteConnectionService routeConnectionService;
  private DestinationsService destinationsService;
  private TrackService trackService;
  private DepartureService departureService;

  @Autowired
  public void setDepartureService(DepartureService departureService) {
    this.departureService = departureService;
  }

  @Autowired
  public void setTrackService(TrackService trackService) {
    this.trackService = trackService;
  }
  @Autowired
  public void setDestinationsService(DestinationsService destinationsService) {
    this.destinationsService = destinationsService;
  }
  @Autowired
  public void setRouteService(RouteConnectionService routeConnectionService) {
    this.routeConnectionService = routeConnectionService;
  }
  @Autowired
  public void setRouteService(RouteService routeService) {
    this.routeService = routeService;
  }
  @Autowired
  public void setBusLineService(BusLineService busLineService) {
    this.busLineService = busLineService;
  }
  @Autowired
  public VersionServiceImpl(VersionRepository versionRepository) {
    this.versionRepository = versionRepository;
  }


  @Override
  public Iterable<ScheduleVersion> getAll() {
    return StreamSupport.stream(versionRepository.findAll().spliterator(), false)
      .collect(Collectors.toList());
  }

  @Override
  public List<ScheduleVersion> getUsersVersions(User user) {
    return versionRepository.getByCompanies(user.getCompanies());
  }

  @Override
  public List<ScheduleVersion> getAllCompany(Long companyId) {
    return versionRepository.getByCompany(companyId);
  }

  @Override
  public List<ScheduleVersion> getByDateAndCompany(Long companyId, String date) {
    return versionRepository.getByDateAndCompany(companyId, date);
  }

  @Override
  public ScheduleVersion create(ScheduleVersion version) {
    return versionRepository.save(version);
  }

  @Override
  public ScheduleVersion getById(Long companyId) {
    return versionRepository.findById(companyId).get();
  }

  @Override
  public ScheduleVersion updateVersion(ScheduleVersion version) {
    return versionRepository.save(version);
  }

  @Override
  public List<ScheduleVersion> getCompanyActiveVersions(Long companyId) {
    return versionRepository.getCompanyActiveVersions(companyId);
  }

  @Override
  public List<ScheduleVersion> getAllActiveVersions() {
    return versionRepository.getAllActiveVersions();
  }

  @Override
  public void buildNewVersion(Map<String, Object> data) {
    if (data.containsKey("date") && data.containsKey("parent") && data.containsKey("tables")) {
      Long parentId = (long) (int) data.get("parent");
      List<String> tables = (List<String>) data.get("tables");

      ScheduleVersion scheduleVersion = new ScheduleVersion(data);
      ScheduleVersion newVersion = create(scheduleVersion);
      try {
        if (tables.contains("bus_lines")) {
          List<BusLine> oldBusLines = busLineService.getAll(parentId);
          for (BusLine line : oldBusLines) {
            BusLine tmpLine = new BusLine();
            tmpLine.setName(line.getName());
            tmpLine.setVersion(newVersion);

            BusLine newLine = busLineService.add(tmpLine);
            if (tables.contains("routes")) {
              List<Route> oldRoutes = routeService.getFullByLine(line.getId());

              for (Route route : oldRoutes) {
                Route tmpRoute = new Route();
                tmpRoute.setName(route.getName());
                tmpRoute.setDetails(route.getDetails());
                tmpRoute.setDestinationDescription(route.getDestinationDescription());
                tmpRoute.setComments(route.getComments());
                tmpRoute.setBusLine(newLine);

                Route newRoute = routeService.add(tmpRoute);

                if (tables.contains("route_connections")) {
                  List<RouteConnection> oldRouteConnections = routeConnectionService.getByRoute(route.getId());

                  for (RouteConnection connection : oldRouteConnections) {
                    RouteConnection tmpRouteConnection = new RouteConnection();
                    tmpRouteConnection.setIsOptional(connection.getIsOptional());
                    tmpRouteConnection.setRoute(newRoute);
                    tmpRouteConnection.setLp(connection.getLp());
                    tmpRouteConnection.setBusStop(connection.getBusStop());

                    routeConnectionService.add(tmpRouteConnection);
                  }
                }
                if (tables.contains("destinations")) {
                  List<Destination> oldDestinations = destinationsService.getAllByRouteLong(route.getId());

                  for (Destination destination : oldDestinations) {
                    Destination tmpDestination = new Destination();
                    tmpDestination.setBoardName(destination.getBoardName());
                    tmpDestination.setScheduleName(destination.getScheduleName());

                    DestinationId destinationId = new DestinationId();
                    destinationId.setId(newRoute);
                    destinationId.setSymbols(destination.getDestinationId().getSymbols());
                    tmpDestination.setDestinationId(destinationId);

                    destinationsService.add(tmpDestination);
                  }
                }

                if (tables.contains("tracks")) {
                  List<Track> oldTracks = trackService.getByRoute(route.getId());
                  for (Track track : oldTracks) {
                    Track tmpTrack = new Track();
                    tmpTrack.setDayTypes(track.getDayTypes());
                    tmpTrack.setDayString(track.getDayString());
                    tmpTrack.setRoute(newRoute);
                    tmpTrack.setLp(track.getLp());

                    Track newTrack = trackService.add(tmpTrack);

                    if (tables.contains("departures")) {
                      List<Departure> oldDepartures = departureService.getByTrack(track.getId());

                      for (Departure departure : oldDepartures) {
                        Departure tmpDeparture = new Departure();
                        tmpDeparture.setTrack(newTrack);
                        tmpDeparture.setBusStop(departure.getBusStop());
                        tmpDeparture.setSymbols(departure.getSymbols());
                        tmpDeparture.setTime(departure.getTime());
                        tmpDeparture.setTimeInMin(departure.getTimeInMin());

                        departureService.add(tmpDeparture);
                      }
                    }
                  }
                }
              }
            }
          }
        }
        newVersion.setStatus("created");
        updateVersion(newVersion);
      } catch(Exception e ) {
        newVersion.setStatus("error");
        updateVersion(newVersion);
      }
    }
  }


  @Override
  public void deleteVersion(Long versionId) {
    ScheduleVersion version = new ScheduleVersion();
    version.setId(versionId);
    versionRepository.delete(version);
  }

  @Override
  public void deleteVersion(ScheduleVersion version) {
    versionRepository.delete(version);
  }

  @Override
  public void deleteVersions(List<ScheduleVersion> versions) {
    versionRepository.deleteAll(versions);
  }

}
