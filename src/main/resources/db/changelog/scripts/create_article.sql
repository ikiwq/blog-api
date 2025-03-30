--liquibase formatted sql
--changeset ikiwq:create-user-article-table splitStatements:true endDelimiter:;
CREATE TABLE IF NOT EXISTS article (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    excerpt TEXT,
    content TEXT,
    slug VARCHAR(255) NOT NULL UNIQUE,
    image VARCHAR(255),
    pinned BOOLEAN NOT NULL,
    author_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (author_id) REFERENCES user_data(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);
CREATE INDEX article_id_idx ON article(id);
CREATE INDEX article_slug_idx ON article(slug);