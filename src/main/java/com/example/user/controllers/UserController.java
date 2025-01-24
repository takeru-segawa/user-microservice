package com.example.user.controllers;

import com.example.user.dtos.AuthResponse;
import com.example.user.entities.User;
import com.example.user.services.UserService;
import com.example.user.springjwt.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(AuthenticationManager authenticationManager, JwtUtil jwtUtil ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        try {
            userService.register(user);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setStatus(HttpStatus.OK.value());
            authResponse.setMessage("User registered successfully");
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }
        catch (Exception e) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            authResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(authResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User authRequest) {
        try {
            System.out.println("Login attempt for username: " + authRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(authRequest.getUsername());

            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "username", authRequest.getUsername()
                    )
            );
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

//    @GetMapping("/{username}")
//    public String getUser(@PathVariable String username) {
//        return userService.findByUsername(username).getId();
//    }
}
