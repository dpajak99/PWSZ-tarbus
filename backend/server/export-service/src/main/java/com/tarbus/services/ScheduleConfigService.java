package com.tarbus.services;

import java.util.Map;

public interface ScheduleConfigService {
  Map<String, Object> generateDatabaseInfo() throws RuntimeException;
}
