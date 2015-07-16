
create table book (
  book_id VARCHAR(256) PRIMARY KEY,

  name VARCHAR(256),
  abbreviation VARCHAR(256),
  testament VARCHAR(256)
);

create table congregation (
  congregation_id VARCHAR(256) PRIMARY KEY,

  name VARCHAR(256),
  full_name VARCHAR(256)
);

create table minister (
  minister_id VARCHAR(256) PRIMARY KEY,

  first_name VARCHAR(256),
  middle_name VARCHAR(256),
  last_name VARCHAR(256),
);

create table sermon (
  sermon_id VARCHAR(256) PRIMARY KEY,

  congregation_id VARCHAR(256),
  date DATE,
  minister VARCHAR(256),
  bible_text VARCHAR(256),
  comments VARCHAR(256),
  file_url VARCHAR(256),
);