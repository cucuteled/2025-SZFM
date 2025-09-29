package com.project.sfm2025.auth;

import com.project.sfm2025.auth.AuthenticationResponse;
import com.project.sfm2025.auth.AuthenticationRequest;
import com.project.sfm2025.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse resp = service.register(request);
        // create httpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", resp.getToken())
                .httpOnly(true)
                .secure(false) // dev: false; éles környezetben true (HTTPS)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 nap (másold igény szerint)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resp);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse resp = service.authenticate(request);
        ResponseCookie cookie = ResponseCookie.from("jwt", resp.getToken())
                .httpOnly(true)
                .secure(false) // dev -> false; élesen true
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resp);
    }
}
