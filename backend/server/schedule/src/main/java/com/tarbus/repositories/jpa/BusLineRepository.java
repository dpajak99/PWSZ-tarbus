package com.tarbus.repositories.jpa;

import com.tarbus.models.BusLine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusLineRepository extends CrudRepository<BusLine, Long> {
  @Query("SELECT bl FROM BusLine bl WHERE bl.version.id = ?1 ORDER BY bl.name")
  List<BusLine> getAllByVersionId(Long versionId);
}
