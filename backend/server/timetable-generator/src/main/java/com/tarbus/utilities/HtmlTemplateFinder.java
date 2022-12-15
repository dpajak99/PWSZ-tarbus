package com.tarbus.utilities;

import com.tarbus.models.TemplateType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class HtmlTemplateFinder {
  private TemplateType templateType;
  private String htmlFilePath;
  private String cssFilePath;
  private String workingDirectory;

  private String htmlContent;
  private String cssContent;


  public void process() {
    htmlContent = _getFile(workingDirectory + "/" + htmlFilePath);
    cssContent =  _getFile(workingDirectory + "/" + cssFilePath);
  }

  private String _getFile(String path) {
    FileScanner fileScanner = new FileScanner();
    System.out.println("path: " + path);
    return new BufferedReader(new InputStreamReader(fileScanner.getFileByPath(path))).lines().collect(Collectors.joining("\n"));
  }



}
