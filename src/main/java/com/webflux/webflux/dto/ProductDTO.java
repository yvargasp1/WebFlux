package com.webflux.webflux.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
    @NotBlank(message = "name is mandatory")
    private String name;
    private String image;
    @Min(value = 1, message = "Price must be greater then zero")
    private float price;

}

