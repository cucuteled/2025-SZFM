package com.project.sfm2025.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/api/v1/auth/**",
                                "/register",
                                "/login",
                                "/pictures/**",
                                "/etelek", "/etelek/**",
                                "/italok", "/italok/**",
                                "/menuk", "/menuk/**",
                                "/index",
                                "/index.html",
                                "/css/**",
                                "/?continue",
                                "/api/contact",
                                "/favicon.ico"
                        ).permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/elado/**").hasRole("ELADO")
//                        .requestMatchers("/api/v1/coupons/**"
//                        ).authenticated() // ez nem kell mert denyAll ami nincs a kivételben
                        .requestMatchers( // tiltani!
                                "/data/**"
                        ).denyAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                //.formLogin(login -> login
                //       .loginPage("/login")
                //        .permitAll()
                //)
                //.logout(logout -> logout.permitAll());

        return http.build();
    }
}
