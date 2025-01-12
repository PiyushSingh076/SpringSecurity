package com.example.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {
    @GetMapping("/hello")
    public String greet() {
        return "Hello";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/admin")
    public String adminEndPoint(){
        return "Hello, Admin";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String userEndPoint(){
        return "Hello, user";
    }
}
