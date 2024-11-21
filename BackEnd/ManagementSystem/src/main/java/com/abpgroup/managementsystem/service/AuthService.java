package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.LoginRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
