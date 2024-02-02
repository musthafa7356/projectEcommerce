package com.example.library.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {

    private  Long id;

    @NotBlank
    @Size(min = 3,max = 10,message = "First name contains 3-10 characters")
    @Pattern(regexp = "^[A-Za-z\\\\s]+$", message = "Username must contain only alphanumeric characters.")
    private String firstName;

    @NotBlank
    @Size(min = 3,max=10,message = "Last name contains 3-10 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Username must contain only alphanumeric characters.")
    private String lastName;

    @Email
    private String username;

    @Size(min = 3, max = 20, message = "password should have 3-15 characters")
    @NotEmpty
    private String password;

    @NotEmpty(message = "PhoneNumber Cannot be Empty")
    @Min(value = 1000000000, message = "Phone number should have at least 10 digits")
    @Max(value = 9999999999L, message = "Phone number should have at most 10 digits")
    private String phoneNumber;
    private String addresses;
    private String confirmPassword;

//    @AssertTrue(message = "The customer is blocked")
    private boolean isBlocked;
    private long otp;
    private String repeatPassword;

    private boolean isActive;
    private String referralCode;
}
