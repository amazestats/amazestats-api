CREATE TABLE IF NOT EXISTS amaze_users(
  id SERIAL PRIMARY KEY,
  alias VARCHAR(50) UNIQUE,
  password VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS competitions(
  id SERIAL PRIMARY KEY,
  key VARCHAR(50) UNIQUE,
  name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS competition_admins(
  admin INTEGER REFERENCES amaze_users(id),
  competition INTEGER REFERENCES competitions(id),
  PRIMARY KEY (admin, competition)
);

CREATE TABLE IF NOT EXISTS divisions(
  id SERIAL PRIMARY KEY,
  key VARCHAR(50),
  name VARCHAR(50),
  competition INTEGER REFERENCES competitions(id),
  UNIQUE (key, competition),
  UNIQUE (key, name)
);

CREATE TABLE IF NOT EXISTS division_admins(
  admin INTEGER REFERENCES amaze_users(id),
  division INTEGER REFERENCES divisions(id),
  PRIMARY KEY (admin, division)
);

CREATE TABLE IF NOT EXISTS seasons(
  id SERIAL PRIMARY KEY,
  key VARCHAR(50),
  name VARCHAR(50),
  division INTEGER REFERENCES divisions(id),
  start_date TIMESTAMP,
  end_date TIMESTAMP,
  UNIQUE (key, division)
);

CREATE TABLE IF NOT EXISTS teams(
  id SERIAL PRIMARY KEY,
  key VARCHAR(50),
  name VARCHAR(50),
  activated BOOLEAN NOT NULL DEFAULT FALSE,
  competition INTEGER REFERENCES competitions(id) NOT NULL,
  division INTEGER REFERENCES divisions(id),
  UNIQUE (key, competition),
  UNIQUE (name, competition)
);

CREATE TABLE IF NOT EXISTS matches(
  id SERIAL PRIMARY KEY,
  season INTEGER REFERENCES seasons(id),
  home_team INTEGER REFERENCES teams(id),
  away_team INTEGER REFERENCES teams(id),
  referee INTEGER REFERENCES teams(id),
  match_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sets(
  id SERIAL PRIMARY KEY,
  match INTEGER REFERENCES matches(id),
  home_score INTEGER,
  away_score INTEGER
);
