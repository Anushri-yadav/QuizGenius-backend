package com.QuizGen.Master.controller;

import com.QuizGen.Master.dto.LoginRequest;
import com.QuizGen.Master.dto.LoginResponse;
import com.QuizGen.Master.dto.RegisterRequest;
import com.QuizGen.Master.model.User;
import com.QuizGen.Master.security.JwtService;
import com.QuizGen.Master.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        try {

            User user =
                    userService.registerUser(request);

            return ResponseEntity.ok(
                    "Registration successful"
            );

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String token =
                    jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    new LoginResponse(
                            token,
                            userDetails.getUsername()
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password");
        }
    }
}