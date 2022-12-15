package com.tarbus.services;

import com.tarbus.models.ScheduleVersion;
import com.tarbus.models.User;

import java.util.List;
import java.util.Map;

public interface VersionService {

  Iterable<ScheduleVersion> getAll();

  List<ScheduleVersion> getUsersVersions(User user);

  List<ScheduleVersion> getAllCompany(Long companyId);

  List<ScheduleVersion> getByDateAndCompany(Long companyId, String date);
  List<ScheduleVersion> getCompanyActiveVersions(Long companyId);
  List<ScheduleVersion> getAllActiveVersions();
  ScheduleVersion create(ScheduleVersion version);
  ScheduleVersion getById(Long companyId);
  ScheduleVersion updateVersion(ScheduleVersion version);

  void buildNewVersion(Map<String, Object> data);

  void deleteVersion(Long versionId);

  void deleteVersion(ScheduleVersion version);

  void deleteVersions(List<ScheduleVersion> versions);
}
