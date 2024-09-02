package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class LoginRepository {

    // Simulación de una base de datos en memoria usando un HashMap
    private final Map<String, User> userDatabase = new HashMap<>();

    // Método para agregar un usuario (esto es solo para fines de simulación)
    public void saveUser(User user) {
        userDatabase.put(user.getUsername(), user);
    }

    // Método para encontrar un usuario por su nombre de usuario
    public User findByUsername(String username) {
        return userDatabase.get(username);
    }

    // Método para encontrar un usuario por su correo electrónico
    public User findByEmail(String email) {
        for (User user : userDatabase.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}


