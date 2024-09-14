package com.naiki.ecommerce.controllers.user;

import com.naiki.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUser(@PathVariable Long id) throws Exception {
        UserData user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}

