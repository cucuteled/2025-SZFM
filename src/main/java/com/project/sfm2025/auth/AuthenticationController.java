package com.project.sfm2025.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        ResponseCookie cookie = ResponseCookie.from("jwt", resp.getToken())
                .httpOnly(true)
                .secure(false) // fejlesztés: false; éles környezetben true (HTTPS)
                .path("/")
                .maxAge(24 * 60 * 60)
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
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resp);
    }

    // --- visszaadja a bejelentkezett felhasználó alapadatait (ha be van jelentkezve) ---
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        // authentication.getPrincipal() általában UserDetails (a te User osztályod)
        Object principal = authentication.getPrincipal();
        return ResponseEntity.ok(principal);
    }

    // --- logout (cookie törlése) ---
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
