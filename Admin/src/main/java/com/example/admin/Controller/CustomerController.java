package com.example.admin.Controller;

import com.example.library.dto.CustomerDto;
import com.example.library.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }
    @GetMapping("/users")
    public String listUser(Model model, Principal principal){
        if (principal == null){
            return "login";
        }
        List<CustomerDto> customers = customerService.findAll();
        model.addAttribute("title", "users");
        model.addAttribute("users", customers);
        System.out.println(customers);
        model.addAttribute("size", customers.size());
        return "user";
    }

    @RequestMapping(value = "/unblock-users/{id}", method = {RequestMethod.GET})
    public String unblockUser(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        try {
            customerService.unblockById(id);
            redirectAttributes.addFlashAttribute("success", "Unblocked successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to Unblock");
        }
        return "redirect:/users";

    }

    @RequestMapping(value = "/block-users/{id}", method = {RequestMethod.GET})
    public String blockUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try {
            customerService.blockById(id);
            redirectAttributes.addFlashAttribute("success", "Blocked successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to delete");
        }
        return "redirect:/users";

    }
}
