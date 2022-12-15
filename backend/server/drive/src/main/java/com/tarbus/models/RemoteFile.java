package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RemoteFile {
  private String name;
  private String url;
  private String size;
  private String lastModified;

  @Override
  public String toString() {
    return "RemoteFile{" +
      "name='" + name + '\'' +
      ", url='" + url + '\'' +
      ", size='" + size + '\'' +
      ", lastModified='" + lastModified + '\'' +
      '}';
  }
}
