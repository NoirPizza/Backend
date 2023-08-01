package com.pizza.pizzashop;

import com.pizza.pizzashop.utils.GlobalLogger.GlobalLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PizzaShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaShopApplication.class, args);
        GlobalLogger.log("INFO", "Application started");
    }

}
