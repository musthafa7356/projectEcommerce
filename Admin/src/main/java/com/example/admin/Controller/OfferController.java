package com.example.admin.Controller;

import com.example.library.dto.CategoryDto;
import com.example.library.dto.OfferDto;
import com.example.library.dto.ProductDto;
import com.example.library.exception.OfferException;
import com.example.library.model.Category;
import com.example.library.model.Offer;
import com.example.library.service.CategoryService;
import com.example.library.service.OfferService;
import com.example.library.service.ProductService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class OfferController {

    private OfferService offerService;
    private CategoryService categoryService;
    private ProductService productService;

    public OfferController(OfferService offerService, CategoryService categoryService, ProductService productService) {
        this.offerService = offerService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/offers")
    public String offer(Principal principal, Model model){
       if (principal == null) {
           return "redirect:/login";
       }
        List<OfferDto> offerDtoList = offerService.getAllOffers();
       model.addAttribute("offers", offerDtoList);
       model.addAttribute("size", offerDtoList.size());

       return "offers";
    }

    @GetMapping("/offers/add-offer")
    public String addOffer(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<ProductDto> productList = productService.allProduct();
        List<Category> categoryList = categoryService.findAllByActivatedTrue();

        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);
        model.addAttribute("offer", new OfferDto());

        return "add-offer";
    }

    @PostMapping("/offers/save")
    public String postOffer(@ModelAttribute("offer") OfferDto offer, RedirectAttributes redirectAttributes) {
        try {
            Offer savedOffer = offerService.save(offer);
            //offerService.save(offer);
            if (savedOffer != null) {
                redirectAttributes.addFlashAttribute("success", "Added new offer successfully!");
            }
            else {
                redirectAttributes.addFlashAttribute("error", "Failed to add new Product");
            }
            //redirectAttributes.addFlashAttribute("success", "Added new offer successfully!");
        }
        catch (OfferException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new offer!.....");
        }

        return "redirect:/offers";
    }

    @GetMapping("/offers/update-offer/{id}")
    public String update(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductDto> productList = productService.allProduct();
        List<Category> categoryList = categoryService.findAllByActivatedTrue();

        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);

        OfferDto offerDto = offerService.findById(id);
        model.addAttribute("offer", offerDto);
        return "update-offer";
    }

    @PostMapping("/offers/update-offer/{id}")
    public String addUpdate(@ModelAttribute("offer") OfferDto offerDto, RedirectAttributes redirectAttributes) {
        try {
            offerService.update(offerDto);
            redirectAttributes.addFlashAttribute("success", "Updated successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/offers";
    }

    @GetMapping("/enable-offer/{id}")
    public String enable(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        offerService.enable(id);
        redirectAttributes.addFlashAttribute("success", "Enabled successfully");
        return "redirect:/offers";
    }

    @GetMapping("/disable-offer/{id}")
    public String disable(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        offerService.disable(id);
        redirectAttributes.addFlashAttribute("success", "Offer disabled");
        return "redirect:/offers";
    }

    @GetMapping("/delete-offer/{id}")
    public String delete(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        offerService.deleteOffer(id);
        redirectAttributes.addFlashAttribute("success", "Offer deleted");
        return "redirect:/offers";
    }
}
