package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.LoginRequestDTO;
import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.LoginResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    AppUser loadUserById(Long id);
    UsersResponseDTO register(UsersRequestDTO user);
    UsersResponseDTO update(Long id, UsersRequestDTO user);
    UsersResponseDTO delete(Long id);
    UsersResponseDTO findById(Long id);
    List<UsersResponseDTO> getAllUsers();
}
