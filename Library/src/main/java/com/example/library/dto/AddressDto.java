package com.example.library.dto;

import com.example.library.model.Customer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private long id;

    @NotEmpty(message = "State is required")
    private String state;

    @NotEmpty(message = "Address Line 1 is required")
    private String addressLine1;

    @NotEmpty(message = "Address Line 2 is required")
    private String addressLine2;

    @NotEmpty(message = "District is required")
    private String district;

    @NotEmpty(message = "Pin Code is required")
    @Pattern(regexp = "^\\d{6}$", message = "Pin Code must be a 6-digit number")
    private String pin_code;

    @NotEmpty(message = "Address Type is required")
    private String addressType;

    @NotEmpty(message = "Contact Number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact Number must be a 10-digit number")
    private String contactNumber;

    private boolean Active = true;

    private Customer customer;
}
