package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.RegisterRepository;
import com.naiki.ecommerce.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final RegisterRepository registerRepository;

    @Autowired
    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    // Registro de un nuevo usuario
    public String registerUser(User user) {
        if (registerRepository.findByUsername(user.getUsername()) != null) {
            return "El nombre "+ user.getUsername() + " no está disponible.";
        }
        if (registerRepository.findByEmail(user.getEmail()) != null) {
            return "El correo electrónico " + user.getEmail() + " ya se encuentra registrado.";
        }
        registerRepository.saveUser(user);
        return user.getUsername() + " tu cuenta se creó con éxito!";
    }
}

