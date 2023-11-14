package com.example.services;

import com.example.models.Cart;
import com.example.models.Product;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    public static Long currentCartId = Long.valueOf(1);
    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }

    @Transactional
    public Cart getCartById(Long cartId) {
        if (cartRepository.findAll().size()<1) {
            Cart cart = cartRepository.findById(cartId).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setId(cartId);
                currentCartId = cartId;
                return cartRepository.save(newCart);
            });
        }
        return cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " not found"));
    }


    @Transactional
    public void addProductToCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setId(cartId);
            currentCartId = cartId;
            return cartRepository.save(newCart);
        });
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
        System.out.println(productId);
        cart.getProducts().add(product);
        cartRepository.save(cart);
    }
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));                ;
        cart.getProducts().remove(product);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeAllProductsFromCart(Long cartId){
        Cart cart = cartRepository.findById(cartId).
                orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " not found"));
            cart.getProducts().removeAll( cart.getProducts());
            cartRepository.save(cart);
            //productRepository.deleteAll();
    }
}
