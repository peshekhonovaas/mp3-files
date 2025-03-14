CREATE TABLE IF NOT EXISTS song_meta_data (
    id serial PRIMARY KEY,
    name VARCHAR (100) NOT NULL,
    artist VARCHAR (100) NOT NULL,
    album VARCHAR (100) NOT NULL,
    duration VARCHAR (100) NOT NULL,
    year VARCHAR (100) NOT NULL
);