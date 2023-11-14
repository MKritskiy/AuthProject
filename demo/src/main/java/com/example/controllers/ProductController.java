package com.example.controllers;

import com.example.models.Cart;
import com.example.models.Product;
import com.example.services.CartService;
import com.example.services.ProductService;
import lombok.extern.java.Log;
import org.hibernate.cache.spi.support.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins="http://localhost:8080")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @GetMapping("/products")
    public String showAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        Cart cart = cartService.getCartById(CartService.currentCartId);
        Set<Product> cartProducts = cart.getProducts();
        for (Iterator<Product> iterator = products.listIterator(); iterator.hasNext();){
            Product product = iterator.next();
            System.out.println("Product: " + ((Product)product).getId());
            if (cartProducts.stream().anyMatch(i->i.getId().equals(product.getId())))
            {
                System.out.println("Here: " + ((Product)product).getId());
                iterator.remove();
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("cartId", CartService.currentCartId);
        return "product-list";
    }

}
