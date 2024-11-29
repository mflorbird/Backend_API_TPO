package com.naiki.ecommerce.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("usuario")
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty("contraseña")
    private String password;

    @Column(nullable = false)
    @JsonProperty("nombre")
    private String firstName;

    @Column(nullable = false)
    @JsonProperty("apellido")
    private String lastName;

    @Column(nullable = false)
    @JsonProperty("fechaNacimiento")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> favoritos = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> visitados = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {

        return this.email;
    }

}
