package com.abpgroup.managementsystem.security;

import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.UserService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtil;
//    private final UserService userService;
    private final UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                String clientToken = headerAuth.substring(7);

                if (jwtUtil.verifyJwtToken(clientToken)) {
                    Map<String, String> userInfo = jwtUtil.getUserInfoByToken(clientToken);
                    Optional<Users> userData = usersRepository.findById(Long.parseLong(userInfo.get("idUser")));
                    if (userData.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: User not found");
                        return;
                    }
                    UserDetails user = AppUser.builder()
                            .email(userData.get().getEmail())
                            .password(userData.get().getPassword())
                            .role(userData.get().getRole())
                            .build();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    request.setAttribute("idUser", userInfo.get("idUser"));
                }
            }
        } catch (JWTVerificationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
