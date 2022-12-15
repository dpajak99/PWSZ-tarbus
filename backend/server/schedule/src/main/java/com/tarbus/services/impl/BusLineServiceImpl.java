package com.tarbus.services.impl;

import com.google.common.collect.Lists;
import com.tarbus.models.BusLine;
import com.tarbus.repositories.jpa.BusLineRepository;
import com.tarbus.services.BusLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BusLineServiceImpl implements BusLineService {

    private BusLineRepository busLineRepository;

    @Autowired
    public BusLineServiceImpl(BusLineRepository busLineRepository) {
        this.busLineRepository = busLineRepository;
    }


    @Override
    public List<BusLine> getAll(Long versionId) {
      List<BusLine> result = new ArrayList<>();
      if( versionId != null ) {
        result = busLineRepository.getAllByVersionId(versionId);
      } else {
        busLineRepository.findAll().forEach(result::add);
      }
      return result;
    }

    @Override
    public List<BusLine> getAll() {
        return getAll(null);
    }

    @Override
    public BusLine getById(Long id) {
        return busLineRepository.findById(id).orElse(null);
    }

    @Override
    public void saveLinesList(List<BusLine> busLines) {
        busLineRepository.saveAll(busLines);
    }

    @Override
    public void deleteLinesList(List<BusLine> busLines) {
        busLineRepository.deleteAll(busLines);
    }

    @Override
    public void deleteById(Long busLineId) {
        busLineRepository.deleteById(busLineId);
    }

    @Override
    public List<BusLine> addAll(List<BusLine> busLines) {
        return Lists.newArrayList(busLineRepository.saveAll(busLines));
    }

  @Override
  public BusLine add(BusLine busLine) {
    return busLineRepository.save(busLine);
  }
}
