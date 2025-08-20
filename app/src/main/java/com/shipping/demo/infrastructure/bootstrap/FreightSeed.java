package com.shipping.demo.infrastructure.bootstrap;

import com.shipping.demo.domain.model.FreightRate;
import com.shipping.demo.infrastructure.repository.FreightRateRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class FreightSeed {

    @Bean
    ApplicationRunner seedFreight(FreightRateRepository repo) {
        return args -> seed(repo);
    }

    @Transactional
    void seed(FreightRateRepository repo) {
        if (repo.count() == 0) {
            FreightRate convencional = new FreightRate();
            convencional.setDeadlineDays(5);
            convencional.setPrice(10);
            convencional.setDestinationZip("01310931");
            convencional.setOriginZip("01310932");

            FreightRate express = new FreightRate();
            express.setPrice(20);
            express.setDestinationZip("79803030");
            express.setOriginZip("04445080");
            express.setDeadlineDays(2);

            repo.save(convencional);
            repo.save(express);
        }
    }
}