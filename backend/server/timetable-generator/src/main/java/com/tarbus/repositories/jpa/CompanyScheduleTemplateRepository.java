package com.tarbus.repositories.jpa;


import com.tarbus.models.jpa.CompanyScheduleTemplate;
import com.tarbus.models.jpa.CompanyScheduleTemplateId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyScheduleTemplateRepository extends CrudRepository<CompanyScheduleTemplate, CompanyScheduleTemplateId> {
  CompanyScheduleTemplate findByIdCompanyId(Long companyId);
}
