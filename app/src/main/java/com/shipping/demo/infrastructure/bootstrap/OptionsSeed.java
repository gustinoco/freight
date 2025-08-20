package com.shipping.demo.infrastructure.bootstrap;

import com.shipping.demo.domain.model.ShippingOption;
import com.shipping.demo.infrastructure.repository.ShippingRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OptionsSeed {
    private static final List<String> DEFAULT_OPTIONS = List.of("convencional", "express", "all");

    @Bean
    ApplicationRunner seedShippingOptions(ShippingRepository repository) {
        return args -> ensureDefaultOptions(repository);
    }

    @Transactional
    void ensureDefaultOptions(ShippingRepository repository) {
        for (String code : DEFAULT_OPTIONS) {
            if (!repository.existsByCode(code)) {
                repository.save(new ShippingOption(code));
            }
        }
    }

}
