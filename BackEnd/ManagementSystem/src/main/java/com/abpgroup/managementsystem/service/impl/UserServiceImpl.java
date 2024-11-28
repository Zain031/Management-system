package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.UserService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

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
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (userRequest.getName() == null || userRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (userRequest.getRole() == null || userRequest.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }

        if (!isValidRole(userRequest.getRole())) {
            throw new IllegalArgumentException("Invalid role specified, must be SUPER_ADMIN or ADMIN");
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

    @Override
    public Page<UsersResponseDTO> getUserByName(String name, Pageable pageable) {
        Page<Users> users = userRepository.getUsersByName(name, pageable);
        return users.map(this::convertToResponse);
    }

    @Override
    public byte[] generatedPdf(List<Users> users) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Title
            Paragraph title = new Paragraph("Users Data Export")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add empty line for spacing
            document.add(new Paragraph(" "));

            // Define table column widths
            float[] columnWidths = {1, 3, 4, 3}; // Adjust column widths
            Table table = new Table(columnWidths);

            // Add Table Header with styling
            table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Email").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Role").setBold()));

            // Add Table Data
            int index = 1;
            for (Users user : users) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))));
                table.addCell(new Cell().add(new Paragraph(user.getName())));
                table.addCell(new Cell().add(new Paragraph(user.getEmail())));
                table.addCell(new Cell().add(new Paragraph(user.getRole().name())));
            }

            // Add table to the document
            document.add(table);

            // Close document
            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
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
