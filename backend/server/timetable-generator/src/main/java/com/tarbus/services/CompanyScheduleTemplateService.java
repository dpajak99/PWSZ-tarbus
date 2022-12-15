package com.tarbus.services;

import com.tarbus.models.jpa.CompanyScheduleTemplate;

import java.util.List;

public interface CompanyScheduleTemplateService {
  CompanyScheduleTemplate getCompanyTemplate(Long companyId );
  List<CompanyScheduleTemplate> getAll( );
}
