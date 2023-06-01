--user
DROP TABLE IF EXISTS customer cascade;
DROP TABLE IF EXISTS movie_genres cascade;
CREATE TABLE customer
(
id serial primary key,
name varchar UNIQUE,
password varchar
);


--Genres
--ACTION ADVENTURE COMEDY DRAMA FANTASY HISTORICAL HORROR MYSTERY MUSICAL
DROP TABLE IF EXISTS genres cascade;
CREATE TABLE genres
(
id serial primary key,
genre varchar UNIQUE
);


--movies
DROP TABLE IF EXISTS movies cascade;
CREATE TABLE movies (
id serial primary key,
name varchar,
year integer,
duration integer,
UNIQUE(name, year);
)

--movie genres

DROP TABLE IF EXISTS movie_genres cascade;
CREATE TABLE movie_genres
(
movie_id integer REFERENCES movies(id),
genres_id integer REFERENCES genres(id)
);

--theater
DROP TABLE IF EXISTS theater cascade;
CREATE TABLE theater
(
id serial primary key,
numberofseats integer,
name varchar UNIQUE
);


DROP TABLE IF EXISTS screening cascade;
--screening
CREATE TABLE screening
(
id serial primary key,
price numeric,
opening TIMESTAMP,
movie_id integer REFERENCES movies(id) ON DELETE CASCADE,
theater_id integer REFERENCES theater(id) ON DELETE CASCADE
);


DROP TABLE IF EXISTS reservations cascade;
CREATE TABLE reservations
(
id serial primary key,
--type
expire TIMESTAMP,
price numeric,
customer_id integer REFERENCES customer(id) ON DELETE CASCADE,
screening_id integer REFERENCES screening(id) ON DELETE CASCADE,
status boolean DEFAULT FALSE
);


DROP TABLE IF EXISTS seat cascade;
CREATE TABLE seat
(
id serial primary key,
number integer,
reservation_id integer REFERENCES reservations(id) ON DELETE CASCADE,
screening_id integer REFERENCES screening(id) ON DELETE CASCADE
);

ALTER TABLE seat
ADD UNIQUE(screening_id, number);

DROP TABLE IF EXISTS kupon cascade;
CREATE TABLE kupon
(
id serial primary key,
customer_id integer,
code varchar UNIQUE,
reservation_id integer REFERENCES reservations(id) ON DELETE CASCADE
);


