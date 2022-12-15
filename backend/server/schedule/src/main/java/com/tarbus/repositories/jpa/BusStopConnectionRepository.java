package com.tarbus.repositories.jpa;

import com.tarbus.models.BusStopConnection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BusStopConnectionRepository extends CrudRepository<BusStopConnection, Long> {
  @Query("SELECT COUNT(c) FROM BusStopConnection c WHERE c.busStopConnectionId.fromBusStopId.id = ?1 AND c.busStopConnectionId.toBusStopId.id = ?2")
  Long connectionExist(Long from, Long to);

  @Query("SELECT c FROM BusStopConnection c WHERE c.busStopConnectionId.fromBusStopId.id = ?1 AND c.busStopConnectionId.toBusStopId.id = ?2")
  BusStopConnection findSingleConnection(Long from, Long to);

  @Query("SELECT c FROM BusStopConnection c WHERE c.coordsList = '' OR c.coordsList IS NULL")
  List<BusStopConnection> findEmptyConnections();

  @Query("SELECT c FROM BusStopConnection c WHERE c.busStopConnectionId.toBusStopId = ?1")
  List<BusStopConnection> findAllTo(Long to);

  @Query("SELECT c FROM BusStopConnection c WHERE c.busStopConnectionId.fromBusStopId = ?1")
  List<BusStopConnection> findAllFrom(Long from);
}


