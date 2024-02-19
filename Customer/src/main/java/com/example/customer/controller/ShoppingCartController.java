package com.example.customer.controller;

import com.example.library.dto.ProductDto;
import com.example.library.exception.InsufficientQuantityException;
import com.example.library.model.CartItem;
import com.example.library.model.Customer;
import com.example.library.model.ShoppingCart;
import com.example.library.repository.CartItemRepository;
import com.example.library.service.AddressService;
import com.example.library.service.CustomerService;
import com.example.library.service.ProductService;
import com.example.library.service.ShoppingCartService;
import com.example.library.service.impl.ShoppingCartServiceImpl;
import jakarta.mail.search.SearchTerm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.validator.cfg.defs.Mod10CheckDef;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Set;

@Controller
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final CartItemRepository itemRepository;
    private final ShoppingCartServiceImpl shoppingCartService;

    public ShoppingCartController(ShoppingCartService cartService, ProductService productService, CustomerService customerService, AddressService addressService, CartItemRepository itemRepository, ShoppingCartServiceImpl shoppingCartService){
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.itemRepository = itemRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/cart")
    public String Cart(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Set<CartItem> item = null;
            if(cart!=null) {
                 item = cart.getCartItems();
            }
            if (cart == null || cart.getCartItems().isEmpty()){
                System.out.println("cart null");
                model.addAttribute("text", "Cart is empty");
            }
            if (cart != null){
                model.addAttribute("grantTotal", cart.getTotalPrice());
            }
            System.out.println(cart.getTotalPrice());
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("cartItem", item);
            model.addAttribute("title", "cart");
            return "cart";
        }
    }

    @RequestMapping(value = "/add-to-cart/{id}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request, Model model,
                                Principal principal,
                                HttpSession session,
                                RedirectAttributes redirectAttributes){
        ProductDto productDto = productService.getById(id);
        if (principal == null){
            return "redirect:/login";
        }
        else {
            String username = principal.getName();
            System.out.println(id);
            try {
                ShoppingCart shoppingCart = cartService.addItemToCart(productDto, quantity, username);
                //System.out.println(shoppingCart);
                session.setAttribute("totalItem", shoppingCart.getTotalItems());
                model.addAttribute("shoppingCart", shoppingCart);
                Set<CartItem> cartItems = shoppingCart.getCartItems();
                model.addAttribute("cartItem", cartItems);
            }
            catch (InsufficientQuantityException ex){
                String errorMessage = ex.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            }
        }
        return "cart";
    }

    @RequestMapping(value = "/update-cart/{id}", method = RequestMethod.POST, params = "action-update")
    public String updateCart(@RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "quantity", required = false) int quantity,
                             Model model, Principal principal, RedirectAttributes redirectAttributes,
                             HttpServletRequest httpServletRequest) throws Exception{
        if (principal == null){
            return "redirect:/login";
        }
        else {
            ProductDto productDto = productService.getById(id);
            String username = principal.getName();
            try {
                ShoppingCart shoppingCart = cartService.updateCart(productDto, quantity, username);
                redirectAttributes.addAttribute("shoppingCart", shoppingCart);
            }
            catch (InsufficientQuantityException ex){
                String errorMessage = ex.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            }
        }
        return "redirect:/cart";
    }

    @RequestMapping(value = "/delete-cart/{id}", method = RequestMethod.POST, params = "action-delete")
    private String deleteItem(@RequestParam("id") Long id,
                              Model model,
                              Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        else {
            ProductDto productDto = productService.getById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/cart";
        }
    }
}
