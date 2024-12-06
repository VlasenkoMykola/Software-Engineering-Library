-- -- for Postgres DB --
-- CREATE DATABASE library WITH ENCODING 'UTF8';
-- CREATEUSER library WITH PASSWORD 'library';

set client_encoding="UTF-8";

BEGIN;

DROP TABLE IF EXISTS Copies;
DROP TABLE IF EXISTS Readers;
DROP TABLE IF EXISTS BookAuthors;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Authors;
DROP TYPE IF EXISTS CopyStatus;

CREATE TYPE CopyStatus AS ENUM ('free', 'booked', 'given', 'lost');

CREATE TABLE Authors (
       id SERIAL PRIMARY KEY,
       -- assume UTF-8 letter: up to 4 bytes
       letter TEXT,
       full_name TEXT,
       first_name TEXT,
       middle_name TEXT,
       last_name TEXT,
       description TEXT
);

\COPY Authors FROM 'lib_authors.tsv' DELIMITER E'\t';

-- CREATE UNIQUE INDEX author_search ON Authors(full_name);
CREATE INDEX author_search ON Authors(full_name);
CREATE INDEX author_lastname ON Authors(last_name);
CREATE INDEX author_letter ON Authors(letter);

CREATE TABLE Books (
       id SERIAL PRIMARY KEY,
       title TEXT,
       annotation TEXT,
       genres TEXT,
       description TEXT,
       lang VARCHAR(2)
       -- year INTEGER
);

-- <slash> COPY Books FROM 'lib_books.tsv' DELIMITER E'\t';
\i 'books.sql';

CREATE TABLE BookAuthors (
       book_id INTEGER,
       author_id INTEGER
--       CONSTRAINT fba_book FOREIGN KEY(book_id) REFERENCES Books(id),
--       CONSTRAINT fba_author FOREIGN KEY(author_id) REFERENCES Authors(id)
);
\COPY BookAuthors FROM 'lib_bookauthors.tsv' DELIMITER E'\t';

ALTER TABLE BookAuthors ADD CONSTRAINT fba_book FOREIGN KEY(book_id) REFERENCES Books(id);
ALTER TABLE BookAuthors ADD CONSTRAINT fba_author FOREIGN KEY(author_id) REFERENCES Authors(id);

CREATE UNIQUE INDEX book2author ON BookAuthors(book_id, author_id);
CREATE UNIQUE INDEX author2book ON BookAuthors(author_id, book_id);

CREATE TABLE Readers (
       -- reader id is also used as library card id
       id SERIAL PRIMARY KEY,
       first_name TEXT NOT NULL,
       middle_name TEXT NOT NULL,
       last_name TEXT NOT NULL,
       address TEXT,
       phone TEXT,
       email TEXT,
       registration_date TIMESTAMP NOT NULL,
       valid_to TIMESTAMP
);

CREATE TABLE Copies (
       id SERIAL PRIMARY KEY,
       book_id INTEGER NOT NULL,
       reader_id INTEGER,
       status CopyStatus,
       status_change TIMESTAMP,
       book_or_lend_from DATE,
       book_or_lend_to DATE,
       CONSTRAINT fcc_book FOREIGN KEY(book_id) REFERENCES Books(id)
);

-- 1-2 copies of each book
INSERT INTO Copies (book_id, status) SELECT id, 'free' FROM Books;
INSERT INTO Copies (book_id, status) SELECT id, 'free' FROM Books LIMIT 500;

CREATE UNIQUE INDEX book2copy ON Copies(book_id, id);
ALTER TABLE Copies ADD CONSTRAINT fcc_reader FOREIGN KEY(reader_id) REFERENCES Readers(id);

COMMIT;
-- -------------------------------------------------
