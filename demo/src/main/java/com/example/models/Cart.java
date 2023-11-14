package com.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;
    @ManyToMany(targetEntity = Product.class)
    Set<Product> products = new HashSet<>();
}
