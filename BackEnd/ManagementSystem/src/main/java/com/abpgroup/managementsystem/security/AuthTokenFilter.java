package com.abpgroup.managementsystem.security;

import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtil;
    private final UsersRepository usersRepository;
    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                String clientToken = headerAuth.substring(7);

                if (jwtUtil.verifyJwtToken(clientToken)) {
                    // Mendapatkan informasi pengguna dari token
                    Map<String, String> userInfo = jwtUtil.getUserInfoByToken(clientToken);

                    // Memastikan pengguna ada di database
                    Optional<Users> userData = usersRepository.findById(Long.parseLong(userInfo.get("idUser")));
                    if (userData.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: User not found");
                        return;
                    }

                    Users user = userData.get();

                    // Membuat GrantedAuthority untuk role pengguna
                    List<GrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority(user.getRole().name())
                    );

                    // Membuat UserDetails dengan GrantedAuthority
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            authorities
                    );

                    // Membuat Authentication Token
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    // Menambahkan detail permintaan
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Menambahkan Authentication ke SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    // Menambahkan atribut pengguna ke request (opsional)
                    request.setAttribute("idUser", userInfo.get("idUser"));

                    // Debugging (Opsional)
                    logger.info("Authenticated user: {}, Authorities: {}", user.getEmail(), authorities);
                }
            }
        } catch (JWTVerificationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Invalid token");
            logger.error("JWT verification failed: {}", e.getMessage());
            return;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: " + e.getMessage());
            logger.error("Unexpected error: {}", e.getMessage());
            return;
        }

        // Lanjutkan filter chain
        filterChain.doFilter(request, response);
    }


}
