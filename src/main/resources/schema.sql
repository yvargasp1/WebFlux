CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,      -- auto-increment integer primary key
    name VARCHAR(255) UNIQUE,   -- unique constraint on the name column
    price FLOAT                 -- price stored as a floating point number
);
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,      -- auto-increment integer primary key
    username VARCHAR(255) UNIQUE,   -- unique constraint on the name column
    email VARCHAR(255) UNIQUE,   -- unique constraint on the name column
    password VARCHAR(255),   -- unique constraint on the name column
    roles VARCHAR(255)
);

