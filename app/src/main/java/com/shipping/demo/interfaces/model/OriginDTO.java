package com.shipping.demo.interfaces.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OriginDTO {


    @NotBlank(message = "CEP de origem é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP de origem deve conter 8 dígitos numéricos")
    private String postalCode;
}
