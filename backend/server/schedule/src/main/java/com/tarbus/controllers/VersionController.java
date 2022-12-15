package com.tarbus.controllers;

import com.tarbus.models.ScheduleVersion;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.UserRepository;
import com.tarbus.security.services.UserDetailsImpl;
import com.tarbus.services.VersionService;
import com.tarbus.utilities.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@RequestMapping("/api/schedule/")
public class VersionController {
  @Autowired
  UserRepository userRepository;

  private VersionService versionService;

  @Autowired
  RoleChecker roleChecker;

  @Autowired
  public void setVersionService(VersionService versionService) {
    this.versionService = versionService;
  }

  @GetMapping({"/versions"})
  public ResponseEntity<Object> getVersions() {
    User user = roleChecker.getUser();
    List<ScheduleVersion> versions = versionService.getUsersVersions(user);
    List<Object> result = new ArrayList<>();
    for (ScheduleVersion version : versions) {
      result.add(version.toJson());
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping({"/versions/createNew"})
  public ResponseEntity<String> setNewVersion(@RequestBody() Map<String, Object> data) {
    versionService.buildNewVersion(data);
    return new ResponseEntity<>("Ok", HttpStatus.OK);
  }

  @GetMapping({"/versions/active"})
  public ResponseEntity<List<ScheduleVersion>> getCompanyActiveVersions(@RequestParam() Long companyId) {
    List<ScheduleVersion> result = versionService.getCompanyActiveVersions(companyId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping({"/versions/{id}"})
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> deleteVersion(@PathVariable("id") Long versionId) {
    User user = roleChecker.getUser();
    List<ScheduleVersion> versions = versionService.getUsersVersions(user);

    if ((versions.stream().map(ScheduleVersion::getId)).collect(Collectors.toList()).contains(versionId)) {
      versionService.deleteVersion(versionId);
      return new ResponseEntity<>("OK",HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Login to access", HttpStatus.UNAUTHORIZED);
    }
  }


  @PostMapping({"/versions"})
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> updateVersion(@RequestBody ScheduleVersion version) {
      ScheduleVersion newVersion = versionService.updateVersion(version);
      return new ResponseEntity<>(newVersion, HttpStatus.OK);
  }
}
