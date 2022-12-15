package com.tarbus.models;

import com.tarbus.utilities.HtmlTemplateFinder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SinglePdfPage extends PdfPage {
  private HtmlTemplateFinder htmlTemplateFinder;

  /**
   * Map of variables to replace
   */
  private Map<String, Object> variables;



  public SinglePdfPage(HtmlTemplateFinder htmlTemplateFinder, Map<String, Object> variables) {
    this.htmlTemplateFinder = htmlTemplateFinder;
    this.variables = variables;
  }


}
