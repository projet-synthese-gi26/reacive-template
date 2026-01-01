package com.yowyob.fleet.application.service;

import com.yowyob.fleet.domain.ports.in.AuthUseCase;
import com.yowyob.fleet.domain.ports.out.AuthPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthPort authPort;

    @Override
    public Mono<AuthPort.AuthResponse> login(String identifier, String password) {
        return authPort.login(identifier, password);
    }

    @Override
    public Mono<AuthPort.AuthResponse> register(String username, String password, String email, String phone, String firstName, String lastName, List<String> roles) {
        return authPort.register(username, password, email, phone, firstName, lastName, roles);
    }

    @Override
    public Mono<Void> resetPassword(String email) {
        return authPort.forgotPassword(email);
    }

    @Override
    public Mono<AuthPort.UserDetail> me(String token) {
        return authPort.me(token);
    }

    @Override
    public Mono<AuthPort.AuthResponse> refreshToken(String refreshToken) {
        return authPort.refresh(refreshToken);
    }

    @Override
    public Mono<Void> logout(UUID userId, String accessToken) {
        return authPort.logout(userId, accessToken);
    }
}