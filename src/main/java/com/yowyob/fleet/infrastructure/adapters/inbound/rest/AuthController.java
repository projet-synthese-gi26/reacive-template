package com.yowyob.fleet.infrastructure.adapters.inbound.rest;

import com.yowyob.fleet.domain.ports.in.AuthUseCase;
import com.yowyob.fleet.domain.ports.out.AuthPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders; // IMPORT AJOUTÉ
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public Mono<AuthPort.AuthResponse> login(@RequestBody LoginRequest request) {
        return authUseCase.login(request.identifier(), request.password());
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription Fleet Management")
    public Mono<AuthPort.AuthResponse> register(@RequestBody RegisterDto dto) {
        return authUseCase.register(
            dto.username(), dto.password(), dto.email(), 
            dto.phone(), dto.firstName(), dto.lastName(), dto.roles()
        );
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile (No visible params)")
    public Mono<AuthPort.UserDetail> getCurrentUser(
        @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        return authUseCase.me(token);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public Mono<AuthPort.AuthResponse> refresh(@RequestBody TokenRefreshRequest request) {
        return authUseCase.refreshToken(request.refreshToken());
    }

    @PostMapping("/logout/{userId}")
    @Operation(summary = "Logout specific user (Only userId visible)")
    public Mono<Void> logout(
        @Parameter(name = "userId", description = "The ID of the user to logout", required = true) 
        @PathVariable UUID userId, 
        
        @Parameter(hidden = true) 
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        return authUseCase.logout(userId, token);
    }


    public record LoginRequest(String identifier, String password) {}
    
    public record RegisterDto(
        String username, 
        String password, 
        String email, 
        String phone, 
        String firstName, 
        String lastName,
        List<String> roles
    ) {}
    public record TokenRefreshRequest(String refreshToken) {}
    
}