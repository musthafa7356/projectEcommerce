package com.example.customer.controller;

import com.example.library.dto.OfferDto;
import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.service.CategoryService;
import com.example.library.service.CustomerService;
import com.example.library.service.OfferService;
import com.example.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    private CategoryService categoryService;

    private ProductService productService;

    private CustomerService customerService;

    private OfferService offerService;

    public ProductController(CategoryService categoryService, ProductService productService, CustomerService customerService, OfferService offerService){
        this.customerService = customerService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.offerService = offerService;
    }

    @GetMapping("/index")
    public String getHomePage(Model model, Principal principal, HttpSession session){
        if (principal != null){
            Customer customer = customerService.findByUsername(principal.getName());
            session.setAttribute("userLoggeIn",true);
            session.setAttribute("username", customer.getFirstName() + " " + customer.getFirstName());
        }
//        List<BannerDto> bannerDtoList=bannerService.getAllBanners();
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> products = productService.getAllProductCatAct();
        List<ProductDto> productChanged= new ArrayList<>();
        for(ProductDto e:products){
            if(e.getCategory().isActivated()){
                productChanged.add(e);
            }
        }
        model.addAttribute("categories", categories);
//        model.addAttribute("banners", bannerDtoList);
        model.addAttribute("products", productChanged);

        return "home";
    }

    @GetMapping("/shoplist")
    public String productList(Model model){
        model.addAttribute("Page", "Products");
        model.addAttribute("title", "Menu");
        List<ProductDto> products = productService.products();
        model.addAttribute("products", products);
        return "shoplist";
    }

    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable("id")Long id, Model model){
        ProductDto product = productService.getById(id);
        model.addAttribute("product", product);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail",product);
        return "product-detail";
    }

    @GetMapping("/menu")
    public String Menu(Model model) {
        model.addAttribute("page", "Products");
        model.addAttribute("title", "Menu");
        List<ProductDto> products = productService.products();
        model.addAttribute("products", products);
        return "home";

    }

    @GetMapping("/products-list")
    public String getShopPage(@RequestParam(name = "id",required = false,defaultValue = "0") long id, Model model,
                              @RequestParam(name = "pageNo",required = false,defaultValue = "0") int pageNo,
                              @RequestParam(name = "sort",required = false,defaultValue = "") String sort,
                              OfferDto offerDto){
        List<Category> categories = categoryService.findAllByActivatedTrue();

        Page<ProductDto> products;
        if (id == 0){
            products = productService.findAllByActivated(pageNo,sort);
            model.addAttribute("sort",sort);
        }
        else {
            products = productService.findAllByActivated(id, pageNo);
        }

        List<ProductDto> productChanged= new ArrayList<>();
        for(ProductDto e:products){
            if(e.getCategory().isActivated()){
                productChanged.add(e);
            }
        }

        long totalProducts = products.getTotalElements();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("products",products);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("size", products.getSize());
        model.addAttribute("categories",categories);
        model.addAttribute("products", productChanged);


        return "shoplist";
    }

    @GetMapping("/search-product")
    public String searchProduct(@RequestParam(defaultValue = "") String keyword, Model model) {
        System.out.println(" product is  " + keyword);
        List<ProductDto> result = productService.searchProduct(keyword);
        System.out.println(" result is " + result);
        model.addAttribute("products", result);
        if (result.isEmpty()) {
            System.out.println("product not found!!!!");
            model.addAttribute("notFound", result);
            return "shoplist";
        }
        return "shoplist";
    }

    @GetMapping("/category/{id}")
    public String displayProductsByCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCartById(id);
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        return "new"; // Assuming "products" is a Thymeleaf template for displaying products
    }

    @PostMapping("/category/{categoryId}")
    public String displayCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCartById(categoryId);
        model.addAttribute("category", category);
        return "new"; // Assuming "products" is a Thymeleaf template for displaying products
    }

}
