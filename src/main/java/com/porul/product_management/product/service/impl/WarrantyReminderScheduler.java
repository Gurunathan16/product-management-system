package com.porul.product_management.product.service.impl;

import com.porul.product_management.auth.entity.Users;
import com.porul.product_management.auth.repository.UsersRepository;
import com.porul.product_management.product.entity.Product;
import com.porul.product_management.product.repository.ProductRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarrantyReminderScheduler {

    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;
    private final JavaMailSender javaMailSender;

    public WarrantyReminderScheduler(ProductRepository productRepository, UsersRepository usersRepository, JavaMailSender javaMailSender)
    {

        this.productRepository = productRepository;
        this.usersRepository = usersRepository;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(cron = "0 09 00 07 * *")
    public void sendWarrantyReminders() {
        List<Product> expiringProducts = productRepository.findProductsExpiringOneMonthFromNow();

        Map<String, List<Product>> userToProductsMap = expiringProducts.stream()
                .collect(Collectors.groupingBy(Product::getUsername));

        for (Map.Entry<String, List<Product>> entry : userToProductsMap.entrySet()) {
            String username = entry.getKey();
            List<Product> products = entry.getValue();

            Users users = usersRepository.findByUsername(username);

            if(users != null)
                sendGroupedWarrantyReminder(users.getMailId(), products);
        }
    }

    public void sendGroupedWarrantyReminder(String toEmail, List<Product> products) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Warranty Expiry Reminder");

            String content = buildHtmlContent(products);
            helper.setText(content, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildHtmlContent(List<Product> products) {
        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h3>Warranty Expiry Reminder</h3>");
        html.append("<p>The following products will expire in 1 month:</p>");
        html.append("<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse;'>");
        html.append("<tr>")
                .append("<th>Product Name</th>")
                .append("<th>Category</th>")
                .append("<th>Company</th>")
                .append("<th>Date of Purchase</th>")
                .append("<th>Usage Location</th>")
                .append("<th>Expiry Date</th>")
                .append("</tr>");

        for (Product product : products) {
            LocalDate expiryDate = product.getDateOfPurchase().plusMonths(product.getWarrantyPeriodInMonths());
            html.append("<tr>")
                    .append("<td>").append(product.getProductName()).append("</td>")
                    .append("<td>").append(product.getCategory()).append("</td>")
                    .append("<td>").append(product.getCompany()).append("</td>")
                    .append("<td>").append(product.getDateOfPurchase()).append("</td>")
                    .append("<td>").append(product.getUsageLocation()).append("</td>")
                    .append("<td>").append(expiryDate).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");
        html.append("<p>Please consider extending your warranty before the expiry date.</p>");
        html.append("<br><p>Regards,<br><b>Team Porul</b></p>");
        html.append("</body></html>");

        return html.toString();
    }
}