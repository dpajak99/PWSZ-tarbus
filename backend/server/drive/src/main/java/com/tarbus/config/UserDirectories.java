package com.tarbus.config;

import com.tarbus.AppConfig;
import com.tarbus.models.RemoteFile;
import com.tarbus.models.User;
import com.tarbus.utilities.RoleChecker;
import com.tarbus.utils.FileSizeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

@Component
public class UserDirectories {
  @Autowired
  private AppConfig appConfig;

  @Autowired
  private RoleChecker roleChecker;

  public String getUserCompanyDrivePath() {
    User user = roleChecker.getUser();
    if( user.getCompanies().isEmpty() ) {
      return getUserDrivePath();
    }
    return getCompanyDrivePath(new ArrayList<>(user.getCompanies()).get(0).getId());
  }

  public String getCompanyDrivePath(Long companyId) {
    return appConfig.STORAGE_ABSOLUTE_PATH + "/company/" + companyId;
  }

  public String getUserDrivePath() {
    User user = roleChecker.getUser();
    return appConfig.STORAGE_ABSOLUTE_PATH + "/user/" + user.getId();
  }

  public String serverPathToAppUrl(String serverPath) {
    return serverPath.replaceFirst(appConfig.STORAGE_ABSOLUTE_PATH, appConfig.appURL + "/static");
  }

  public RemoteFile serverPathToRemoteFile(String serverPath) {
      File file = new File(serverPath);
      RemoteFile remoteFile = new RemoteFile();
      remoteFile.setUrl(serverPathToAppUrl(serverPath));
      remoteFile.setSize(FileSizeUtils.humanReadableByteCountSI(file.length()));
      remoteFile.setName(file.getName());
      remoteFile.setLastModified(new Date(file.lastModified()).toString());
      System.out.println(file);
      System.out.println(remoteFile);
      return remoteFile;
  }
}
