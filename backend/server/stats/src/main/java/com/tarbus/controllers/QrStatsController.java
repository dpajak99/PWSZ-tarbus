package com.tarbus.controllers;

import com.tarbus.models.QrScanDataModel;
import com.tarbus.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tarbus.services.QrStatsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;


@RestController
@CrossOrigin
@RequestMapping("/api/stats/")
public class QrStatsController {
  private QrStatsService qrStatsService;

  @Autowired
  public void setQrStatsService(QrStatsService qrStatsService) {
    this.qrStatsService = qrStatsService;
  }


  @GetMapping({"/store"})
  public ResponseEntity<?> onScheduleQrScan(HttpServletRequest request, @RequestParam String directFrom, @RequestParam Long busStopId, @RequestParam Long busLineId) throws ServletException, IOException {
    UserInfo userInfo = new UserInfo(request);
    QrScanDataModel qrScanDataModel = new QrScanDataModel(directFrom, userInfo.clientOS, userInfo.clientBrowser, busStopId, busLineId);
    qrStatsService.add(qrScanDataModel);
    String redirectLink = "https://play.google.com/store/apps/details?id=com.dpajak99.tarbus2021";
    switch(userInfo.clientOS) {
      case "Mac":
      case "IPhone":
        redirectLink = "https://apps.apple.com/pl/app/tarbus/id1554556128";
        break;
    }

    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectLink)).build();
  }
}
