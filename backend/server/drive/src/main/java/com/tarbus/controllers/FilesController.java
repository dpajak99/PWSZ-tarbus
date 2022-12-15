package com.tarbus.controllers;


import java.util.List;
import java.util.stream.Collectors;

import com.tarbus.AppConfig;
import com.tarbus.models.FileInfo;
import com.tarbus.models.FileObject;
import com.tarbus.models.ResponseMessage;
import com.tarbus.services.DriveService;
import com.tarbus.services.FilesStorageService;
import com.tarbus.utilities.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@RestController
@CrossOrigin
@RequestMapping("/api/drive/")
public class FilesController {
  @Autowired
  AppConfig appConfig;

  private FilesStorageService storageService;

  @Autowired
  public void setStorageService(FilesStorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      FileObject uploadedFile = storageService.save(file);
      FileInfo fileInfo = new FileInfo(uploadedFile);
      return new ResponseEntity<>(fileInfo.toJson(), HttpStatus.OK);
    } catch (Exception e) {
      String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/files")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = appConfig.appURL + "/static/uploads/filename";

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    System.out.println("FILE NAME: " + filename);
    Resource file = storageService.load(filename);
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}