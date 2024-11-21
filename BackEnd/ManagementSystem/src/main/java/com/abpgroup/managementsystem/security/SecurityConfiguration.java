package com.abpgroup.managementsystem.security;

import com.abpgroup.managementsystem.constant.APIUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthTokenFilter authTokenFilter;

    private final String [] WHITE_LIST_URL={
            APIUrl.BASE_URL_USER+"/**",
            APIUrl.BASE_URL_DRINK+"/**",
            APIUrl.BASE_URL_FOOD+"/**",
            APIUrl.BASE_URL_FOODSTUFF+"/**",
            APIUrl.BASE_URL_PURCHASE+"/**",
            APIUrl.BASE_URL_PROFIT+"/**",
            APIUrl.BASE_URL_SALES+"/**",
            APIUrl.BASE_URL_TOOL+"/**",
            "/swagger-ui/**",
            "/docs/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/v1/products/create").hasAnyAuthority("ADMIN", "SELLER")
                        .requestMatchers("/api/v1/products/delete-product/").hasAnyAuthority("ADMIN", "SELLER")
                        .requestMatchers("/api/v1/customers/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/products/get-all-product").hasAnyAuthority("CUSTOMER","ADMIN","SELLER")
                        .requestMatchers("/api/v1/transactions/**").hasAnyAuthority("ADMIN", "SELLER", "CUSTOMER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
