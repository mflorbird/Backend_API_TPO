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




//    private final UserService userService;
//    private final AuthenticationManager authenticationManager;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
//        this.userService = userService;
//        this.authenticationManager = authenticationManager;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // Endpoint para registrar un nuevo usuario
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody User user) {
//        try {
//            userService.registerUser(user);
//            return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // Endpoint para hacer login
//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestParam("usernameOrEmail") String usernameOrEmail,
//                                            @RequestParam("password") String password) {
//        try {
//            // Autenticar al usuario
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
//            );
//
//            // Si la autenticación es exitosa, guardamos el contexto de seguridad
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            return new ResponseEntity<>("Login exitoso para el usuario: " + userDetails.getUsername(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Nombre de usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
//        }
//    }


}

