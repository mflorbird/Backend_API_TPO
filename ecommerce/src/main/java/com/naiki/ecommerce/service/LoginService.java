package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.LoginRepository;
import com.naiki.ecommerce.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    // Autenticar un usuario por su nombre de usuario y contraseña
    public String authenticateByUsername(String username, String password) {
        User user = loginRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "Login exitoso para el usuario: " + username;
        }
        return "Login fallido: nombre de usuario o contraseña incorrectos";
    }

    // Autenticar un usuario por su correo electrónico y contraseña
    public String authenticateByEmail(String email, String password) {
        User user = loginRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return "Login exitoso para el correo: " + email;
        }
        return "Login fallido: correo electrónico o contraseña incorrectos";
    }


}
