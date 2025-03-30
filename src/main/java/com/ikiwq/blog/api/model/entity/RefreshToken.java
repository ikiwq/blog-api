package com.ikiwq.blog.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String id;

    @Column(nullable = false)
    private String value;
    @Column(nullable = false)
    private boolean enabled;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private BlogUser user;

    public RefreshToken(String id, String value, long userId, Instant expiresAt) {
        this.id = id;
        this.value = value;
        this.user = new BlogUser(userId);
        this.expiresAt = expiresAt;
        this.enabled = true;
    }
}
