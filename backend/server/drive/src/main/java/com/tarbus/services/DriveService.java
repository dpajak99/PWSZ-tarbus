package com.tarbus.services;

import com.tarbus.models.Company;
import com.tarbus.models.User;

import java.util.List;
import java.util.Map;

public interface DriveService {
    List<Map<String, Object>> getFilesInRootDirectory();

    List<Map<String, Object>> getFilesInDirectory(String directory);
}
