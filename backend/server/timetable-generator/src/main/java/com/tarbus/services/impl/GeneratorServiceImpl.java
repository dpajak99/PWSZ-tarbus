package com.tarbus.services.impl;

import com.tarbus.AppConfig;
import com.tarbus.config.UserDirectories;
import com.tarbus.models.*;
import com.tarbus.models.jpa.CompanyScheduleTemplate;
import com.tarbus.models.jpa.ScheduleTemplate;
import com.tarbus.models.schedule.builder.HtmlRoutesScheduleBuilder;
import com.tarbus.models.schedule.builder.HtmlStopsScheduleBuilder;
import com.tarbus.repositories.jpa.ScheduleTemplateRepository;
import com.tarbus.services.*;
import com.tarbus.utilities.HtmlTemplateFinder;
import com.tarbus.utilities.PdfGenerator;
import com.tarbus.utilities.ZipGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class GeneratorServiceImpl implements GeneratorService {
  private final Logger logger = LoggerFactory.getLogger(UserDetails.class);

  private final BusStopService busStopService;
  private final DepartureService departureService;
  private final RouteConnectionService routeConnectionService;
  private final RouteService routeService;
  private final CompanyScheduleTemplateService companyScheduleTemplateService;
  private final ScheduleTemplateRepository scheduleTemplateRepository;
  private final CompanyService companyService;
  private final AppConfig appConfig;
  private final UserDirectories userDirectories;

  private final HtmlStopsScheduleBuilder htmlStopsScheduleBuilder;
  private final HtmlRoutesScheduleBuilder htmlRoutesScheduleBuilder;

  public GeneratorServiceImpl(BusStopService busStopService, DepartureService departureService, RouteConnectionService routeConnectionService, RouteService routeService, CompanyScheduleTemplateService companyScheduleTemplateService, ScheduleTemplateRepository scheduleTemplateRepository, CompanyService companyService, AppConfig appConfig, UserDirectories userDirectories, HtmlStopsScheduleBuilder htmlStopsScheduleBuilder, HtmlRoutesScheduleBuilder htmlRoutesScheduleBuilder) {
    this.busStopService = busStopService;
    this.departureService = departureService;
    this.routeConnectionService = routeConnectionService;
    this.routeService = routeService;
    this.companyScheduleTemplateService = companyScheduleTemplateService;
    this.scheduleTemplateRepository = scheduleTemplateRepository;
    this.companyService = companyService;
    this.appConfig = appConfig;
    this.userDirectories = userDirectories;
    this.htmlStopsScheduleBuilder = htmlStopsScheduleBuilder;
    this.htmlRoutesScheduleBuilder = htmlRoutesScheduleBuilder;
  }


  @Override
  public Object generateSchedule(TemplateSettings templateSettings) {
    List<LocalFile> pdfFileResponse = _createScheduleTables(templateSettings);
    if (templateSettings.isWantsZip()) {
      return _saveSchedulesAsZip(pdfFileResponse, templateSettings.getCompanyId());
    } else {
      List<RemoteFile> remoteFiles = new ArrayList<>();
      for (LocalFile localFile : pdfFileResponse) {
        remoteFiles.add(userDirectories.serverPathToRemoteFile(localFile.getPath()));
      }
      return remoteFiles;
    }
  }

  @Override
  public List<ScheduleTemplate> getAvailableTemplates() {
    return scheduleTemplateRepository.getAvailableTemplates();
  }

  private RemoteFile _saveSchedulesAsZip(List<LocalFile> pdfFileResponse, long companyId) {
    try {
      String fileName = "schedule-" + System.currentTimeMillis();
      ByteArrayOutputStream zipFile = ZipGenerator.makeZip(pdfFileResponse);
      String savePath = userDirectories.getCompanyDrivePath(companyId) + "/schedule/archives/" + fileName + ".zip";
      try (OutputStream outputStream = new FileOutputStream(savePath)) {
        zipFile.writeTo(outputStream);
      }
      return userDirectories.serverPathToRemoteFile(savePath);
    } catch (Exception e) {
      System.out.println("Cannot make zip from pdf: " + e);
      logger.error("Cannot make zip from pdf", e);
    }
    return null;
  }

  private List<LocalFile> _createScheduleTables(TemplateSettings templateSettings) {
    Company company = companyService.getCompanyById(templateSettings.getCompanyId());
    CompanyScheduleTemplate companyScheduleTemplateObject = companyScheduleTemplateService.getCompanyTemplate(templateSettings.getCompanyId());
    TemplateType mainTemplateType = companyScheduleTemplateObject.getId().getTemplate().getType();

    List<PdfDocument> documents;
    if (mainTemplateType == TemplateType.by_stop) {
      System.out.println("Should generate by stop");
      documents = htmlStopsScheduleBuilder.generateScheduleTables(company, templateSettings);
    } else {
      System.out.println("Should generate by route");
      documents = htmlRoutesScheduleBuilder.generateScheduleTables(company, templateSettings);
    }

    String fontPath = appConfig.STORAGE_ABSOLUTE_PATH + "/templates/fonts";
    PdfGenerator pdfGenerator = new PdfGenerator(fontPath);

    List<LocalFile> generatedFiles = new ArrayList<>();
    HtmlTemplateFinder titleTemplateFinder = getTitlePageTemplate();

    for (PdfDocument pdfDocument : documents) {
      Map<String, Object> report = new HashMap<>();
      pdfDocument.getGenerateScheduleReport().setEndDate(new Date());
      report.put("report", pdfDocument.getGenerateScheduleReport());
      SinglePdfPage singlePdfPage = new SinglePdfPage(titleTemplateFinder, report);
      try {
        String path = pdfGenerator.generateTemplate(pdfDocument, singlePdfPage);
        LocalFile pdfFileResponse = new LocalFile();
        pdfFileResponse.setFileName(pdfDocument.getFileName());
        pdfFileResponse.setPath(path);
        generatedFiles.add(pdfFileResponse);
      } catch (Exception e) {
        logger.error("Cannot create PDF file", e);
      }
    }
    return generatedFiles;
  }

  public HtmlTemplateFinder getTitlePageTemplate() {

    HtmlTemplateFinder htmlTemplateFinder = new HtmlTemplateFinder();
    htmlTemplateFinder.setHtmlFilePath("templates/schedule/title.html");
    htmlTemplateFinder.setCssFilePath("templates/schedule/css/title.css");
    htmlTemplateFinder.setWorkingDirectory(appConfig.STORAGE_ABSOLUTE_PATH);
    htmlTemplateFinder.process();
    return htmlTemplateFinder;
  }
}
