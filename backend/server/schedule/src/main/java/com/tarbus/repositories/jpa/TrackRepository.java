package com.tarbus.repositories.jpa;

import com.tarbus.models.Track;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TrackRepository extends CrudRepository<Track, String> {
    @Query("SELECT t FROM Track AS t WHERE t.route.id = ?1 ORDER BY t.lp")
    List<Track> getByRoute(Long routeId);

    @Query("SELECT t FROM Track AS t WHERE t.route.busLine.version.id = ?1")
    List<Track> getAll(Long versionId);

    @Query("SELECT MAX(t.lp) FROM Track AS t WHERE t.route.id = ?1")
    Long getMaxLp(Long routeId);
}
