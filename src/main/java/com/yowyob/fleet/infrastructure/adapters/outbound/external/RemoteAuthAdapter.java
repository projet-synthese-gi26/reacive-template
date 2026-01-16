package com.yowyob.fleet.infrastructure.adapters.outbound.external;

import java.util.List;
import java.util.UUID;

import com.yowyob.fleet.domain.ports.out.AuthPort;
import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.AuthApiClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class RemoteAuthAdapter implements AuthPort {

    private final AuthApiClient authApiClient;
    
    private static final String SERVICE_NAME = "FLEET_MANAGEMENT";

    public RemoteAuthAdapter(AuthApiClient authApiClient) {
        this.authApiClient = authApiClient;
    }

    @Override
    public Mono<AuthResponse> login(String identifier, String password) {
        return authApiClient.authenticate(new AuthApiClient.LoginRequest(identifier, password))
                .map(this::mapToAuthResponse);
    }

    @Override
    public Mono<AuthResponse> register(String username, String password, String email, String phone, 
                                     String firstName, String lastName, List<String> roles) {

        List<String> rolesToSend = (roles == null || roles.isEmpty()) ? List.of("USER") : roles;
        
        AuthApiClient.RegisterRequest request = new AuthApiClient.RegisterRequest(
            username, password, email, phone, firstName, lastName, SERVICE_NAME, rolesToSend
        );  
    return authApiClient.register(request)
                .map(this::mapToAuthResponse)
                .doOnError(e -> log.error("Erreur Register: {}", e.getMessage()));
    }

    @Override
    public Mono<UserDetail> me(String accessToken) {
        return authApiClient.getCurrentUser(ensureBearer(accessToken))
                .map(this::mapToDomainUserDetail);
    }

    @Override
    public Mono<AuthResponse> refresh(String refreshToken) {
        return authApiClient.refreshToken(new AuthApiClient.RefreshRequest(refreshToken))
                .map(this::mapToAuthResponse);
    }

    @Override
    public Mono<Void> logout(UUID userId, String accessToken) {
        log.info("Tentative de logout pour l'utilisateur {}", userId);
        return authApiClient.logout(userId, ensureBearer(accessToken));
    }

    private String ensureBearer(String token) {
        if (token == null) return "";
        return token.startsWith("Bearer ") ? token : "Bearer " + token;
    }

    private AuthResponse mapToAuthResponse(AuthApiClient.TraMaSysResponse res) {
        // res.user() est maintenant de type AuthApiClient.UserDetailResponse
        return new AuthResponse(
            res.accessToken(), 
            res.refreshToken(), 
            mapToDomainUserDetail(res.user()) 
        );
    }

    private UserDetail mapToDomainUserDetail(AuthApiClient.UserDetailResponse res) {
        if (res == null) return null;
        return new UserDetail(
            res.id(), 
            res.username(), 
            res.email(), 
            res.phone(),
            res.firstName(), 
            res.lastName(), 
            res.service(),
            res.roles(), 
            res.permissions()
        );
    }

    @Override
    public Mono<Void> forgotPassword(String email) { return Mono.empty(); }
}