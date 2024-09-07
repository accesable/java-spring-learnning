package com.example.learningjava.dtos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Valid
    @NotBlank(message = "Product name is required")
    @NotNull(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    private Double price;
}
