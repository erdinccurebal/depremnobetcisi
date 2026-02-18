CREATE TABLE notification_log (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id),
    earthquake_id   BIGINT NOT NULL REFERENCES earthquakes(id),
    distance_km     DOUBLE PRECISION NOT NULL,
    message_text    TEXT NOT NULL,
    delivery_status VARCHAR(20) DEFAULT 'SENT',
    sent_at         TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, earthquake_id)
);

CREATE INDEX idx_notification_log_user ON notification_log (user_id);
