package com.tarbus.models;

import java.util.HashMap;
import java.util.Map;

public class FileInfo {
  private final String id;
  private final String name;

  public FileInfo(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public FileInfo(FileObject uploadedFile) {
    this.id = uploadedFile.getId();
    this.name = uploadedFile.getName();
  }

  public Map<String, Object> toJson() {
    Map<String, Object> response = new HashMap<>();
    response.put("id", id);
    response.put("name", name);
    return response;
  }
}