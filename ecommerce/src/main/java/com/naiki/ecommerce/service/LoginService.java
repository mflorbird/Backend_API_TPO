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
            return "Hola "+ username + ", te damos la bienvenida a Naiki.";
        }
        return "El usuario o la contraseña son incorrectos. Intenta nuevamente.";
    }

    // Autenticar un usuario por su correo electrónico y contraseña
    public String authenticateByEmail(String email, String password) {
        User user = loginRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return "Hola " + email + ", te damos la bienvenida a Naiki.";
        }
        return "El correo o la contraseña son incorrectos. Intenta nuevamente.";
    }


}
