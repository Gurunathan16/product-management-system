package com.porul.product_management.product.service;

import com.porul.product_management.product.dto.ProductInfo;
import com.porul.product_management.product.dto.ProductRegistration;
import com.porul.product_management.product.dto.ProductUpdate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {
    ResponseEntity<Map<String, Object>> saveProduct(ProductRegistration productRegistration);

    ResponseEntity<Map<String, Object>> viewProducts(Pageable pageable);

    ResponseEntity<Map<String, Object>> productUpdate(ProductUpdate productUpdate);

    @Transactional
    ResponseEntity<String> productDelete(ProductInfo productInfo);

    ResponseEntity<Map<String, Object>> categoriesFetch(Pageable pageable);

    ResponseEntity<Map<String, Object>> companiesFetch(Pageable pageable);

    ResponseEntity<Map<String, Object>> usageLocationsFetch(Pageable pageable);

    ResponseEntity<Map<String, Object>> fetchProductsByCategory(String category, Pageable pageable);

    ResponseEntity<Map<String, Object>> fetchProductsByCompany(String company, Pageable pageable);

    ResponseEntity<Map<String, Object>> fetchProductsByUsageLocation(String usageLocation, Pageable pageable);

    ResponseEntity<Map<String, Object>> reminderSwitch(ProductInfo productInfo);

    String getUsername();
}
