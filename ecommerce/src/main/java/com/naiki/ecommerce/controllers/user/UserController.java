package com.naiki.ecommerce.controllers.user;

import com.naiki.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUser(@PathVariable Long id) throws Exception {
        UserData user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}

