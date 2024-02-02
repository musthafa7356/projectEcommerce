package com.example.customer.controller;

import com.example.library.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReferralController {

    private CustomerService customerService;

    public ReferralController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/shareReferral")
    public ResponseEntity<String> shareReferral(@RequestParam String referralCode,
                                                @RequestParam String emailAddress){

        String success = "success";
        String result = customerService.shareReferralCode(referralCode, emailAddress);
        if (result.equals(success)) {
            return ResponseEntity.ok("success");
        }
        else {
            return ResponseEntity.ok("invalid");
        }
    }
}
