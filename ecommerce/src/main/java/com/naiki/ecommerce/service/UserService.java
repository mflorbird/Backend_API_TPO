package com.naiki.ecommerce.service;

import com.naiki.ecommerce.controllers.user.UserData;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.Carrito;
import com.naiki.ecommerce.repository.entity.User;
import com.naiki.ecommerce.dto.CarritoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserService {
    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UserRepository userRepository;

    public UserData getUserById(Long id) throws Exception {

        var userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id).orElseThrow(() -> new Exception("El usuario no se ha encontrado."));
        return new UserData(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(), Collections.emptyList(), Collections.emptyList());
    }

    public UserData getAuthenticatedUserProfile() throws Exception {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new Exception("El Usuario no se ha encontrado."));

        List<Carrito> carritos = carritoService.obtenerCarritosPorUsuario(user.getId());

        List<CarritoInfo> carritoInfoList = carritos.stream()
                .map(carrito -> new CarritoInfo(carrito.getId(), (LocalDateTime) carrito.getFechaTransaccion()))
                .collect(Collectors.toList());

        UserData userData = new UserData(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),Collections.emptyList(), Collections.emptyList());
//        userData.setCarritos(carritoInfoList);

        return userData;

    }

}
