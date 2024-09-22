package com.naiki.ecommerce.service;

import com.naiki.ecommerce.controllers.user.UserData;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserData getUserById(Long id) throws Exception {

        var userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id).orElseThrow(() -> new Exception("El usuario no se ha encontrado."));
        return new UserData(user.getId(),user.getFirstName(), user.getFirstName(), user.getEmail());
    }

    public UserData getAuthenticatedUserProfile() throws Exception {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new Exception("El Usuario no se ha encontrado."));
        return new UserData(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

}
