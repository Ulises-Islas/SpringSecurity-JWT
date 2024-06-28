package me.ulises.demo_jwt.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ulises.demo_jwt.auth.common.AuthResponse;
import me.ulises.demo_jwt.auth.common.LoginRequest;
import me.ulises.demo_jwt.auth.common.RegisterRequest;
import me.ulises.demo_jwt.auth.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService AUTH_SERVICE;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(AUTH_SERVICE.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(AUTH_SERVICE.register(request));
    }
    
}
