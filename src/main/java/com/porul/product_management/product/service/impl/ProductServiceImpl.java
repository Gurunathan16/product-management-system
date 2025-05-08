package com.porul.product_management.product.service.impl;

import com.porul.product_management.util.response.ResponseEntityHandler;
import com.porul.product_management.product.dto.ProductInfo;
import com.porul.product_management.product.dto.ProductProjection;
import com.porul.product_management.product.dto.ProductRegistration;
import com.porul.product_management.product.dto.ProductUpdate;
import com.porul.product_management.product.service.ProductService;
import com.porul.product_management.util.mapper.ProductMappers;
import com.porul.product_management.product.entity.Product;
import com.porul.product_management.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMappers productMappers;

    public ProductServiceImpl(ProductRepository productRepository, ProductMappers productMappers)
    {
        this.productRepository = productRepository;
        this.productMappers = productMappers;
    }

    @Override
    public ResponseEntity<Map<String, Object>> saveProduct(ProductRegistration productRegistration)
    {
        if(productRepository.isProductExists(getUsername(), productRegistration.getProductName(),
                productRegistration.getCompany(),
                productRegistration.getUsageLocation()))
        {
            return ResponseEntityHandler.getResponseEntity(HttpStatus.CONFLICT, "Product Already Exists.",
                    "Recovery", "Product with same name under same company and usage location already exists. You are" +
                            " trying to add existing product. If not, try with different product name.");
        }

        Product product = productMappers.productRegistrationToProduct(productRegistration);
        product.setUsername(getUsername());

        productRepository.save(product);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.CREATED, "Product added Successfully.",
                "Details", productRegistration.getProductName());
    }

    @Override
    public ResponseEntity<Map<String, Object>> viewProducts(Pageable pageable)
    {
        Page<Product> products = productRepository.findAllByUsername(getUsername(), pageable);

        if(products == null || products.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found.", "Recovery",
                    "Add products by clicking the '+' Button.");

        Page<ProductProjection> productsDisplay =
                products.map(productMappers::productToProductProjection);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Products fetched Successfully.","Details", productsDisplay);
    }

    @Override
    public ResponseEntity<Map<String, Object>> productUpdate(ProductUpdate productUpdate)
    {
        Product product = productRepository.findByIdAndUsername(productUpdate.getId(), getUsername());

        if(product == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found.", "Recovery",
                    "Add products by clicking the '+' Button.");

        productRepository.save(productMappers.productUpdateToProduct(productUpdate, product));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Product Details Updated " +
                "Successfully.", "Details", productUpdate.getProductName());
    }

    @Transactional
    @Override
    public ResponseEntity<String> productDelete(ProductInfo productInfo)
    {
        Integer result = productRepository.deleteByIdAndUsername(productInfo.id(), getUsername());

        if(result != null && result > 0)
            return ResponseEntity.ok("Product Deleted Successfully.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not found.");
    }

    @Override
    public ResponseEntity<Map<String, Object>> categoriesFetch(Pageable pageable)
    {
        Page<String> categories = productRepository.findUniqueCategories(getUsername(), pageable);

        if(categories == null || categories.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found. So, no " +
                            "categories found.", "Recovery", "Add products by clicking the '+' Button.");

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Distinct Categories listed.",
                "Details", categories);
    }

    @Override
    public ResponseEntity<Map<String, Object>> companiesFetch(Pageable pageable)
    {
        Page<String> companies = productRepository.findUniqueCompanies(getUsername(), pageable);

        if(companies == null || companies.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found. So, no companies" +
                            " found.", "Recovery", "Add products by clicking the '+' Button.");

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Distinct Categories listed.",
                "Details", companies);
    }

    @Override
    public ResponseEntity<Map<String, Object>> usageLocationsFetch(Pageable pageable)
    {
        Page<String> usageLocation = productRepository.findUniqueUsageLocation(getUsername(), pageable);

        if(usageLocation == null || usageLocation.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found. So, no usage " +
                            "location found.", "Recovery", "Add products by clicking the '+' Button.");

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Distinct usage locations listed.",
                "Details", usageLocation);
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchProductsByCategory(String category, Pageable pageable)
    {
        if(category == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Category cannot be null" +
                    ".", "Recovery", "Check category name or add product.");

        Page<Product> products = productRepository.getProductsByCategory(category, getUsername(), pageable);

        if(products == null || products.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found under " +
                    "this category.","Recovery", "Add products by clicking the '+' Button.");

        Page<ProductProjection> productProjections = products.map(productMappers::productToProductProjection);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK,
                "Products by category fetched " +
                "successfully.", "Details", productProjections);
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchProductsByCompany(String company, Pageable pageable)
    {
        if(company == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Company cannot be null" +
                    ".", "Recovery", "Check company name or add product.");

        Page<Product> products = productRepository.getProductsByCompany(company, getUsername(), pageable);

        if(products == null || products.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found under " +
                    "this company.","Recovery", "Add products by clicking the '+' Button.");

        Page<ProductProjection> productProjections = products.map(productMappers::productToProductProjection);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Products by company fetched " +
                "successfully.", "Details", productProjections);
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchProductsByUsageLocation(String usageLocation, Pageable pageable)
    {
        if(usageLocation == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Usage Location cannot be" +
                    " null.", "Recovery", "Check usage location or add product.");

        Page<Product> products = productRepository.getProductsByUsageLocation(usageLocation, getUsername(),
                pageable);

        if(products == null || products.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No Products found under " +
                    "this usage location.","Recovery", "Add products by clicking the '+' Button.");

        Page<ProductProjection> productProjections = products.map(productMappers::productToProductProjection);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Products by company fetched " +
                "successfully.", "Details", productProjections);
    }

    @Override
    public ResponseEntity<Map<String, Object>> reminderSwitch(ProductInfo productInfo)
    {
        Product product = productRepository.findByIdAndUsername(productInfo.id(), getUsername());

        if(product == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "Product not found.",
                    "Recovery", "Add products by clicking the '+' Button.");

        Boolean res = product.getReminderEnabled();

        if(res)
        {
            product.setReminderEnabled(false);
            productRepository.save(product);

            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Reminder turned off " +
                            "successfully.", "Details", product.getProductName());
        }
        else
        {
            product.setReminderEnabled(true);
            productRepository.save(product);

            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Reminder turned on " +
                    "successfully.", "Details", product.getProductName());
        }
    }

    @Override
    public String getUsername()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

}
