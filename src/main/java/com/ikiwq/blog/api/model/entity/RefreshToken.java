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

    private String value;
    private boolean enabled;

    private Instant expiresAt;

    @Column(name = "user_id")
    private long userId;

    public RefreshToken(String id, String value, long userId, Instant expiresAt) {
        this.id = id;
        this.value = value;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.enabled = true;
    }
}
