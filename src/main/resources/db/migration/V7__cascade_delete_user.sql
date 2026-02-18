ALTER TABLE help_requests DROP CONSTRAINT help_requests_user_id_fkey;
ALTER TABLE help_requests ADD CONSTRAINT help_requests_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE notification_log DROP CONSTRAINT notification_log_user_id_fkey;
ALTER TABLE notification_log ADD CONSTRAINT notification_log_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
