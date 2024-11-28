package com.naiki.ecommerce.service;

import com.naiki.ecommerce.exception.UserAlreadyExistsException;
import com.naiki.ecommerce.exception.UserNotFoundException;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.controllers.config.JwtService;
import com.naiki.ecommerce.controllers.auth.*;
import com.naiki.ecommerce.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario con el email " + request.getEmail() + " ya existe.");
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthDate())
                .role(request.getRole())
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user,
                                                user.getRole().name(),
                                                user.getFirstName(),
                                                user.getLastName(),
                                                user.getBirthDate().toString(),
                                                user.getFavoritos(),
                                                user.getVisitados());
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("El usuario con el email " + request.getEmail() + " no existe."));
        var jwtToken = jwtService.generateToken(user,
                                                user.getRole().name(),
                                                user.getFirstName(),
                                                user.getLastName(),
                                                user.getBirthDate().toString(),
                                                user.getFavoritos(),
                                                user.getVisitados());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }



}

