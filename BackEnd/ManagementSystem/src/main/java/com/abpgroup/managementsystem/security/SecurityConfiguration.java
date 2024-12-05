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
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthTokenFilter authTokenFilter;

    private final String [] WHITE_LIST_URL={
            APIUrl.BASE_URL_USER+"/**",
            APIUrl.BASE_URL_PAYMENT+"/**",
            APIUrl.BASE_URL_PURCHASE+"/**",
            APIUrl.BASE_URL_BUSINESS_PERFORMANCE+"/**",
            APIUrl.BASE_URL_PRODUCT+"/**",
            APIUrl.BASE_URL_INVENTORY+"/**",
            APIUrl.BASE_URL_ORDERS+"/**",
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
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL).permitAll()
//                        .requestMatchers(APIUrl.BASE_URL_USER+"/register").hasAnyAuthority("SUPER_ADMIN")
//                        .requestMatchers(HttpMethod.GET,APIUrl.BASE_URL_USER+"/users").hasAnyAuthority("SUPER_ADMIN")
//                        .requestMatchers(APIUrl.BASE_URL_USER+"/update/**").hasAnyAuthority("SUPER_ADMIN")
//                        .requestMatchers(HttpMethod.GET,APIUrl.BASE_URL_USER+"/user/**").hasAnyAuthority("SUPER_ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,APIUrl.BASE_URL_USER+"/user/**").hasAnyAuthority("SUPER_ADMIN")
//                        .requestMatchers(APIUrl.BASE_URL_PRODUCT).hasAnyAuthority("SUPER_ADMIN","ADMIN")
//                        .requestMatchers(HttpMethod.GET,APIUrl.BASE_URL_PRODUCT+"/category/**").hasAnyAuthority("SUPER_ADMIN","ADMIN")
//                        .requestMatchers(APIUrl.BASE_URL_PRODUCT+"/create").hasAnyAuthority("SUPER_ADMIN","ADMIN")
//                        .requestMatchers(APIUrl.BASE_URL_PRODUCT+"/update/**").hasAnyAuthority("SUPER_ADMIN","ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,APIUrl.BASE_URL_PRODUCT+"/product/delete/**").hasAnyAuthority("SUPER_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
