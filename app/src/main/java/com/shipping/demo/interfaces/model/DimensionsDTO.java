package com.shipping.demo.interfaces.model;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class DimensionsDTO {

    @DecimalMin(value = "0.0", inclusive = false, message = "Largura deve ser maior que zero")
    private double width;

    @DecimalMin(value = "0.0", inclusive = false, message = "Altura deve ser maior que zero")
    private double height;

    @DecimalMin(value = "0.0", inclusive = false, message = "Comprimento deve ser maior que zero")
    private double length;

    @DecimalMin(value = "0.0", inclusive = false, message = "Peso deve ser maior que zero")
    private double weight;

}
