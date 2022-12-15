package com.tarbus.services;

public interface ExportService {
  String exportByVersion(Long versionId);
  String exportAll();
}
