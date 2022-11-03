CREATE TABLE candidate(
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created TIMESTAMP,
    photo BYTEA,
    city_id INTEGER
)