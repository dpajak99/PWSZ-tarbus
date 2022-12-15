package com.tarbus.utilities;

import com.lowagie.text.pdf.BaseFont;
import com.tarbus.models.MultiPdfPage;
import com.tarbus.models.PdfDocument;
import com.tarbus.models.PdfPage;
import com.tarbus.models.SinglePdfPage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

  private final TemplateEngine templateEngine = new TemplateEngine();
  String fontsPath;

  public PdfGenerator(String fontsPath) {
    this.fontsPath = fontsPath;
    StringTemplateResolver templateResolver = new StringTemplateResolver();
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setTemplateMode(TemplateMode.HTML);

    templateEngine.setTemplateResolver(templateResolver);
  }

  /**
   * Generates PDF from HTML
   * Can save it loccaly on server
   *
   * @return PDF bute array
   * @throws IOException - when something gone wrong
   */
  public String generateTemplate(PdfDocument pdfDocument, SinglePdfPage titlePage) throws IOException {
    String parsedTitlePage = parseTitlePage(titlePage);
    List<String> parsedPages = parsePages(pdfDocument.getPages());
    try {
      generateSingleFileIfRequired(parsedPages, pdfDocument.getPages());
    } catch (Exception e) {
      e.printStackTrace();
    }
    ByteArrayOutputStream os = generatePdf(parsedPages, parsedTitlePage, pdfDocument.getTitle());
    String finalPath = pdfDocument.getWorkingDirectory() + pdfDocument.getFileName() + ".pdf";
    File file = new File(pdfDocument.getWorkingDirectory());
    file.mkdirs();
    file = new File(finalPath);
    writeFile(file, os);

    return finalPath;
  }

  /**
   * Generates a single file if so set in PdfPage parameters
   *
   * @param parsedPages - List of PdfPages parsed to HTML
   * @param pages       - list of single PdfPages
   * @throws IOException - when something gone wrong
   */
  public void generateSingleFileIfRequired(List<String> parsedPages, List<PdfPage> pages) throws IOException {
    for (int i = 0; i < parsedPages.size(); i++) {
      PdfPage page = pages.get(i);
      if (page.isSaveSinglePage()) {
        System.out.println("Saving single page " + page.getFileName());
        ByteArrayOutputStream singleFileData = generatePdf(parsedPages.get(i), page.getFileName());
        Files.createDirectories(Paths.get(page.getWorkingDirectory()));
        File file = new File(page.getWorkingDirectory() + page.getFileName() + ".pdf");
        writeFile(file, singleFileData);
      }
    }
  }

  /**
   * Saves file
   *
   * @param file - file to save
   * @param data - file content
   */
  private void writeFile(File file, ByteArrayOutputStream data) {
    try {
      OutputStream os = new FileOutputStream(file);
      if (data != null) {
        os.write(data.toByteArray());
      }
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates final version of pdf (single page)
   * When you want to save only one page
   *
   * @param parsedPage    - Single page of pdf
   * @param documentTitle - document title
   * @return - pdf in byte array
   * @throws IOException - when something gone wrong
   */
  private ByteArrayOutputStream generatePdf(String parsedPage, String documentTitle) throws IOException {
    List<String> parsedPages = new ArrayList<>();
    parsedPages.add(parsedPage);
    return generatePdf(parsedPages, null, documentTitle);
  }

  /**
   * Generates final version of pdf
   *
   * @param parsedPages   - List of PdfPages parsed to HTML
   * @param documentTitle - document title
   * @return - pdf in byte array
   * @throws IOException - when something gone wrong
   */
  private ByteArrayOutputStream generatePdf(List<String> parsedPages, String titlePage, String documentTitle) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    ITextRenderer iTextRenderer = new ITextRenderer(300, 230);

    File f = new File(fontsPath);
    if (f.isDirectory()) {
      File[] files = f.listFiles((dir, name) -> {
        String lower = name.toLowerCase();
        return lower.endsWith(".otf") || lower.endsWith(".ttf");
      });

      assert files != null;
      for (File file : files) {
        iTextRenderer.getFontResolver().addFont(file.getAbsolutePath(), BaseFont.IDENTITY_H,
          BaseFont.NOT_EMBEDDED);
      }
    }

    if (titlePage != null) {
      iTextRenderer.setDocumentFromString(titlePage);
    } else {
      iTextRenderer.setDocumentFromString(parsedPages.get(0));
    }
    iTextRenderer.layout();
    iTextRenderer.createPDF(os, false);
    for (int i = 0; i < parsedPages.size(); i++) {
      if( i == 0 && titlePage == null) {
        continue;
      }
      String html = parsedPages.get(i);
      iTextRenderer.setDocumentFromString(html);
      iTextRenderer.layout();
      iTextRenderer.writeNextDocument();
    }
    iTextRenderer.getOutputDevice().setMetadata("title", documentTitle);
    iTextRenderer.finishPDF();
    os.close();
    return os;
  }

  private String parseTitlePage(SinglePdfPage singlePdfPage) {
    Map<String, Object> variables = singlePdfPage.getVariables();
    variables.put("style", singlePdfPage.getHtmlTemplateFinder().getCssContent());
    return getHtmlString(variables, singlePdfPage.getHtmlTemplateFinder().getHtmlContent());
  }

  /**
   * Builds HTML from PdfPage data
   *
   * @param pages - list of PdfPages
   * @return HTML List
   */
  private List<String> parsePages(List<PdfPage> pages) {
    List<String> parsedPages = new ArrayList<>();
    for (PdfPage page : pages) {
      StringBuilder result = new StringBuilder();
      result.append("<html><head>");
      result.append("<style type=\"text/css\">");
      result.append("\n");
      List<String> htmlPages = new ArrayList<>();
      if (page instanceof SinglePdfPage) {
        SinglePdfPage singlePdfPage = (SinglePdfPage) page;
        Map<String, Object> variables = singlePdfPage.getVariables();
        result.append(singlePdfPage.getHtmlTemplateFinder().getCssContent()).append("\n");
        htmlPages.add(getHtmlString(variables, singlePdfPage.getHtmlTemplateFinder().getHtmlContent()));
      } else if (page instanceof MultiPdfPage) {
        MultiPdfPage multiPdfPage = (MultiPdfPage) page;
        for (SinglePdfPage singlePdfPage : multiPdfPage.getPages()) {
          Map<String, Object> variables = singlePdfPage.getVariables();
          result.append(singlePdfPage.getHtmlTemplateFinder().getCssContent()).append("\n");
          String htmlString = getHtmlString(variables, singlePdfPage.getHtmlTemplateFinder().getHtmlContent());
          htmlPages.add(htmlString);
        }
      } else {
        throw new IllegalArgumentException("Unknown page type");
      }
      result.append("</style></head><body>");
      result.append("\n");
      result.append(String.join("", htmlPages));
//      result.append(htmlPages.get(0));
      result.append("</body></html>");
      parsedPages.add(result.toString());
    }
    return parsedPages;
  }

  /**
   * Load html file and replace variables
   *
   * @param variables  - variables to replace
   * @param htmlString - html template
   * @return Parsed HTML
   */
  private String getHtmlString(Map<String, Object> variables, String htmlString) {
    try {
      final Context ctx = new Context();
      ctx.setVariables(variables);
      return templateEngine.process(htmlString, ctx);
    } catch (Exception e) {
      return null;
    }
  }
}