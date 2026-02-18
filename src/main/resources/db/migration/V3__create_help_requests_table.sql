CREATE TABLE help_requests (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES users(id),
    full_name       VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(20) NOT NULL,
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    address_text    VARCHAR(1000),
    need_types      VARCHAR(500) NOT NULL,
    description     TEXT,
    kvkk_consent    BOOLEAN NOT NULL CHECK (kvkk_consent = TRUE),
    status          VARCHAR(20) DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_help_requests_status ON help_requests (status);
CREATE INDEX idx_help_requests_location ON help_requests (latitude, longitude) WHERE status = 'ACTIVE';
