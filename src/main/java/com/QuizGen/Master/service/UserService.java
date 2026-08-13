package com.QuizGen.Master.service;

import com.QuizGen.Master.dto.RegisterRequest;
import com.QuizGen.Master.model.Role;
import com.QuizGen.Master.model.User;
import com.QuizGen.Master.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        User user = new User();

        user.setUsername(
                request.getUsername()
        );

        // NEVER store plain password
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Every new user is USER
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
}