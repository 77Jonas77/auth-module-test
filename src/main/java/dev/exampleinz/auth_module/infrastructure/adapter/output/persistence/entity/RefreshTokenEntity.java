package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @MapsId
    @JoinColumn(name="id", nullable = false)
    private UserJpaEntity user;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

//    public RefreshTokenEntity(UserJpaEntity user, Instant createdAt, Instant expiresAt) {
//        this.user = user;
//        this.createdAt = createdAt;
//        this.expiresAt = expiresAt;
//    }

    public RefreshTokenEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
