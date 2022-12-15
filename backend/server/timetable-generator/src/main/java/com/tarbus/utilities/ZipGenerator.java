package com.tarbus.utilities;

import com.tarbus.models.LocalFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipGenerator {
  static public ByteArrayOutputStream makeZip(List<LocalFile> filenames) throws IOException {
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteOutputStream);

    for (LocalFile localFile : filenames) {
      File file = new File(localFile.getPath());
      zipOutputStream.putNextEntry(new ZipEntry( localFile.getFileName()));
      FileInputStream fileInputStream = new FileInputStream(file);
      IOUtils.copy(fileInputStream, zipOutputStream);
      fileInputStream.close();
      zipOutputStream.closeEntry();
    }
    zipOutputStream.close();
    return byteOutputStream;
  }
}
