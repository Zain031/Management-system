package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.LoginRequestDTO;
import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.LoginResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.security.JWTUtils;
import com.abpgroup.managementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser loadUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return AppUser.builder()
                .id(user.getIdUser())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(Users.Role.valueOf(user.getRole().name()))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return AppUser.builder()
                .id(user.getIdUser())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    @Override
    public UsersResponseDTO register(UsersRequestDTO userRequest) {
        if (!isValidRole(userRequest.getRole())) {
            throw new IllegalArgumentException("Invalid role specified");
        }

        Users user = Users.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword())) // Encrypt password
                .name(userRequest.getName())
                .role(Users.Role.valueOf(userRequest.getRole()))
                .build();

        userRepository.save(user);
        return convertToResponse(user);
    }

    @Override
    public UsersResponseDTO update(Long id, UsersRequestDTO userRequest) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());

        if (isValidRole(userRequest.getRole())) {
            user.setRole(Users.Role.valueOf(userRequest.getRole()));
        }

        userRepository.save(user);
        return convertToResponse(user);
    }

    @Override
    public UsersResponseDTO delete(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        return convertToResponse(user);
    }

    @Override
    public UsersResponseDTO findById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToResponse(user);
    }

    @Override
    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(this::convertToResponse).toList();
    }

    private UsersResponseDTO convertToResponse(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    private boolean isValidRole(String role) {
        try {
            Users.Role.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
