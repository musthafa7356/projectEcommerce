package com.example.customer.config;

import com.example.library.model.Customer;
import com.example.library.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    private final CustomerRepository customerRepository;

    public CustomLogoutHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication.getName();
        Customer customer = customerRepository.findByUsername(username);
        if(customer!=null){
            customer.setBlocked(false);
            customerRepository.save(customer);
        }

    }
}
