package com.shipping.demo.domain.service;

import com.shipping.demo.domain.model.ShippingOption;
import com.shipping.demo.interfaces.model.ShippingOptionDTO;
import com.shipping.demo.infrastructure.repository.ShippingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OptionsService {

    private final ShippingRepository repository;
    private final AtomicReference<List<ShippingOptionDTO>> cache = new AtomicReference<>();

    public OptionsService(ShippingRepository repository) {
        this.repository = repository;
    }

    public List<ShippingOptionDTO> getAllOptions() {
        List<ShippingOptionDTO> current = cache.get();
        if (current != null) {
            return current;
        }
        // Carrega do DB e guarda em cache
        List<ShippingOptionDTO> loaded = repository.findAll()
                .stream()
                .map(ShippingOption::getCode)
                .sorted()
                .map(ShippingOptionDTO::new)
                .toList();
        cache.compareAndSet(null, loaded);
        return cache.get();
    }
}