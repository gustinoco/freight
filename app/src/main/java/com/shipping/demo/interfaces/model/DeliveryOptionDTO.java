package com.shipping.demo.interfaces.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryOptionDTO {
    private String name;
    private int deadeline;
    private double price;
}