package com.order.food;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodController
{
    @GetMapping("/")
    public String testAPI()
    {
        return "Hello World";
    }
}
