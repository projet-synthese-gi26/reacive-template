package com.yowyob.fleet.domain.ports.in;

import java.util.List;
import java.util.UUID;

import com.yowyob.fleet.domain.ports.out.AuthPort;
import reactor.core.publisher.Mono;

public interface AuthUseCase {
    Mono<AuthPort.AuthResponse> login(String identifier, String password);
    
    Mono<Void> resetPassword(String email);

    Mono<AuthPort.AuthResponse> register(
        String username, 
        String password, 
        String email, 
        String phone, 
        String firstName, 
        String lastName,
        List<String> roles
    );

    Mono<AuthPort.UserDetail> me(String token);

    Mono<AuthPort.AuthResponse> refreshToken(String refreshToken);

    Mono<Void> logout(UUID userId, String accessToken);
}