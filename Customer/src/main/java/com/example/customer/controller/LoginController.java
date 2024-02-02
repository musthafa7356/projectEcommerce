package com.example.customer.controller;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Category;
import com.example.library.model.Customer;
import com.example.library.repository.CustomerRepository;
import com.example.library.service.CategoryService;
import com.example.library.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    private final CategoryService categoryService;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    public LoginController(CategoryService categoryService, CustomerRepository customerRepository, CustomerService customerService, BCryptPasswordEncoder passwordEncoder){
        this.categoryService = categoryService;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title","Login Page");
        model.addAttribute("Page","Home");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("page", "Register");
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }

    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                                   BindingResult result,
                                   Model model){
        try {
            if (result.hasErrors()){
                model.addAttribute("customerDto" , customerDto);
                return "register";
            }
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username);
            if (customer!=null){
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("error", "Email has been register!");
                return "register";
            }
            if (customerDto.getPassword().equals(customerDto.getConfirmPassword())){
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                model.addAttribute("success","Register successfully!");
            }
            else {
                model.addAttribute("error","password is incorrect");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "Server is error, try again later!");
        }
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordOTP(Model model, CustomerDto customerDto){
        model.addAttribute("title", "Forgot Password-OTP");
        model.addAttribute("username", customerDto);

        return "enter-username";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordOTPSend(@ModelAttribute("username") CustomerDto customerDto, Model model){
        String otp = customerService.generateOTPAndSendOnEmail(customerDto.getUsername());
        model.addAttribute("data", customerDto);
        model.addAttribute("otpGenerationTime", System.currentTimeMillis());
        return "otpScreenEmail";
    }

    @PostMapping("/forgot-password/otpVerification")
    public String otpSentEmailPost(@ModelAttribute("data") CustomerDto customerDto, Model model){
        System.out.println("Came here on 114 login controller");
        boolean isOTPValid = customerService.verifyOTP(customerDto.getOtp(), customerDto.getUsername());
        if (isOTPValid) {
            model.addAttribute("customerDto",customerDto);
            return "passwordResetPage";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "otpScreenEmail";
        }
    }

    @PostMapping("/passwordResetPage")
    public String passwordResetPage(@ModelAttribute("customerDto") CustomerDto customerDto, RedirectAttributes redirectAttributes){
        if (customerDto.getPassword().equals(customerDto.getRepeatPassword())){
            Customer customer = customerRepository.findByUsername(customerDto.getUsername());
            customer.setPassword((passwordEncoder.encode(customerDto.getPassword())));
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("success", "Password is changed");
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Passwords are not same");
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("address");
        session.invalidate();
        return "redirect:/login";
    }
}
