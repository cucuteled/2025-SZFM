package com.project.sfm2025.auth;

import com.project.sfm2025.entities.Coupon;
import com.project.sfm2025.entities.Role;
import com.project.sfm2025.entities.User;
import com.project.sfm2025.repositories.UserRepository;
import com.project.sfm2025.security.JwtService;
import com.project.sfm2025.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CouponRepository couponRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        // --- Regisztrációkór kupon létrehozása ---
        Coupon welcomeCoupon = new Coupon(
                "WELCOME1000", //kupon kod
                1000, //kedvezmeny
                user.getUsername(),
                LocalDateTime.now().plusDays(30) // 30 napig érvényes!
        );
        couponRepository.save(welcomeCoupon); // mentjük az adatbázisba

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
