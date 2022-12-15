package com.tarbus.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.tarbus.models.FileObject;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

  FileObject save(MultipartFile file);

  Resource load(String fileId);

  Stream<Path> loadAll();
}