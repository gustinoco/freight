package com.shipping.demo.infrastructure.repository;

import com.shipping.demo.domain.model.FreightRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreightRateRepository extends JpaRepository<FreightRate, Long> {

    List<FreightRate> findByOriginZipAndDestinationZip(String originZip, String destinationZip);
}