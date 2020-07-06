
-- SET search_path TO dev;
-- SET search_path TO prod;

CREATE TABLE management_refresh_tokens (
    id BIGSERIAL NOT NULL,
    token_id VARCHAR(255) NOT NULL UNIQUE,
    refresh_token TEXT NOT NULL,
    PRIMARY KEY (id)
);
