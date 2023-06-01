--user
DROP TABLE IF EXISTS customer cascade;
DROP TABLE IF EXISTS movie_genres cascade;
CREATE TABLE customer
(
id serial primary key,
name varchar UNIQUE,
password varchar
);
INSERT INTO customer(name, password) VALUES ('Fero', 'Heslo');
INSERT INTO customer
SELECT
	   i AS id,
       'User' || i AS name,
       'heslo' AS heslo
       FROM generate_series(2,500000) AS seq(i);

CREATE SEQUENCE customer_secq AS integer START 500001 OWNED BY customer.id;

ALTER TABLE customer ALTER COLUMN id SET DEFAULT nextval('customer_secq');


--Genres
--ACTION ADVENTURE COMEDY DRAMA FANTASY HISTORICAL HORROR MYSTERY MUSICAL
DROP TABLE IF EXISTS genres cascade;
CREATE TABLE genres
(
id serial primary key,
genre varchar UNIQUE
);

INSERT INTO genres(genre)
VALUES
('ACTION'), ('ADVENTURE'), ('COMEDY'), ('DRAMA'), ('FANTASY'), ('HISTORICAL'), ('HORROR'), ('MYSTERY') , ('MUSICAL');

--movies
DROP TABLE IF EXISTS movies cascade;
CREATE TABLE movies AS
SELECT i AS id,
       'Film' || i AS name,
       floor(random() * 21 + 2000)::integer AS year,
       floor(random() * 20 + 60)::integer AS duration
       FROM generate_series(1,50) AS seq(i);
       CREATE SEQUENCE my_serial AS integer START 51 OWNED BY movies.id;

       ALTER TABLE movies ALTER COLUMN id SET DEFAULT nextval('my_serial');

       ALTER TABLE movies
       ADD UNIQUE(name, year);
       ALTER TABLE movies
       ADD UNIQUE(id);


--movie genres
DROP TABLE IF EXISTS movie_genres cascade;
CREATE TABLE movie_genres AS
SELECT
    floor(random() * 50 + 1)::integer AS movie_id,
    floor(random() * 8 + 1)::integer AS genres_id
    FROM generate_series(1,200) AS seq(i);
ALTER TABLE movie_genres
ADD FOREIGN KEY (movie_id) REFERENCES movies(id),
ADD FOREIGN KEY (genres_id) REFERENCES genres(id);

--theater
DROP TABLE IF EXISTS theater cascade;
CREATE TABLE theater
(
id serial primary key,
numberofseats integer,
name varchar UNIQUE
);
INSERT INTO theater(name, numberofseats) VALUES('A', 50);
INSERT INTO theater(name, numberofseats) VALUES('B', 20);
INSERT INTO theater(name, numberofseats) VALUES('C', 30);
INSERT INTO theater(name, numberofseats) VALUES('D', 10);

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

INSERT INTO screening(price, opening, movie_id, theater_id) VALUES
(20, current_timestamp + (20 * interval '1 minute') , 1, 1),
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --2
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --3
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --4
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --5
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --6
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --7
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1), --8
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), floor(random() * 8 + 1)::integer, 1)  --9
;
INSERT INTO screening
SELECT
	   i AS id,
       floor(random() * 10 + 10)::numeric AS price,
       current_timestamp - (random() * (interval '1825 days')) AS opening,
       floor(random() * 50 + 1)::integer AS movie_id,
	   floor(random() * 4 + 1)::integer AS theater_id
       FROM generate_series(10,9000) AS seq(i); --5 filmov za den

CREATE SEQUENCE ser AS integer START 9001 OWNED BY screening.id;

ALTER TABLE screening ALTER COLUMN id SET DEFAULT nextval('ser');


DROP TABLE IF EXISTS reservations cascade;
CREATE TABLE reservations
(
id serial primary key,
expire TIMESTAMP,
price numeric,
customer_id integer REFERENCES customer(id) ON DELETE CASCADE,
screening_id integer REFERENCES screening(id) ON DELETE CASCADE,
status boolean DEFAULT FALSE
);


--uplne nahodne rezervacie
INSERT INTO reservations(price, expire, customer_id, screening_id, status) VALUES
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --1
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --2
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --3
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --4
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --5
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --6
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --7
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --8
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true), --9
(floor(random() * 10 + 10)::numeric, NOW() - (random() * (interval '90 days')), 1, floor(random() * 8 + 2)::integer, true); --10

INSERT INTO reservations
SELECT
	   i AS id,
	   current_timestamp - (random() * (interval '1825 days')) AS expire,
       floor(random() * 50 + 10)::numeric AS price,
	   floor(random() * 500000 + 1)::integer AS customer_id,
       floor(random() * 1999 + 2)::integer AS screening_id,
	   TRUE as status
       FROM generate_series(11,2500000) AS seq(i); --30 objednavok

CREATE SEQUENCE reser_seq AS integer START 2500001 OWNED BY reservations.id;

ALTER TABLE reservations ALTER COLUMN id SET DEFAULT nextval('reser_seq');


--seat
DROP TABLE IF EXISTS seat cascade;
CREATE TABLE seat
(
id serial primary key,
number integer,
reservation_id integer,
screening_id integer
);

INSERT INTO seat
SELECT
	   i AS id,
	   i AS number,
       floor(random() * 2499998 + 2)::integer AS reservation_id,
	   floor(random() * 8998 + 2)::integer AS screening_id
       FROM generate_series(1,4000000) AS seq(i); --30 objednavok

CREATE SEQUENCE seat_seq AS integer START 4000001 OWNED BY seat.id;

ALTER TABLE seat ALTER COLUMN id SET DEFAULT nextval('seat_seq');

ALTER TABLE seat
ADD FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
ADD FOREIGN KEY (screening_id) REFERENCES screening(id)  ON DELETE CASCADE;


 ALTER TABLE seat
 ADD UNIQUE(screening_id, number);

DROP TABLE IF EXISTS kupon cascade;
CREATE TABLE kupon
(
id serial primary key,
customer_id integer,
code varchar UNIQUE,
reservation_id integer REFERENCES reservations(id) ON DELETE NULL
);
