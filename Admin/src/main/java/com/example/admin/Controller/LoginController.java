package com.example.admin.Controller;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public LoginController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/login")
    public String loginForm(Model model){
       model.addAttribute("title","Login");
       return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        System.out.println("register controller");
       model.addAttribute("title","Register");
       model.addAttribute("adminDto", new AdminDto());
       return "register";
    }

    @GetMapping("/index")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
   }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
       model.addAttribute("title","Forgot Password");
       return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@ModelAttribute("adminDto")AdminDto adminDto,
                              BindingResult result,
                              Model model){
        System.out.println("1");
       try {
           if (result.hasErrors()){
               model.addAttribute("adminDto",adminDto);
               result.toString();
               return "register";
           }
           String username = adminDto.getUsername();
           Admin admin = adminService.findByUsername(username);
           if (admin!=null){
               model.addAttribute("adminDto",adminDto);
               System.out.println("admin not null");
               model.addAttribute("emailError","Your emnail has been registered!");
               return "register";
           }
           if (adminDto.getPassword().equals(adminDto.getRepeatPassword())){
               adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
               System.out.println("2");
               adminService.save(adminDto);
               System.out.println("Success");
               model.addAttribute("success","Register Successfully");
               model.addAttribute("adminDto",adminDto);
           }
           else {
               model.addAttribute("adminDto",adminDto);
               model.addAttribute("passwordError","your password may be error! check again!");
               System.out.println("Password not same");
               return "register";
           }
       }
       catch (Exception e){
           e.printStackTrace();
           model.addAttribute("errors", " The server has been wrong!");
       }
       return "register";
    }
}
