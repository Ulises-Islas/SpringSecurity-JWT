package me.ulises.demo_jwt.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ulises.demo_jwt.auth.common.AuthResponse;
import me.ulises.demo_jwt.auth.common.LoginRequest;
import me.ulises.demo_jwt.auth.common.RegisterRequest;
import me.ulises.demo_jwt.config.jwt.service.JwtService;
import me.ulises.demo_jwt.entity.Role;
import me.ulises.demo_jwt.entity.User;
import me.ulises.demo_jwt.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository USER_REPOSITORY;
    private final JwtService JWT_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final AuthenticationManager AUTHENTICATION_MANAGER;

    public AuthResponse login(LoginRequest request) {
        AUTHENTICATION_MANAGER.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = USER_REPOSITORY.findByUsername(request.getUsername()).orElseThrow();
        String Token = JWT_SERVICE.getToken(user);
        return AuthResponse.builder()
                .token(Token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(PASSWORD_ENCODER.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        USER_REPOSITORY.save(user);
        return AuthResponse.builder()
                .token(JWT_SERVICE.getToken(user))
                .build();
    }
    
}
