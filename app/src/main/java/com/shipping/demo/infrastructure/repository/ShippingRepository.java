package com.shipping.demo.infrastructure.repository;

import com.shipping.demo.domain.model.ShippingOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingOption, Long> {
    boolean existsByCode(String code);
}
