package com.example.user.dtos;

import com.example.user.entities.User;

public class UserResponseDTO {
    private Integer status;
    private UserDTO data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserDTO getData() {
        return data;
    }

    public void setData(UserDTO data) {
        this.data = data;
    }
}
