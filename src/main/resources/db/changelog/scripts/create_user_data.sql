--liquibase formatted sql
--changeset ikiwq:create-user-data-table splitStatements:true endDelimiter:;
CREATE TABLE IF NOT EXISTS user_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) UNIQUE NOT NULL,
    image VARCHAR(255),
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP
);
CREATE INDEX idx_user_id ON user_data(id);
CREATE INDEX idx_user_username ON user_data(username);
