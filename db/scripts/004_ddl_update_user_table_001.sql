ALTER TABLE users
    ADD COLUMN email    TEXT UNIQUE,
    ADD COLUMN password TEXT;
