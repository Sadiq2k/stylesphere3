package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductVariant;
import com.Mini.Mini.Entity.Variants;
import com.Mini.Mini.dto.ProductDTO;
import com.Mini.Mini.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class VariantController {
    @Autowired
    VariantsService variantsService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductVariantService  productVariantService;


    @GetMapping("/admin/variantsAdd")
    public String showVariantPage(Model model){
        model.addAttribute("variants",new Variants());
        model.addAttribute("allVariants",variantsService.getAllVariantName());
        return "variantsAdd";
    }

    @PostMapping("/admin/variantsAdd")
    public String addVariants(@ModelAttribute("variants") Variants variants, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "variantsAdd";
        }
//        Product product = productService.getProductById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
//        ProductDTO productDTO=new ProductDTO();

//        productDTO.setName(product.getName());
        variantsService.saveVariants(variants);
        model.addAttribute("variants",variantsService.getAllVariantName());
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("categories",categoryService.getAllCategory());
//        model.addAttribute("productDTO",productDTO);

        return "redirect:/admin/variantsAdd";
    }


    @GetMapping("/admin/AllVariants")
    public String getVariantsProduct( Model model){
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("variants",new Variants());
        return "AllVariants";
    }

    @GetMapping("/admin/UpdateVariants/{id}")
    public String getUpdateVariantPage(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());

        model.addAttribute("variants", productVariantService.getVariantsByProductId(id));
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("product", productDTO);

        return "UpdateVariantsSize";
    }

    @PostMapping("/admin/UpdateVariants")
    public String updateVariants(@PathVariable Long id, Model model){
        Product product = productService.getProductById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        ProductDTO productDTO=new ProductDTO();

        productDTO.setName(product.getName());
        final Optional<ProductVariant> variantIdByProductId = productVariantService.getVariantIdByProductId(product);
        System.out.println("variant  "+variantIdByProductId);

        model.addAttribute("variants",variantsService.getAllVariantName());
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",productDTO);

        return "UpdateVariantsSize";
    }


}
