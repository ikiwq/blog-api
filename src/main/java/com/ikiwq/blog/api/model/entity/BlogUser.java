package com.ikiwq.blog.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikiwq.blog.api.model.enumeration.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "user_data")
@Data
@NoArgsConstructor
public class BlogUser {
    public BlogUser(long id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Article> articles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;

    @Column(name = "createdAt", updatable = false)
    private Instant createdAt;
}
