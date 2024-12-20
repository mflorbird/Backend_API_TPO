package com.naiki.ecommerce.controllers.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naiki.ecommerce.repository.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty("nombre")
    private String firstname;
    @JsonProperty("apellido")
    private String lastname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("usuario")
    private String username;
    @JsonProperty("contraseña")
    private String password;
    @JsonProperty("fechaNacimiento")
    private LocalDate birthDate;
    @JsonProperty("role")
    private Role role;
}
