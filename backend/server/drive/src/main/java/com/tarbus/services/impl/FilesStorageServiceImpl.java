package com.tarbus.services.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import com.tarbus.AppConfig;
import com.tarbus.models.FileObject;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.FilesRepository;
import com.tarbus.services.FilesStorageService;
import com.tarbus.utilities.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  @Autowired
  AppConfig appConfig;

  @Autowired
  FilesRepository filesRepository;

  @Autowired
  RoleChecker roleChecker;

  private final Path root = Paths.get("/home/dpajak99/Storage/GitHub/tarBUS/tarBUS-server/static/uploads");

  @Override
  public FileObject save(MultipartFile file) {
    try {
      String fileName = file.getOriginalFilename();
      System.out.println(fileName);
      String extension = fileName.split("\\.")[fileName.split("\\.").length-1];
      User user = roleChecker.getUser();
      FileObject fileObject = new FileObject(fileName, extension, user);
      fileObject = filesRepository.save(fileObject);
      Files.copy(file.getInputStream(), this.root.resolve(fileObject.getId() + "." + fileObject.getExtension()));
      return fileObject;
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String fileId) {
    try {
      Optional<FileObject> fileObject = filesRepository.findById(fileId);
      if( fileObject.isPresent() ) {
        Path file = root.resolve(fileObject.get().getParsedName());
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
          return resource;
        } else {
          return null;
        }
      } else {
        throw new RuntimeException("File not exists in database!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }
}