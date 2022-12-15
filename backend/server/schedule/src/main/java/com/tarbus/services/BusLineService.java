package com.tarbus.services;

import com.tarbus.models.BusLine;

import java.util.List;

public interface BusLineService {

  List<BusLine> getAll(Long versionId);

  List<BusLine> getAll();

  BusLine getById(Long id);

  void saveLinesList(List<BusLine> busLines);

  void deleteLinesList(List<BusLine> busLines);

  void deleteById(Long busLineId);

  List<BusLine> addAll(List<BusLine> busLines);

  BusLine add(BusLine busLine);
}
