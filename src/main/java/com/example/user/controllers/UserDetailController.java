package com.example.user.controllers;

import com.example.user.dtos.UserDTO;
import com.example.user.dtos.UserResponseDTO;
import com.example.user.entities.User;
import com.example.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserDetailController {
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public UserResponseDTO findByUsername(@PathVariable String username) {
        try {
            UserDTO user = userService.findByUsername(username);

            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setData(user);
            userResponseDTO.setStatus(200);
            return userResponseDTO;
        } catch (Exception e) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setData(null);
            userResponseDTO.setStatus(500);
            return userResponseDTO;
        }
    }
}
