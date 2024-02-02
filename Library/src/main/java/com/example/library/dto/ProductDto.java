package com.example.library.dto;

import com.example.library.model.Category;
import com.example.library.model.Color;
import com.example.library.model.Image;
import com.example.library.model.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private int currentQuantity;

    private double costPrice;

    private double salePrice;

    private List<Image> images;

    private List<Size> sizes;

    private List<Color> colors;

    @Pattern(regexp = "^[A-Za-z]", message = "Not accepted numeric values")
    private Category category;

    private boolean activated;

    private boolean deleted;
}
