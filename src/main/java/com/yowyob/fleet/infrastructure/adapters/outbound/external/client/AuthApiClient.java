package com.yowyob.fleet.infrastructure.adapters.outbound.external.client;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import reactor.core.publisher.Mono;
import java.util.List;
import java.util.UUID;

@HttpExchange("/api/auth")
public interface AuthApiClient {
    
    @PostExchange("/login")
    Mono<TraMaSysResponse> authenticate(@RequestBody LoginRequest request);

    @PostExchange("/register")
    Mono<TraMaSysResponse> register(@RequestBody RegisterRequest request);

    
    @GetExchange("/me")
    Mono<UserDetailResponse> getCurrentUser(@RequestHeader("Authorization") String bearerToken);

    @PostExchange("/refresh")
    Mono<TraMaSysResponse> refreshToken(@RequestBody RefreshRequest request);

    @PostExchange("/logout")
    Mono<Void> logout(
        @PathVariable("userId") UUID userId, 
        @RequestHeader("Authorization") String bearerToken
    );

    record LoginRequest(String identifier, String password) {}

    record RefreshRequest(String refreshToken) {}
    
    record RegisterRequest(
        String username,
        String password,
        String email,
        String phone,
        String firstName,
        String lastName,
        String service, // Sera "FLEET_MANAGEMENT"
        List<String> roles
    ) {}

    record TraMaSysResponse(String accessToken, String refreshToken, UserDetailResponse user) {}
    record UserDetailResponse(
        UUID id, 
        String username, 
        String email, 
        String phone,
        String firstName,
        String lastName,
        String service,
        List<String> roles, 
        List<String> permissions
    ) {}    
}