package com.tarbus.services.impl;

import com.google.common.collect.Lists;
import com.tarbus.models.jpa.CompanyScheduleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.repositories.jpa.CompanyScheduleTemplateRepository;
import com.tarbus.services.CompanyScheduleTemplateService;

import java.util.List;

@Service
public class CompanyScheduleTemplateServiceImpl implements CompanyScheduleTemplateService {
  private final CompanyScheduleTemplateRepository companyScheduleTemplateRepository;

  @Autowired
  public CompanyScheduleTemplateServiceImpl(CompanyScheduleTemplateRepository companyScheduleTemplateRepository) {
    this.companyScheduleTemplateRepository = companyScheduleTemplateRepository;
  }
  @Override
  public CompanyScheduleTemplate getCompanyTemplate(Long companyId) {
    return companyScheduleTemplateRepository.findByIdCompanyId(companyId);
  }

  @Override
  public List<CompanyScheduleTemplate> getAll() {
    return Lists.newArrayList(companyScheduleTemplateRepository.findAll());
  }
}
