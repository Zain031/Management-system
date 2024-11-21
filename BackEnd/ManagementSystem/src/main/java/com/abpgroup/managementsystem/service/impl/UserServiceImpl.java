package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;

    @Override
    public AppUser loadUserById(Long id) {
        Users user=userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return AppUser.builder()
                .id(user.getIdUser())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(Users.Role.valueOf(user.getRole().name()))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UsersResponseDTO register(UsersRequestDTO user) {
        Users users = Users.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(Users.Role.valueOf(user.getRole()))
                .build();

        userRepository.save(users);
        return convertToResponse(users);
    }

    @Override
    public UsersResponseDTO update(Long id, UsersRequestDTO user) {
        Users users = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        users.setEmail(user.getEmail());
        users.setName(user.getName());
        users.setRole(Users.Role.valueOf(user.getRole()));
        userRepository.save(users);
        return convertToResponse(users);
    }

    @Override
    public UsersResponseDTO delete(Long id) {
        Users users = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        userRepository.delete(users);
        return convertToResponse(users);
    }

    @Override
    public UsersResponseDTO findById(Long id) {
        Users users = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return convertToResponse(users);
    }

    @Override
    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(this::convertToResponse).toList();
    }

    private UsersResponseDTO convertToResponse(Users users) {
        return UsersResponseDTO.builder()
                .idUser(users.getIdUser())
                .email(users.getEmail())
                .name(users.getName())
                .role(users.getRole().name())
                .build();
    }

}
