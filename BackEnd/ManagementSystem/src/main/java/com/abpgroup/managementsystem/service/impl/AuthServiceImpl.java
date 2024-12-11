package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.LoginRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.LoginResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.security.JWTUtils;
import com.abpgroup.managementsystem.service.AuthService;
import com.abpgroup.managementsystem.utils.CapitalizeFirstLetter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UsersRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        CapitalizeFirstLetter.capitalizeFirstLetter(loginRequestDTO.getEmail()),
                        loginRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Users user = userRepository.findByEmail(CapitalizeFirstLetter.capitalizeFirstLetter(loginRequestDTO.getEmail()));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String token = jwtUtils.generateToken(AppUser.builder()
                .id(user.getIdUser())
                .email(user.getEmail())
                .role(user.getRole())
                .build(),
                Users.builder()
                        .idUser(user.getIdUser())
                        .name(user.getName())
                        .build());

        return LoginResponseDTO.builder()
                .token(token)
                .user(convertToResponse(user))
                .build();
    }

    private UsersResponseDTO convertToResponse(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
