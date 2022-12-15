package com.tarbus.repositories.jpa;

import com.tarbus.models.RouteConnection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface RouteConnectionRepository extends CrudRepository<RouteConnection, Long> {

    @Query("SELECT rc FROM RouteConnection as rc WHERE rc.route.busLine.version.id = ?1")
    List<RouteConnection> getAll(Long versionId);

    @Query("SELECT rc FROM RouteConnection rc WHERE rc.route.id = ?1 ORDER BY rc.lp")
    List<RouteConnection> findByRoute(Long routeId);
}
