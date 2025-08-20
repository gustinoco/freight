package com.shipping.demo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "freight_rate", indexes = {
        @Index(name = "ix_freight_rate_origin_dest", columnList = "originZip,destinationZip")
})
public class FreightRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 8)
    private String originZip;

    @Column(nullable = false, length = 8)
    private String destinationZip;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int deadlineDays;

    public Long getId() { return id; }
    public String getOriginZip() { return originZip; }
    public String getDestinationZip() { return destinationZip; }
    public double getPrice() { return price; }
    public int getDeadlineDays() { return deadlineDays; }

    public void setId(Long id) { this.id = id; }
    public void setOriginZip(String originZip) { this.originZip = originZip; }
    public void setDestinationZip(String destinationZip) { this.destinationZip = destinationZip; }
    public void setPrice(double price) { this.price = price; }
    public void setDeadlineDays(int deadlineDays) { this.deadlineDays = deadlineDays; }
}