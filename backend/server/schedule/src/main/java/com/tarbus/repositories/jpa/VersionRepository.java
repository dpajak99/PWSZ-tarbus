package com.tarbus.repositories.jpa;

import com.tarbus.models.Company;
import com.tarbus.models.ScheduleVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface VersionRepository extends CrudRepository<ScheduleVersion, Long> {
  @Query("SELECT v FROM ScheduleVersion as v WHERE v.company.id = ?1")
  List<ScheduleVersion> getByCompany(Long versionId);

  @Query("SELECT v as companyId  FROM ScheduleVersion as v WHERE v.company IN (?1)")
  List<ScheduleVersion> getByCompanies(Set<Company> companies);

  @Query("SELECT v FROM ScheduleVersion as v WHERE v.company.id = ?1 AND v.date = ?2")
  List<ScheduleVersion> getByDateAndCompany(Long companyId, String date);

  @Query("SELECT v FROM ScheduleVersion as v WHERE v.company.id = ?1 AND v.activeStatus IN ('active', 'visible')")
  List<ScheduleVersion> getCompanyActiveVersions(Long companyId);

  @Query("SELECT v FROM ScheduleVersion as v WHERE v.activeStatus IN ('active')")
  List<ScheduleVersion> getAllActiveVersions();
}
