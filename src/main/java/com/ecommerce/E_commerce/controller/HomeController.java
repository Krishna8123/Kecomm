package com.ecommerce.E_commerce.controller;

import com.ecommerce.E_commerce.service.ProductService;
import com.ecommerce.E_commerce.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;


import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    // 🏠 Home Page (with optional category filter)
    @GetMapping({"/", "/home"})
    public String home(Model model,
                       @RequestParam(name = "categoryId", required = false) Long categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        if (categoryId != null) {
            model.addAttribute("products", productService.getProductsByCategoryId(categoryId));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        model.addAttribute("categories", productService.getAllCategories());
        return "home";
    }

    // 🔍 Search Products by name or description
    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Product> products;

        if (query == null || query.trim().isEmpty()) {
            products = productService.getAllProducts();
        } else {
            products = productService.searchProducts(query.trim());
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("searchQuery", query); // Optional: keep search term in bar
        return "home";
    }
}
