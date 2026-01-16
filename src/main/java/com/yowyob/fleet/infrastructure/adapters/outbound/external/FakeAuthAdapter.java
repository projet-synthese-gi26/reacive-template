package com.yowyob.fleet.infrastructure.adapters.outbound.external;

import com.yowyob.fleet.domain.ports.out.AuthPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.UUID;

@Slf4j
public class FakeAuthAdapter implements AuthPort {

    @Override
    public Mono<AuthResponse> login(String identifier, String password) {
        log.info("🛠 MODE FAKE AUTH : Login pour {}", identifier);
        UserDetail fakeUser = createFakeUser(identifier, "fake-email@yowyob.com");
        return Mono.just(new AuthResponse("fake-access-token", "fake-refresh-token", fakeUser));
    }

    @Override
    public Mono<AuthResponse> register(String username, String password, String email, String phone, String firstName, String lastName, List<String> roles) {
        log.info("🛠 MODE FAKE AUTH : Inscription pour {}", username);
        UserDetail newUser = new UserDetail(
            UUID.fromString("8a1f5e2c-3d4b-4c6a-9f8e-123456789abc"), username, email, phone, 
            firstName, lastName, "FLEET_MANAGEMENT", 
            roles, List.of("*")
        );
        return Mono.just(new AuthResponse("fake-access-token", "fake-refresh-token", newUser));
    }

    @Override
    public Mono<UserDetail> me(String accessToken) {
        log.info("🛠 MODE FAKE AUTH : Récupération du profil (me)");
        return Mono.just(createFakeUser("fake_admin", "admin@yowyob.com"));
    }

    @Override
    public Mono<AuthResponse> refresh(String refreshToken) {
        log.info("🛠 MODE FAKE AUTH : Rafraîchissement du token");
        UserDetail fakeUser = createFakeUser("fake_admin", "admin@yowyob.com");
        return Mono.just(new AuthResponse("new-fake-access-token", "new-fake-refresh-token", fakeUser));
    }

    @Override
    public Mono<Void> logout(UUID userId, String accessToken) {
        log.info("🛠 MODE FAKE AUTH : Logout effectué pour le token {}", userId);
        return Mono.empty();
    }

    @Override
    public Mono<Void> forgotPassword(String email) {
        log.info("🛠 MODE FAKE AUTH : Demande de réinitialisation pour {}", email);
        return Mono.empty();
    }

    /**
     * Helper pour créer un utilisateur de domaine factice cohérent.
     */
    private UserDetail createFakeUser(String username, String email) {
        return new UserDetail(
            UUID.fromString("8a1f5e2c-3d4b-4c6a-9f8e-123456789abc"),
            username,
            email,
            "+237600000000",
            "Fake",
            "User",
            "FLEET_MANAGEMENT",
            List.of("ADMIN", "FLEET_MANAGER"),
            List.of("fleet:read", "fleet:write", "vehicle:all")
        );
    }
}