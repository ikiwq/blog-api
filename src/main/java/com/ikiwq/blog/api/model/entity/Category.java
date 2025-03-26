package com.ikiwq.blog.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(unique = true, nullable = false)
    private String slug;

    private String image;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Article> articles;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
