package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.UserService;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    @Transactional
    public UsersResponseDTO register(UsersRequestDTO userRequest) {
        // Validasi input
        validateUserRequest(userRequest);

        // Jika validasi berhasil, buat entitas pengguna
        Users user = Users.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword())) // Encrypt password
                .name(userRequest.getName())
                .role(Users.Role.valueOf(userRequest.getRole()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Simpan pengguna ke database
        userRepository.save(user);

        // Konversi dan kembalikan respons
        return convertToResponse(user);
    }

    // Metode validasi terpisah untuk menjaga kerapian kode
    private void validateUserRequest(UsersRequestDTO userRequest) {
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!isValidEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
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
    }


    private boolean isValidEmail(@Email(message = "Invalid email format") @NotBlank(message = "Email cannot be blank") String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    @Override
    public UsersResponseDTO update(Long id, UsersRequestDTO userRequest) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!isValidEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
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

        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        if (isValidRole(userRequest.getRole())) {
            user.setRole(Users.Role.valueOf(userRequest.getRole()));
        }

        user.setUpdatedAt(LocalDateTime.now());

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
    public Page<UsersResponseDTO> getAllUsers(Pageable pageable) {
        Page<Users> users = userRepository.findAll(pageable);
        return users.map(this::convertToResponse);
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
            PageSize pageSize = PageSize.A4.rotate();
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);

            // Set page size
            pdf.setDefaultPageSize(pageSize);
            Document document = new Document(pdf);
            // Set document properties
            document.setMargins(20, 20, 20, 20);

            // Add Title
            Paragraph title = new Paragraph("Users Data Export")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add empty line for spacing
            document.add(new Paragraph(" "));

            // Define table column widths
            float[] columnWidths = {1f, 3f, 3f, 3f, 3f, 3f};
            Table table = new Table(columnWidths);

            // Add Table Header with styling
            table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Email").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Role").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Created At").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Updated At").setBold()));

            // Add Table Data
            int index = 1;
            for (Users user : users) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))));
                table.addCell(new Cell().add(new Paragraph(user.getName())));
                table.addCell(new Cell().add(new Paragraph(user.getEmail())));
                table.addCell(new Cell().add(new Paragraph(user.getRole().name())));
                table.addCell(new Cell().add(new Paragraph(user.getCreatedAt().toString())));
                table.addCell(new Cell().add(new Paragraph(user.getUpdatedAt().toString())));
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
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
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
