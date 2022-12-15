package com.tarbus.controllers;

import com.tarbus.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.tarbus.models.Company;
import com.tarbus.models.ERole;
import com.tarbus.models.User;
import com.tarbus.services.DriveService;
import com.tarbus.utilities.RoleChecker;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/drive/")
public class DriveController {

  private DriveService driveService;

  @Autowired
  RoleChecker roleChecker;

  @Autowired
  public void setDriveService(DriveService driveService) {
    this.driveService = driveService;
  }


  @PreAuthorize("hasRole('USER')")
  @GetMapping({"/getFiles"})
  public ResponseEntity<?> getFiles() {
    List<Map<String, Object>> result = driveService.getFilesInRootDirectory();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping({"/getFiles/**"})
  public ResponseEntity<?> getFiles(HttpServletRequest request) {
    String requestURL = request.getRequestURL().toString();
    String moduleName = requestURL.split("/getFiles/")[1];
    if( !roleChecker.isAdmin() && !moduleName.contains("company")) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    List<Map<String, Object>> result = driveService.getFilesInDirectory(moduleName);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
