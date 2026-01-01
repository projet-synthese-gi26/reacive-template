package com.yowyob.fleet.domain.ports.out;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

// import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.AuthApiClient.UserDetailResponse;

public interface AuthPort {
    Mono<AuthResponse> login(String email, String password);
    
    Mono<Void> forgotPassword(String email);

    Mono<AuthResponse> register(
        String username, 
        String password, 
        String email, 
        String phone, 
        String firstName, 
        String lastName,
        List<String> roles);

     // Nouvelles méthodes
    Mono<UserDetail> me(String accessToken);

    Mono<AuthResponse> refresh(String refreshToken);

    Mono<Void> logout(UUID userId, String accessToken);
    
    record AuthResponse(
        String accessToken, 
        String refreshToken, 
        UserDetail user
    ) {}



    record UserDetail(
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