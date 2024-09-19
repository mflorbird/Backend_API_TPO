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
        UserData user;
        user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserData> getAuthenticatedUserProfile() throws Exception {
        UserData user;
        user = userService.getAuthenticatedUserProfile();
        return ResponseEntity.ok(user);
    }


}

