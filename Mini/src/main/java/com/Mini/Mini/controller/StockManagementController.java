package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.dto.ProductDTO;
import com.Mini.Mini.repository.ProductRepository;
import com.Mini.Mini.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class StockManagementController {
    @Autowired
    ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/admin/StockManagement")
    public String getStockManagement(Model model){

        model.addAttribute("products",productService.getAllProduct());
        return "StockManagement";
    }
    @GetMapping("/admin/StockManagement/update/{id}")
    public String updateStock(@PathVariable("id")Long id,
                              Model model){
        Optional<Product> product=productService.getProductById(id);
        ProductDTO productDTO=new ProductDTO();
        productDTO.setUnitsInStock(product.get().getUnitsInStock());
        productDTO.setName(product.get().getName());
        productDTO.setId(productDTO.getId());
        model.addAttribute("productDTO",productDTO);

        return "UpdateStock";
    }
    @PostMapping("/admin/StockManagement/update/save")
    public String UpdateStockSave(@ModelAttribute("productDTO") ProductDTO productDTO){
        Product product = productService.getProductById(productDTO.getId()).get();
//        product.setId(productDTO.getId());
        product.setUnitsInStock(productDTO.getUnitsInStock());
        productRepository.save(product);

        return "redirect:/StockManagement";
    }
}
