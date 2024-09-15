package com.naiki.ecommerce.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserData {
    private final Long id;
    private final String name;
    private final String email;
}
