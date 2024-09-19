package com.naiki.ecommerce.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserData {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
}
