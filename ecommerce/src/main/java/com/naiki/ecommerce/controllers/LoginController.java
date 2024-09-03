package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // Autenticar usuario por nombre de usuario
    @PostMapping("/authenticateByUsername")
    public String authenticateByUsername(@RequestParam String username, @RequestParam String password) {
        return loginService.authenticateByUsername(username, password);
    }

    // Autenticar usuario por correo electrónico
    @PostMapping("/authenticateByEmail")
    public String authenticateByEmail(@RequestParam String email, @RequestParam String password) {
        return loginService.authenticateByEmail(email, password);
    }

}







