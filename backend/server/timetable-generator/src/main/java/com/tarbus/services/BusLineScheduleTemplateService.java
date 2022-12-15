package com.tarbus.services;

import com.tarbus.models.jpa.BusLineScheduleTemplate;
import com.tarbus.models.jpa.CompanyScheduleTemplate;

import java.util.List;

public interface BusLineScheduleTemplateService {
  BusLineScheduleTemplate getBusLineTemplate( Long busLineId );
  List<BusLineScheduleTemplate> getAll( );
}
