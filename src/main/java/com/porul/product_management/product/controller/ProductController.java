package com.porul.product_management.product.controller;

import com.porul.product_management.product.service.ProductService;
import com.porul.product_management.util.response.ResponseEntityHandler;
import com.porul.product_management.product.dto.ProductInfo;
import com.porul.product_management.product.dto.ProductRegistration;
import com.porul.product_management.product.dto.ProductUpdate;
import com.porul.product_management.product.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService)
    {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addProduct(@Valid @RequestBody ProductRegistration productRegistration
            , BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST,
                    "Validation Failed", "Validation Error",
                    bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));

        return productService.saveProduct(productRegistration);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProduct(@Valid @RequestBody ProductUpdate productUpdate,
                                                             BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Validation Failed.",
                    "Validation Error", bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));

        return productService.productUpdate(productUpdate);
    }

    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> getProducts(Pageable pageable)
    {
        return productService.viewProducts(pageable);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestBody ProductInfo productInfo)
    {
        return productService.productDelete(productInfo);
    }

    @GetMapping("/companies")
    public ResponseEntity<Map<String, Object>> getCompanies(Pageable pageable)
    {
        return productService.companiesFetch(pageable);
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories(Pageable pageable)
    {
        return productService.categoriesFetch(pageable);
    }

    @GetMapping("/usageLocations")
    public ResponseEntity<Map<String, Object>> getUsageLocations(Pageable pageable)
    {
        return productService.usageLocationsFetch(pageable);
    }

    @GetMapping("/productsByCategory")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(String category, Pageable pageable)
    {
        return productService.fetchProductsByCategory(category, pageable);
    }

    @GetMapping("/productsByCompany")
    public ResponseEntity<Map<String, Object>> getProductsByCompany(String company, Pageable pageable)
    {
        return productService.fetchProductsByCompany(company, pageable);
    }

    @GetMapping("/productsByUsageLocation")
    public ResponseEntity<Map<String, Object>> getProductsByUsageLocation(String usageLocation, Pageable pageable)
    {
        return productService.fetchProductsByUsageLocation(usageLocation, pageable);
    }

    @PostMapping("/switchReminder")
    public ResponseEntity<Map<String, Object>> turnReminderOnOrOff(@RequestBody ProductInfo productInfo)
    {
        return productService.reminderSwitch(productInfo);
    }

}
