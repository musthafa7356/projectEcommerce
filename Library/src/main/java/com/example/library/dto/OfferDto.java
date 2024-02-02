package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 15, message = "Name should contain between 3 to 15 characters")
    @Pattern(regexp = "^[A-Za-z\\\\s]+$", message = "Name must contain only alphanumeric characters.")
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    @Pattern(regexp = "^[0-9\\\\s]+$", message = "Percentage must contain only numbers.")
    private int offPercentage;

    private String offerType;

    private Long offerProductId;

    private String applicableForProductName;

    private Long offerCategoryId;

    private String applicableForCategoryName;

    private boolean enabled;

}




