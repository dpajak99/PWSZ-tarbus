package com.tarbus.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import com.tarbus.models.QrScanDataModel;

public interface QrStatsRepository extends CrudRepository<QrScanDataModel, Long> {

}
