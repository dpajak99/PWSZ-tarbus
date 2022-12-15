package com.tarbus.repositories.jpa;


import com.tarbus.models.jpa.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusLineScheduleTemplateRepository extends CrudRepository<BusLineScheduleTemplate, BusLineScheduleTemplateId> {
  BusLineScheduleTemplate findByIdBusLineId(Long busLineId);
}
