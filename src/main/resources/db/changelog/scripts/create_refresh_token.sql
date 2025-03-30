--liquibase formatted sql
--changeset ikiwq:create-user-refresh-token-table splitStatements:true endDelimiter:;
CREATE TABLE IF NOT EXISTS refresh_token (
    id VARCHAR(36) PRIMARY KEY,
    value VARCHAR(255) NOT NULL,
    enabled TINYINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user_data(id)
);
CREATE INDEX refresh_token_id_idx ON refresh_token(id);