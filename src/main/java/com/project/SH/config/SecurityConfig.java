package com.project.SH.config;

import com.project.SH.service.impl.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
public class SecurityConfig {

    private final UserDetailService userDetailsService;
    private final JwtAuthenticationFilter jwtTokenFilter;

    public SecurityConfig(UserDetailService userDetailsService, JwtAuthenticationFilter jwtTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.OPTIONS, "/api/login").permitAll()
//                        .requestMatchers("/api/login").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/tales/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/tales/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/tales/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/tales/**").hasRole("ADMIN")
//                        .requestMatchers("/api/tales/*/like").hasAnyRole("CONSUMER", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
//                        .requestMatchers("/api/users/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
                // Отключаем httpBasic, т.к. используем JWT
                .httpBasic(AbstractHttpConfigurer::disable)
                // Отключаем сессии — JWT статeless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Добавляем фильтр JWT перед стандартным фильтром аутентификации
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}