package com.example.admin.Controller;

import com.example.library.model.Category;
import com.example.library.service.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/categories")
    public String categories(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication ==  null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        model.addAttribute("title", "Manage Category");
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("categoryNew", new Category());
        return "categories";
    }
    @PostMapping("/save-category")
    public String save(@ModelAttribute("categoryNew") Category category, Model model, RedirectAttributes redirectAttributes){
        try {
            categoryService.save(category);
            model.addAttribute("categoryNew", category);
            redirectAttributes.addFlashAttribute("success","Add Successfully!");
        }
        catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Duplicate name of category, please check again!");
        }
        catch (Exception e2){
            e2.printStackTrace();
            model.addAttribute("categoryNew", category);
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/findById/{id}",method = {RequestMethod.PUT, RequestMethod.GET})
    @ResponseBody
    public Category findById(@PathVariable("id")Long id){

        return categoryService.findById(id);
    }

    @PostMapping("/update-category")
    public String update(Category category, RedirectAttributes redirectAttributes){
        try {
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("success", "update successfully!");
        }
        catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category,please check again!");
        }
        catch (Exception e2){
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error from server or duplicate, please check again!");
        }
        return "redirect:/categories";
    }

    @GetMapping("/delete-category/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Disabled Successfully!");
        }
        catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        }
        catch (Exception e2){
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error Server");
        }
        return "redirect:/categories";
    }

    @GetMapping("/enable-category/{id}")
    public String enable(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try {
            System.out.println("Recieved"+id);
            categoryService.enableByID(id);
            redirectAttributes.addFlashAttribute("success", "Enable successfully!");
        }
        catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        }
        catch (Exception e2){
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories";
    }
}
