package com.porul.product_management.product.repository;

import com.porul.product_management.auth.repository.UsersRepository;
import com.porul.product_management.product.entity.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>
{
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Product AS p WHERE p.username = :username " +
            "AND LOWER(p.productName) = LOWER(:productName) AND LOWER(p.company) = LOWER(:company) AND " +
            "LOWER(p.usageLocation) = LOWER(:usageLocation)")
    boolean isProductExists(String username, String productName, String company, String usageLocation);

    Page<Product> findAllByUsername(String username, Pageable pageable);

    Integer deleteByIdAndUsername(Integer id, String username);

    Product findByIdAndUsername(Integer id, String username);

    @Query("SELECT DISTINCT category FROM Product WHERE username = :username")
    Page<String> findUniqueCategories(String username, Pageable pageable);

    @Query("SELECT DISTINCT company FROM Product WHERE username = :username")
    Page<String> findUniqueCompanies(String username, Pageable pageable);

    @Query("SELECT DISTINCT usageLocation FROM Product WHERE username = :username")
    Page<String> findUniqueUsageLocation(String username, Pageable pageable);

    @Query("SELECT p FROM Product AS p WHERE LOWER(category) = LOWER(:category) AND username = :username")
    Page<Product> getProductsByCategory(String category, String username, Pageable pageable);

    @Query("SELECT p FROM Product AS p WHERE LOWER(company) = LOWER(:company) AND username = :username")
    Page<Product> getProductsByCompany(String company, String username, Pageable pageable);

    @Query("SELECT p FROM Product AS p WHERE LOWER(usageLocation) = LOWER(:usageLocation) AND username = :username")
    Page<Product> getProductsByUsageLocation(String usageLocation, String username, Pageable pageable);

    @Query(value = "SELECT * FROM (SELECT *, DATE_ADD(date_of_purchase, INTERVAL warranty_period_in_months MONTH) AS " +
            "warranty_expiry_date FROM PRODUCT) AS P WHERE P.warranty_expiry_date <= CURDATE() +  INTERVAL 1 MONTH " +
            "AND P.warranty_expiry_date > CURDATE() AND P.reminder_enabled = true ORDER BY P.warranty_expiry_date", nativeQuery = true)
    List<Product> findProductsExpiringOneMonthFromNow();

}



