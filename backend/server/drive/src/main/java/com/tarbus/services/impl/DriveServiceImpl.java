package com.tarbus.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.AppConfig;
import com.tarbus.models.Company;
import com.tarbus.models.User;
import com.tarbus.services.DriveService;
import com.tarbus.utilities.RoleChecker;

import java.io.File;
import java.util.*;

@Service
public class DriveServiceImpl implements DriveService {
  @Autowired
  AppConfig appConfig;

  @Autowired
  RoleChecker roleChecker;

  @Override
  public List<Map<String, Object>> getFilesInRootDirectory() {
    User user = roleChecker.getUser();
    List<Map<String, Object>> result = new ArrayList<>();
    if( roleChecker.isAdmin() ) {
      result = getFilesInDirectory("");
    } else {
      for (Company company : user.getCompanies()) {
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("name", company.getName());
        fileData.put("path", "company/" + company.getId());
        fileData.put("isDirectory", true);
        fileData.put("isFile", false);
        fileData.put("lastModified", 0);
        result.add(fileData);
      }
    }
    return result;
  }

  public List<Map<String, Object>> getFilesInDirectory(String directory) {
    List<Map<String, Object>> parsedFiles = new ArrayList<>();
    try {
      final File folder = new File(appConfig.STORAGE_ABSOLUTE_PATH + "/" + directory);
      List<File> files = new ArrayList<>(Arrays.asList(folder.listFiles()));

      for (File file : files) {
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("name", file.getName());
        fileData.put("path", file.getName());
        fileData.put("isDirectory", file.isDirectory());
        fileData.put("isFile", file.isFile());
        fileData.put("lastModified", file.lastModified());
        parsedFiles.add(fileData);
      }

    } catch (Exception e) {
    }
    return parsedFiles;
  }


}
