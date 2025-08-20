package com.shipping.demo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shipping_option", uniqueConstraints = {
        @UniqueConstraint(name = "br_shipping_option_code", columnNames = "code")
})
public class ShippingOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String code;

    public ShippingOption() {}

    public ShippingOption(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }
}