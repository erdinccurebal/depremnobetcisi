CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    telegram_chat_id BIGINT NOT NULL UNIQUE,
    telegram_username VARCHAR(255),
    latitude        DOUBLE PRECISION NOT NULL DEFAULT 0,
    longitude       DOUBLE PRECISION NOT NULL DEFAULT 0,
    notification_radius_km DOUBLE PRECISION DEFAULT 100.0,
    min_magnitude   DOUBLE PRECISION DEFAULT 4.0,
    is_active       BOOLEAN DEFAULT TRUE,
    conversation_state VARCHAR(50) DEFAULT 'IDLE',
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_users_active_location ON users (is_active, latitude, longitude) WHERE is_active = TRUE;
