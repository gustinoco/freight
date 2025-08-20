package com.shipping.demo.domain.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originCep;
    private String destinationCep;

    private double width;
    private double height;
    private double length;
    private double weight;

    private String selectedServices;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private boolean success;

    @Column(length = 1000)
    private String errorMessage;

    public Long getId() { return id; }
    public String getOriginCep() { return originCep; }
    public String getDestinationCep() { return destinationCep; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getLength() { return length; }
    public double getWeight() { return weight; }
    public String getSelectedServices() { return selectedServices; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }

    public void setId(Long id) { this.id = id; }
    public void setOriginCep(String originCep) { this.originCep = originCep; }
    public void setDestinationCep(String destinationCep) { this.destinationCep = destinationCep; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setLength(double length) { this.length = length; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setSelectedServices(String selectedServices) { this.selectedServices = selectedServices; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}