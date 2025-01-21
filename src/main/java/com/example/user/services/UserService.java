package com.example.user.services;

import com.example.user.entities.User;
import com.example.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        Optional<User> userOptionalEmail = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent() || userOptionalEmail.isPresent()) {
            throw new RuntimeException("User exist");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }


}