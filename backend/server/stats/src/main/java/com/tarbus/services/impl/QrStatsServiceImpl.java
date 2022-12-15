
package com.tarbus.services.impl;

import com.tarbus.models.QrScanDataModel;
import com.tarbus.repositories.jpa.BusLineRepository;
import com.tarbus.repositories.jpa.BusStopRepository;
import com.tarbus.repositories.jpa.QrStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.services.QrStatsService;

@Service
public class QrStatsServiceImpl implements QrStatsService {

  @Autowired
  private QrStatsRepository qrStatsRepository;
  @Autowired
  private BusStopRepository busStopRepository;
  @Autowired
  private BusLineRepository busLineRepository;

  @Override
  public QrScanDataModel add(QrScanDataModel qrScanDataModel) {
    return qrStatsRepository.save(qrScanDataModel);
  }
}
