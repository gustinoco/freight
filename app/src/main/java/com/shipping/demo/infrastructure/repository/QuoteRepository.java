package com.shipping.demo.infrastructure.repository;

import com.shipping.demo.domain.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}