package com.Mini.Mini.controller;
import com.Mini.Mini.Entity.ProductImage;
import com.Mini.Mini.Entity.ProductVariant;
import com.Mini.Mini.Entity.Variants;
import com.Mini.Mini.dto.ProductDTO;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.dto.VariantDto;
import com.Mini.Mini.repository.ProductRepository;
import com.Mini.Mini.repository.VariantsRepository;
import com.Mini.Mini.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static com.Mini.Mini.controller.AdminController.uploadDir;

@Controller
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    VariantsService variantsService;
    @Autowired
    VariantsRepository variantsRepository;
    @Autowired
    ProductRepository productRepository;


    @GetMapping("/admin/products")
    public String getProduct( Model model){
        List<Product> products = productService.getAllProduct();
        Collections.reverse(products);
        model.addAttribute("products",products);
        return "products";
    }
    @GetMapping("/admin/products/add")
    public String productAddGet(Model model){
        model.addAttribute("productDTO",new ProductDTO());
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("variants",variantsService.getAllVariantName());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productGetPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage") List<MultipartFile> files,
                                 @RequestParam("imgName") String imgName,
                                 @RequestParam(name = "variants", required = false) List<String> variants,
                                 @RequestParam(name = "selectedVariants", required = false) List<Long> selectedVariantIds,
                                 Model model) throws IOException {
//        List<Variants> allVariants = variantsService.getAllVariants(); // or whatever method you have
//        productDTO.updateSelectedVariants(allVariants);

        Product product;
        if (productDTO.getId() == null) {
            product = new Product();
        } else {
            product = productService.getProductById(productDTO.getId()).get();
        }
        product.setName(productDTO.getName());
        product.setCategory(categoryService.findCategoryById(productDTO.getCategoryId()).get());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setUnitsInStock(productDTO.getUnitsInStock());
        product.setVariants(productDTO.getSelectedVariants());
        product.setUnitsInStock(productDTO.getUnitsInStock());

        if (!files.get(0).isEmpty()) {
            String imageUUID = files.get(0).getOriginalFilename();
            Path fileNamePath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNamePath, files.get(0).getBytes());

            product.setImageName(imageUUID);

            List<ProductImage> imageList = new ArrayList<>();

            for (int i = 1; i < files.size(); i++) {
                imageUUID = files.get(i).getOriginalFilename();
                fileNamePath = Paths.get(uploadDir, imageUUID);
                Files.write(fileNamePath, files.get(i).getBytes());
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setProductImage(imageUUID);
                imageList.add(productImage);
            }

            product.setImages(imageList);
        }

        if (selectedVariantIds != null) {
            List<ProductVariant> productVariantList = new ArrayList<>();
            for (int i = 0; i < selectedVariantIds.size(); i++) {

                ProductVariant productVariant = new ProductVariant();
                Variants variants1 = variantsRepository.findById(selectedVariantIds.get(i)).get();
                if (variants1 != null) {
                    productVariant.setSize(variants1);
                    productVariant.setProduct(product);
                    productVariant.setPrice(productDTO.getPrice());
                    productVariantList.add(productVariant);
                }
            }
            product.setVariants(productVariantList);
        } else {
            ProductVariant productVariant = new ProductVariant();
            productVariant.setProduct(product);
        }


        productService.saveProduct(product);

        return "redirect:/admin/products";
    }


    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.removeProductById(id);
        productImageService.removeImagesByProductId(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable Long id,Model model){
        Product product = productService.getProductById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        ProductDTO productDTO=new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(Math.toIntExact(product.getCategory().getId()));
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName((product.getImageName()));
        productDTO.setUnitsInStock(product.getUnitsInStock());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",productDTO);
        return "UpdateProduct";

    }

    @PostMapping("/admin/UpdateProduct")
    public String productUpdatePost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage") List<MultipartFile> files,
                                 @RequestParam("imgName") String imgName,
                                 @RequestParam(name = "variants", required = false) List<String> variants,
                                 @RequestParam(name = "selectedVariants", required = false) List<Long> selectedVariantIds,
                                 Model model) throws IOException {

        Product product;
        if (productDTO.getId() == null) {
            product = new Product();
        } else {
            product = productService.getProductById(productDTO.getId()).get();
        }
        product.setName(productDTO.getName());
        product.setCategory(categoryService.findCategoryById(productDTO.getCategoryId()).get());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setUnitsInStock(productDTO.getUnitsInStock());
        product.setVariants(productDTO.getSelectedVariants());

        if (!files.get(0).isEmpty()) {
            String imageUUID = files.get(0).getOriginalFilename();
            Path fileNamePath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNamePath, files.get(0).getBytes());

            product.setImageName(imageUUID);

            List<ProductImage> imageList = new ArrayList<>();

            for (int i = 1; i < files.size(); i++) {
                imageUUID = files.get(i).getOriginalFilename();
                fileNamePath = Paths.get(uploadDir, imageUUID);
                Files.write(fileNamePath, files.get(i).getBytes());
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setProductImage(imageUUID);
                imageList.add(productImage);
            }

            product.setImages(imageList);
        }

        if (selectedVariantIds != null) {
            List<ProductVariant> productVariantList = new ArrayList<>();
            for (int i = 0; i < selectedVariantIds.size(); i++) {

                ProductVariant productVariant = new ProductVariant();
                Variants variants1=variantsRepository.findById(selectedVariantIds.get(i)).get();
                productVariant.setSize(variants1);
                productVariant.setProduct(product);
                productVariant.setPrice(productDTO.getPrice());
                productVariantList.add(productVariant);
            }
            product.setVariants(productVariantList);
        } else {
            ProductVariant productVariant = new ProductVariant();
            productVariant.setProduct(product);
        }

        product.setUnitsInStock(productDTO.getUnitsInStock());
        productService.saveProduct(product);

        return "redirect:/admin/products";
    }






}