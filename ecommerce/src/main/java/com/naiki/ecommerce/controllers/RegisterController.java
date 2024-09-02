package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.service.RegisterService;
import com.naiki.ecommerce.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/register")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // Registrar un nuevo usuario
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return registerService.registerUser(user);
    }
}
