package com.naiki.ecommerce.controllers.user;

import dto.CarritoInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserData {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private List<CarritoInfo> carritos;
}
