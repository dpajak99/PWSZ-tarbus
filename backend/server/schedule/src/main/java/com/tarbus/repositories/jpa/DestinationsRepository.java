package com.tarbus.repositories.jpa;

import com.tarbus.models.Destination;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DestinationsRepository extends CrudRepository<Destination, String> {

  @Query("SELECT d FROM Destination as d WHERE d.destinationId.route.busLine.version.id = ?1")
  List<Destination> getAll(Long versionId);

  @Query("SELECT d FROM Destination as d WHERE d.destinationId.route.id = ?1 AND NOT d.destinationId.symbols = '-' AND (Length(d.destinationId.symbols) = 1 OR d.destinationId.symbols = 'Ł')")
  List<Destination> findByRouteShort(Long routeId);

  @Query("SELECT d FROM Destination as d WHERE d.destinationId.route.id = ?1")
  List<Destination> findByRouteLong(Long routeId);

  @Query("SELECT d FROM Destination as d WHERE d.destinationId.route.id = ?1 AND d.destinationId.symbols = ?2")
  List<Destination> findByRouteAndSymbol(Long routeId, String symbol);

  @Query(value = "SELECT DISTINCT direction_board_name FROM destinations AS ds JOIN departure AS d ON d.symbols = ds.symbol JOIN track AS t ON t.id = d.track_id WHERE t.route_id = ds.route_id AND d.bus_stop_id = ?1 ORDER BY CHAR_LENGTH(ds.direction_board_name)", nativeQuery = true)
  List<String> getShortestDestinationFromBusStop(Long busStopId);
}
