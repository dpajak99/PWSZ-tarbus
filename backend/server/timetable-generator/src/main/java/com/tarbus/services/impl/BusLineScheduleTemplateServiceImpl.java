package com.tarbus.services.impl;

import com.google.common.collect.Lists;
import com.tarbus.models.jpa.BusLineScheduleTemplate;
import com.tarbus.models.jpa.CompanyScheduleTemplate;
import com.tarbus.models.jpa.ScheduleTemplate;
import com.tarbus.repositories.jpa.BusLineScheduleTemplateRepository;
import com.tarbus.repositories.jpa.CompanyScheduleTemplateRepository;
import com.tarbus.repositories.jpa.ScheduleTemplateRepository;
import com.tarbus.services.BusLineScheduleTemplateService;
import com.tarbus.services.CompanyScheduleTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusLineScheduleTemplateServiceImpl implements BusLineScheduleTemplateService {
  private final BusLineScheduleTemplateRepository busLineScheduleTemplateRepository;

  @Autowired
  public BusLineScheduleTemplateServiceImpl(BusLineScheduleTemplateRepository busLineScheduleTemplateRepository) {
    this.busLineScheduleTemplateRepository = busLineScheduleTemplateRepository;
  }

  @Override
  public BusLineScheduleTemplate getBusLineTemplate(Long busLineId) {
    return busLineScheduleTemplateRepository.findByIdBusLineId(busLineId);
  }

  @Override
  public List<BusLineScheduleTemplate> getAll() {
    return Lists.newArrayList(busLineScheduleTemplateRepository.findAll());
  }
}
