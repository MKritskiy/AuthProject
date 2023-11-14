package com.example.controllers;

import com.example.models.Cart;
import com.example.models.Product;
import com.example.services.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins="http://localhost:8080")
@Controller
@RequestMapping("/cart")
public class CartController {
    CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Cart>> getAllCarts(){
        return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable("id") Long id){
        return new ResponseEntity<>(cartService.getCartById(id), HttpStatus.OK);
    }

    @GetMapping("/{cartId}/addProduct/{productId}")
    public String addProductToCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        try {
            cartService.addProductToCart(cartId, productId);
            return "redirect:/products";
        } catch (EntityNotFoundException ex) {
            return "/error";
        } catch (Exception e) {
            return "/error";
        }
    }
    @GetMapping("/{cartId}/remove-product/{productId}")
    public String removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return "redirect:/cart/shoping-cart";
    }

    @GetMapping("/shoping-cart")
    public String showCart(Model model){
        Cart currentCart = cartService.getCartById(cartService.currentCartId);
        Set<Product> products = currentCart.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("cart_id", cartService.currentCartId);
        return "shoping-cart";
    }

    @GetMapping("/{cartId}/make-offer/")
    public String makeOffer(@PathVariable("cartId") Long cartId){
        cartService.removeAllProductsFromCart(cartId);
        return "redirect:/products";
    }
}
