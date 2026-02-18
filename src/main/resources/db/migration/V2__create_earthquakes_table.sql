CREATE TABLE earthquakes (
    id              BIGSERIAL PRIMARY KEY,
    earthquake_id   VARCHAR(50) NOT NULL UNIQUE,
    provider        VARCHAR(50) DEFAULT 'kandilli',
    title           VARCHAR(500) NOT NULL,
    magnitude       DOUBLE PRECISION NOT NULL,
    depth_km        DOUBLE PRECISION NOT NULL,
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    event_time      TIMESTAMPTZ NOT NULL,
    closest_city    VARCHAR(255),
    raw_json        JSONB,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_earthquakes_event_time ON earthquakes (event_time DESC);
CREATE INDEX idx_earthquakes_magnitude ON earthquakes (magnitude);
