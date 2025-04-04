--liquibase formatted sql
--changeset ikiwq:create-user-category-table splitStatements:true endDelimiter:;
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    slug VARCHAR(255) NOT NULL UNIQUE,
    image VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX category_id_idx ON category(id);
CREATE INDEX category_slug_idx ON category(slug);
