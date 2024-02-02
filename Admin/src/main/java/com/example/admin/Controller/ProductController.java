package com.example.admin.Controller;

import com.example.library.dto.CategoryDto;
import com.example.library.dto.ProductDto;
import com.example.library.exception.DisabledProductExistsException;
import com.example.library.model.*;
import com.example.library.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SizeService sizeService;
    private final ColorService colorService;
    private final ImageService imageService;

    public ProductController(ProductService productService, CategoryService categoryService, SizeService sizeService, ColorService colorService, ImageService imageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.sizeService = sizeService;
        this.colorService = colorService;
        this.imageService = imageService;
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("size", products.size());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "products";
    }

//    @GetMapping("/products/{pageNo}")
//    public String allProducts(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
//        if (principal == null){
//            return "redirect:/login";
//        }
//        Page<ProductDto> products = productService.getAllProduct(pageNo);
//        model.addAttribute("title", "Manage Products");
//        model.addAttribute("size", products.getSize());
//        model.addAttribute("products", products);
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", products.getTotalPages());
//        return "products";
//    }

//    @GetMapping("/search-products/{pageNo}")
//    public String searchProduct(@PathVariable("pageNo") int pageNo,
//                                @RequestParam(value = "keyword")String keyword,
//                                Model model){
//        Page<ProductDto> products=productService.searchProduct(pageNo, keyword);
//        model.addAttribute("title","Result Search Products");
//        model.addAttribute("size",products.getSize());
//        model.addAttribute("products",products);
//        model.addAttribute("currentPage",pageNo);
//        model.addAttribute("totalPages",products.getTotalPages());
//        return "product-result";
//    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("title", "Add product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<Size> sizes = sizeService.allSizeVariations();
        List<Color> colors = colorService.allColorVariations();
        model.addAttribute("sizes", sizes);
        model.addAttribute("colors", colors);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryNew", new CategoryDto());
        model.addAttribute("productDto", new ProductDto());

        return "add-product";
    }

//    @PostMapping("/save-product")
//    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
//                              @RequestParam("imageProduct") List<MultipartFile> imageProduct,
//                              @RequestParam("sizes") List<Long> sizes_id,
//                              @RequestParam("colors") List<Long> colors_id,
//                              RedirectAttributes redirectAttributes) {
//        try {
//            productService.save(imageProduct, product, sizes_id, colors_id);
//            redirectAttributes.addFlashAttribute("success", "New product added successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
//        }
//        return "redirect:/products";
//    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
                              @RequestParam("imageProduct") List<MultipartFile> imageProduct,
                              @RequestParam("sizes") List<Long> sizes_id,
                              @RequestParam("colors") List<Long> colors_id,
                              RedirectAttributes redirectAttributes) {
        try {
            // Call the service to save the product
            Product savedProduct = productService.save(imageProduct, product, sizes_id, colors_id);

            // Check if the product was saved successfully
            if (savedProduct != null) {
                redirectAttributes.addFlashAttribute("success", "New product added successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
            }
        } catch (DisabledProductExistsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while adding the product.");
        }
        return "redirect:/products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<Size> sizes = sizeService.allSizeVariations();
        List<Color> colors = colorService.allColorVariations();
        ProductDto productDto = productService.getById(id);
        List<Image> images = imageService.findProductImages(id);
        model.addAttribute("title", "Add Product");
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        model.addAttribute("images", images);
        model.addAttribute("sizes", sizes);
        model.addAttribute("colors", colors);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProducts") List<MultipartFile> imageProducts,
                                @RequestParam("sizes") List<Long> sizes_id,
                                @RequestParam("colors") List<Long> colors_id,
                                RedirectAttributes redirectAttributes) {
        try {
            System.out.println(productDto);
            productService.update(imageProducts, productDto, sizes_id, colors_id);
            redirectAttributes.addFlashAttribute("success", "Update successfylly!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, Please try again!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.GET})
    public String enableProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try {
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled seccessfully!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.GET})
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        System.out.println("Controller codeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee Delete");
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success","Deleted successfully!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/products";
    }

    /*@GetMapping(value = "/delete-product/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        System.out.println("Sysyout Controller classs----------------------------------------------------------");
        try {
            productService.deleteById(id);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Deleted successfully!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Deletion failed!\"}");
        }
    }*/



}
