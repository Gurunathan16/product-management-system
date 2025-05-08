package com.porul.product_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class ProductManagementApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(ProductManagementApplication.class, args);
	}
}
